package com.williamgromme.kotlincard.setcard

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.*
import android.widget.TextView
import com.williamgromme.kotlincard.flashcard.FlashCardListActivity
import com.williamgromme.kotlincard.R
import com.williamgromme.kotlincard.miss.SwipeController
import com.williamgromme.kotlincard.miss.SwipeControllerActions
import com.williamgromme.kotlincard.db.SetCard
import com.williamgromme.kotlincard.db.MyApp
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by Dad on 11/27/2017.
 */


class SetCardFragment: Fragment() {

    private lateinit var setRecyclerView:RecyclerView
    //TODO delete setArrayList
    private lateinit var setArrayList: ArrayList<SetCard>
    private lateinit var setCardAdapter: SetAdapter
    private final val TAG = "SetCardFragment"
    private var  setCardsArrayList = ArrayList<SetCard>()
   // private final val ARG_SETCARD_ID = "set_uid"

    enum class ButtonState {
        GONE,
        RIGHT_VISIBLE,
        LEFT_VISIBLE
    }

    companion object {
       // private final val ARG_SETCARD_ID = "set_uid"
        fun newInstance():SetCardFragment {
            return SetCardFragment()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setArrayList = ArrayList<SetCard>()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater!!.inflate(R.layout.fragment_set_list,container, false)
        retainInstance
        setRecyclerView= view.findViewById(R.id.set_recycler_view)
        setRecyclerView.layoutManager = LinearLayoutManager(context)


        var swipeControllerSet = SwipeController(object : SwipeControllerActions() {
            override fun onLeftClicked(position: Int) {
                super.onLeftClicked(position)
                //Log.d(TAG, "Left/Edit recycler view button clicked for id:" + position)
                //Start activity
                val intent = EditSetCardActivity.newIntent(context, setCardsArrayList[position].uid)
                startActivity(intent)
            }

            override fun onRightClicked(position: Int) {
                super.onRightClicked(position)
                //Log.d(TAG, "Right/Delete recycler view button clicked id" + position)
                deleteSet(position)
            }
        })

        var itemTouchhelper = ItemTouchHelper(swipeControllerSet);
        itemTouchhelper.attachToRecyclerView(setRecyclerView);

        setRecyclerView.addItemDecoration( object :RecyclerView.ItemDecoration() {

            override fun onDraw(c: Canvas?, parent: RecyclerView?, state: RecyclerView.State?) {
                swipeControllerSet.onDraw(c)
            }
        })

        setCardListener()
        return view
        //return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.fragment_set_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.new_set) {
           // Log.d(TAG, "New set menu item pressed")
            //addSet()
            val intent = NewSetCardActivity.newIntent(context)
            startActivity(intent)
        }else if (item?.itemId == R.id.app_help) {
            //Log.d(TAG, "Help menu item pressed")
            //Start activity to display help information
            val intent = Intent(context, HelpActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    fun setCardListener () {
        MyApp.dataBase.setCardDao().allSetsSorted
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
            setCards ->
                    setCardsArrayList.clear()
                    setCardsArrayList.addAll(setCards)
                   //Log.d(TAG, "the size of list is: " + setCardsArrayList.size)

                    setCardAdapter = SetAdapter(setCardsArrayList,context)
                    setRecyclerView.adapter = setCardAdapter
                    setCardAdapter.notifyDataSetChanged()
        }
    }

//    fun addSet() {
//
//        Single.fromCallable {
//            val num = Random().nextInt()
//            val setCard = SetCard("Bubbas" + num, "Kotlin", "Us or them", "https://raw.githubusercontent.com/WillyScott/FlashCardsData/master/Swift_KeywordsV3_0_1.json")
//            MyApp.dataBase.setCardDao().insert(setCard)
//        }
//                .subscribeOn(Schedulers.io())
//                .subscribeBy(
//                        onSuccess = { intCode ->
//                            Log.d(TAG, "SetCard added: " + intCode)
//                        },
//                        onError = {error ->
//                            Log.e(TAG, "Couldn't write SetCard to database", error)
//                        }
//                )
//    }

    fun deleteSet(num: Int) {
        Single.fromCallable {
           // Log.d(TAG,"Deleting setid: " + setCardsArrayList[num].uid)
            MyApp.dataBase.setCardDao().delete(setCardsArrayList[num])
        }.subscribeOn(Schedulers.io())
                .subscribeBy(
                        onSuccess = { setcard ->
                            //Log.d(TAG, "SetCard deleted.")
                        },
                        onError = { error ->
                           // Log.e(TAG, "Couldn't delete SetCard from database", error)
                        }
                )
    }

    fun updateSet() {

    }

    internal inner class  RecyclerSwipeActions : SwipeControllerActions(){
        //private final val TAG = "RecyclerSwipeActions"
        override fun onLeftClicked(position: Int) {
            super.onLeftClicked(position)
            //Log.d(TAG, "Left/Edit recycler view button clicked for id:" + position )
        }

        override fun onRightClicked(position: Int) {
            super.onRightClicked(position)
            //Log.d(TAG, "Right/Delete recycler view button clicked id" + position)
        }
    }

//RecyclerView
    internal inner class SetHolder (v :View) :RecyclerView.ViewHolder(v),View.OnClickListener  {
        private var setName : TextView
        private var setDescription: TextView
        private var setSection:TextView
        private var setCount: TextView



        private lateinit var setCard :SetCard
        //final private val TAG = "SetHolder"

        init {
            setName = v.findViewById<TextView>(R.id.set_name) as TextView
            setDescription = v.findViewById<TextView>(R.id.set_description) as TextView
            setSection = v.findViewById<TextView>(R.id.set_section) as TextView

            setCount = v.findViewById<TextView>(R.id.set_card_count) as TextView
            itemView.setOnClickListener(this)
        }

        fun bind (set: SetCard) {
            setCard = set
            setName.setText(setCard.name)
            setDescription.setText(setCard.description)
            setSection.setText(setCard.section)
            //Log.d(TAG,"card count is " + setCard.count)

            setCount.setText(set.countShow.toString() +"/" + set.count.toString())
        }

        override fun onClick(p0: View) {
           // Log.d(TAG, "onclick:" + setCard.name)
            val intent = FlashCardListActivity.newIntent(p0.context,setCard.uid)
            p0.context.startActivity(intent)
        }

    }

    internal inner class SetAdapter ( var setCard : ArrayList<SetCard>?, val context: Context?): RecyclerView.Adapter<SetHolder> () {

        final private val TAG = "SetAdapter"
        override fun onBindViewHolder(holder: SetHolder, position: Int) {
            holder.bind(setCard!![position])
        }

        override fun getItemCount(): Int {
            //Log.d(TAG, "the size of setCard is: " + setCard!!.size)
            return setCard!!.size
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SetHolder {
            var layoutInflator = LayoutInflater.from(parent!!.context)
            return SetHolder(layoutInflator.inflate(R.layout.recyclerview_set_row, parent, false))
        }
    }

}