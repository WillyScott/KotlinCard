package com.example.dad.kotlincard.setcard

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.TextView
import com.example.dad.kotlincard.R
import db.SetCard
import db.MyApp
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_set_list.*
import java.util.*

/**
 * Created by Dad on 11/27/2017.
 */
class SetCardFragment: Fragment() {

    private lateinit var setRecyclerView:RecyclerView
    private lateinit var setArrayList: ArrayList<SetCard>
    private lateinit var setCardAdapter: SetAdapter
    private final val TAG = "SetCardFragment"
    private var  setCardsArrayList = ArrayList<SetCard>()

    companion object {
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

        setRecyclerView= view.findViewById(R.id.set_recycler_view)
        setRecyclerView.layoutManager = LinearLayoutManager(context)

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
            Log.d(TAG, "New set menu item pressed")
            addSet()
        }
        return super.onOptionsItemSelected(item)
    }


    fun setCardListener () {

        MyApp.dataBase.setCardDao().allSets
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
            setCards ->

                    setCardsArrayList.addAll(setCards)
                    Log.d(TAG, "the size of list is: " + setCardsArrayList.size)

                    setCardAdapter = SetAdapter(setCardsArrayList)
                    set_recycler_view.adapter = setCardAdapter
                    setCardAdapter.notifyDataSetChanged()
        }

    }

    fun addSet() {

        Single.fromCallable {
            val num = Random().nextInt()
            val setCard = SetCard("Bubbas" + num, "Kotlin", "Us or them", "https:dkdk")
            MyApp.dataBase.setCardDao().insert(setCard)
        }
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                        onSuccess = { setcard ->
                            Log.d(TAG, "SetCard added.")
                        },
                        onError = {error ->
                            Log.e(TAG, "Couldn't write SetCard to database", error)
                        }
                )
    }



    class SetHolder (v :View) :RecyclerView.ViewHolder(v) {
        private var setName : TextView
        private var setDescription: TextView

        init {
            setName = v.findViewById<TextView>(R.id.set_name) as TextView
            setDescription = v.findViewById<TextView>(R.id.set_description) as TextView

        }

        fun bind (set: SetCard) {
            setName.setText(set.description)
            setDescription.setText(set.name)

        }
    }

    class SetAdapter ( var setCard : ArrayList<SetCard>?): RecyclerView.Adapter<SetHolder> () {
        final private val TAG = "SetAdapter"
        override fun onBindViewHolder(holder: SetHolder, position: Int) {
            holder.bind(setCard!![position])
        }


        override fun getItemCount(): Int {
            Log.d(TAG, "the size of setCard is: " + setCard!!.size)
            return setCard!!.size
        }


        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SetHolder {
            var layoutInflator = LayoutInflater.from(parent!!.context)
            return SetHolder(layoutInflator.inflate(R.layout.recyclerview_set_row, parent, false))

        }
    }

}