package com.example.dad.kotlincard.FlashCard

import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.*
import android.widget.TextView
import com.example.dad.kotlincard.CardFetcher
import com.example.dad.kotlincard.R
import com.example.dad.kotlincard.SwipeController
import com.example.dad.kotlincard.SwipeControllerActions
import com.example.dad.kotlincard.db.FlashCard
import com.example.dad.kotlincard.db.MyApp
import com.example.dad.kotlincard.db.SetCard
import com.example.dad.kotlincard.export.ExportCardsActivity
import com.example.dad.kotlincard.startcards.StartCardsActivity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


/**
 * Created by Dad on 10/22/2017.
 */
class FlashCardFragment :Fragment() {

    private  lateinit var  cardArrayList: ArrayList<FlashCard>
    private final val TAG = "FlashCardFragment"
    private lateinit var cardRecyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter
    private lateinit var bottomNavView: BottomNavigationView
    private final val SETCARD_ID = "setcard_id"
    private var setCard_id:Int = -1
    private lateinit var setCard: SetCard

    companion object {
        private final val SETCARD_ID = "setcard_id"
        fun newInstance(uid: Int): FlashCardFragment {
            var args:Bundle = Bundle()
            args.putInt(SETCARD_ID,uid)
            var fragment = FlashCardFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCard_id = arguments.getInt(SETCARD_ID)

        Log.d(TAG, "The set id is: " + setCard_id)
        // get the set and cards for the set
        getSetcard(setCard_id)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.fragment_card_list,menu)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        cardArrayList = ArrayList<FlashCard>()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if  ( item?.itemId == R.id.new_card){
            Log.d(TAG, "New Card button selected")
            //Start Activity
            val intent = NewFlashCardActivity.newIntent(context,setCard_id)
            startActivity(intent)
            return true
        }else if (item?.itemId == R.id.exportCSV) {
            Log.d(TAG, "Export to csv selected")
            val intent = ExportCardsActivity.newIntent(context,setCard_id)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       // return super.onCreateView(inflater, container, savedInstanceState)

        var view = inflater!!.inflate(R.layout.fragment_card_list, container, false)
        //Do I need this or not  recyclerview stays on last item.
        //retainInstance = true

        //TODO update to  3 lines below see SetCardFragment  wsg
        cardRecyclerView = view.findViewById<RecyclerView>(R.id.card_recycler_view) as RecyclerView
        cardRecyclerView.layoutManager = LinearLayoutManager(context)

        var swipeControllerCard = SwipeController(object :SwipeControllerActions() {
            override fun onLeftClicked(position: Int) {
                super.onLeftClicked(position)
                var intent = EditFlashCardActivity.newIntent(context, cardArrayList[position].uid)
                startActivity(intent)
            }

            override fun onRightClicked(position: Int) {
                super.onRightClicked(position)
                deleteFlashCard(position)
            }
        })

        var itemTouchHelper = ItemTouchHelper(swipeControllerCard)
        itemTouchHelper.attachToRecyclerView(cardRecyclerView)

        cardRecyclerView.addItemDecoration( object :RecyclerView.ItemDecoration() {

            override fun onDraw(c: Canvas?, parent: RecyclerView?, state: RecyclerView.State?) {
                super.onDraw(c, parent, state)
                swipeControllerCard.onDraw(c)
            }
        })

        bottomNavView = view.findViewById<BottomNavigationView>(R.id.bottom_navigation) as BottomNavigationView
        bottomNavView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.start -> {
                    Log.d(TAG, "start selected")
                    Log.d(TAG, "setCard id is:" + setCard.uid)
                    var intent = StartCardsActivity.newIntent(context,setCard.uid,setCard.randomize, setCard.reverse)
                    startActivity(intent)
                }
                R.id.resettrue -> {
                    Log.d(TAG, "resettrue selected")
                    //reset the show field to true
                    updateFlashCardtotrue()
                }
                R.id.importdata -> {
                    Log.d(TAG, "importdata selected")
                    importCards()
                }
            }

            true
        }

