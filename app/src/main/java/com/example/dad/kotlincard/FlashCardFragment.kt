package com.example.dad.kotlincard

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.TextView
import com.example.dad.kotlincard.card.Card
import db.FlashCard
import db.MyApp
import io.reactivex.Single
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


/**
 * Created by Dad on 10/22/2017.
 */
class FlashCardFragment :Fragment() {

    private  lateinit var  cardArrayList: ArrayList<Card>
    private final val TAG = "FlashCardFragment"
    private lateinit var cardRecyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter
    private lateinit var bottomNavView: BottomNavigationView

    companion object {
        fun newInstance(): FlashCardFragment {
            return FlashCardFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.fragment_card_list,menu)
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        cardArrayList = ArrayList<Card>()

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if  ( item?.itemId == R.id.new_card ){
            Log.d(TAG, "New Card button selected")
            return true
        }else if (item?.itemId == R.id.exportCSV) {
            Log.d(TAG, "Export to csv selected")
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
        val activity = activity
       // linearLayoutManager = LinearLayoutManager(context)
        cardRecyclerView.layoutManager = LinearLayoutManager(activity)
        //card_recycler_view.layoutManager = LinearLayoutManager(activity)

        //wire up the bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener {})
        //

        bottomNavView = view.findViewById<BottomNavigationView>(R.id.bottom_navigation) as BottomNavigationView
        bottomNavView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.start -> { Log.d(TAG, "start selected")}
                R.id.resettrue -> {Log.d(TAG, "resettrue selected")}
                R.id.importdata -> {Log.d(TAG, "importdata selected")}
            }

            true
        }

        // remove latter

//        for (i in 0 .. 10){
//            var flashcard = FlashCard( "front" + i, "back" + i)
//
//            MyApp.dataBase.flashCardDao().insert(flashcard)
//
//        }
//
//        var list:List<FlashCard> = MyApp.dataBase.flashCardDao().all
//        Log.d(TAG, "the size of list is: " + list.size)

//        doAsync {
//            //val result = CardFetcher().getJson("test")
//            val cardArrayListTemp = CardFetcher().fetchCards()
//            uiThread {
//                // Log.d(TAG,result)
//                Log.d(TAG, "the length of cards is: " + cardArrayListTemp.size)
//                for (i in 0 .. cardArrayListTemp.size -1) {
//                    Log.d(TAG, cardArrayListTemp[i].toString())
//            }
//            }
//        }

        updateCards()
        Log.d(TAG, "before call to database")
        return view
    }

    fun updateCards()
    {
             doAsync {
            //val result = CardFetcher().getJson("test")
             cardArrayList = CardFetcher().fetchCards()
                uiThread {


                // Log.d(TAG,result)
//                Log.d(TAG, "the length of cards is: " + cardArrayList.size)
//                for (i in 0 .. cardArrayList.size -1) {
//                    Log.d(TAG, cardArrayList[i].toString())
//                }
                updateUI()
                }
            }
    }


    fun testDatabase() {

//        Single.fromCallable {
//            val setCard = SetCard("test", "", "my first", "https")
//            MyApp.dataBase.setCardDao().insert(setCard)
//        }.subscribeOn(Schedulers.io())
//         .subscribeBy { error ->
//                    Log.e(TAG, "Couldn't write to the database:", error)
//                }

        Single.fromCallable {
            for (i in 0 .. 10) {
                val flashCard = FlashCard("test", "test" + i, "test" + i)
                MyApp.dataBase.flashCardDao().insert(flashCard)
            }
        }


    }

    fun updateUI()
    {
        // isAdded function returns boolean is fragment added to activity
        if (isAdded) {

            cardAdapter = CardAdapter(cardArrayList, context)
            cardRecyclerView.adapter = cardAdapter
        }
    }
    internal inner class CardHolder (v: View) :RecyclerView.ViewHolder(v) {
        private var cardBack: TextView
        private var cardFront: TextView

        init {
            // Where does "itemView" come from well ....
            //
            cardFront = itemView.findViewById<TextView>(R.id.cardfront) as TextView
            cardBack = itemView.findViewById<TextView>(R.id.cardback) as TextView

        }

        fun bind(card: Card) {
            cardBack.setText(card.back)
            cardFront.setText(card.front)

        }
    }

    internal inner class CardAdapter(private val cards:ArrayList<Card>?, context: Context?) : RecyclerView.Adapter<CardHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
            val layoutinflator = LayoutInflater.from(parent.context)
            return CardHolder(layoutinflator.inflate(R.layout.recyclerview_item_row,parent,false))

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