        flashCardListener()
        return view
    }

    fun flashCardListener() {

        MyApp.dataBase.flashCardDao().getAll(setCard_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    flashCards ->
                        cardArrayList.clear()
                        cardArrayList.addAll(flashCards)
                        cardAdapter = CardAdapter(cardArrayList, context)
                        cardRecyclerView.adapter = cardAdapter
                        cardAdapter.notifyDataSetChanged()
                        updateSetCard(setCard)
                      //  updateSetCardCountShow(setCard)
                  }
    }

    fun getSetcard(setID :Int){
        Single.fromCallable {
            MyApp.dataBase.setCardDao().getSet(setID)
        }.subscribeOn(Schedulers.io())
                .subscribeBy(
                        onSuccess = {setcard ->
                            setCard = setcard
                        },
                        onError = {error ->
                            Log.e(TAG, "Couldn't get SetCard. " + error)
                        }
                )
    }

    // fun that updates the count of cards in the set and the count
    // of cards that are marked as known (cardShow)
    fun updateSetCard (setCard: SetCard) {
        Single.fromCallable {
            setCard.setCount(cardArrayList.size)
            //loop list and count marked to not show
            var numCardsNotshow = 0
            for(i in 0 .. cardArrayList.size -1){
                if (cardArrayList[i].show == false ){
                    numCardsNotshow = numCardsNotshow + 1
                }
             }
            setCard.setCountShow(numCardsNotshow)
            MyApp.dataBase.setCardDao().update(setCard)
        }.subscribeOn(Schedulers.io())
         .subscribeBy(
                        onSuccess = { num ->
                            Log.d(TAG, "SetCard updated. " + num)
                        },
                        onError = {error ->
                            Log.e(TAG, "Couldn't update SetCard.", error)
                        }
                )
    }


    // Function that  import the cards from a URL (in a JSON format) off the main thread
    fun importCards()  {
        doAsync {
            //Get the URL field to import from
            //TODO need to test for empty and incorrect URL

            if (setCard.urlString.isEmpty()) {
                //TODO:  add a message to user

            }else {

                cardArrayList = CardFetcher().fetchCards(setCard.urlString)
                //If cards were found persist to database first update parent key
                if (cardArrayList.size > 0) {
                    for (i in 0..cardArrayList.size - 1) {
                        cardArrayList[i].set_uid = setCard_id
                        cardArrayList[i].show = true
                    }
                }
                //Save the newly imported flashcards
                MyApp.dataBase.flashCardDao().insertAll(cardArrayList)
                uiThread {
                    // TODO Remove this nothing to do on main thread
                }

            }
        }
    }

    fun deleteFlashCard(num: Int) {
        val flashCard = cardArrayList[num]

        Single.fromCallable {
            MyApp.dataBase.flashCardDao().delete(flashCard)
        } .subscribeOn(Schedulers.io())
          .subscribeBy(
                        onSuccess = { setcard ->
                            Log.d(TAG, "Flashcard deleted.")
                        },
                        onError = {error ->
                            Log.e(TAG, "Couldn't delete FlashCard.", error)
                        }
          )

    }

    fun updateFlashCard(num: Int) {
        Single.fromCallable {
            MyApp.dataBase.flashCardDao().update(cardArrayList[num])
        } .subscribeOn(Schedulers.io())
                .subscribeBy(
                        onSuccess = {flashcard ->
                            Log.d(TAG, "Flashcard updated.")
                        },
                        onError = {error ->
                            Log.e(TAG, "Update failed on FlashCard.", error)
                        }
                )
     }

    fun updateFlashCardtotrue () {
        Single.fromCallable {
            MyApp.dataBase.flashCardDao().updateShowCardTrue(setCard_id)
        }.subscribeOn(Schedulers.io())
                .subscribeBy(
                        onSuccess = { result ->
                            Log.d(TAG, "Update of flashcards show field successful. " + result)
                        },
                        onError = {error ->
                            Log.e(TAG, "Update failed on flashcards show field: " + error)
                        }
                )
    }

    // TODO: Why internal class ?
    internal inner class CardHolder (v: View) :RecyclerView.ViewHolder(v) {
        private var cardBack: TextView
        private var cardFront: TextView
        private var showCard: TextView

        init {
            cardFront = itemView.findViewById<TextView>(R.id.cardfront) as TextView
            cardBack = itemView.findViewById<TextView>(R.id.cardback) as TextView
            showCard = itemView.findViewById<TextView>(R.id.showcard) as TextView
        }

        fun bind(card: FlashCard) {
            //Log.d(TAG, "the back is: " + card.backcard)
            cardBack.setText(card.backcard)
            cardFront.setText(card.frontcard)
            if (card.show == null){
                showCard.setText("true")
            }else if (card.show == true){
                showCard.setText("true")
            }else {
                showCard.setText("false")
            }
        }
    }

    internal inner class CardAdapter(private val cards:ArrayList<FlashCard>?, context: Context?) : RecyclerView.Adapter<CardHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
            val layoutinflator = LayoutInflater.from(parent.context)
            return CardHolder(layoutinflator.inflate(R.layout.recyclerview_card_row,parent,false))
        }

        override fun onBindViewHolder(holder: CardHolder, position: Int) {
            holder.bind(cards!![position])
        }

        override fun getItemCount(): Int {
           //Log.d(TAG, "the card size in getItemCount is: " + cards!!.size)
           return cards!!.size
        }
    }
}