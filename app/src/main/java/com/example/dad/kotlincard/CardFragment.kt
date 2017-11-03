package com.example.dad.kotlincard

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_container.*
import kotlinx.android.synthetic.main.fragment_card_list.*
import kotlinx.android.synthetic.main.recyclerview_item_row.*


/**
 * Created by Dad on 10/22/2017.
 */
class CardFragment:Fragment() {

    private  lateinit var  cardArrayList: ArrayList<Card>
    private final val TAG = "CardFragment"
    private lateinit var cardRecyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter
    //private lateinit var linearLayoutManager: LinearLayoutManager


    companion object {
        fun newInstance():CardFragment {
            return CardFragment()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        cardArrayList = ArrayList<Card>()

        for (i in 0 .. 99){
            var card = Card("front" + i,"back" + i)
            cardArrayList.add(card)
        }

      //  Log.i(TAG,"number add" +  cardArrayList?.size  )
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       // return super.onCreateView(inflater, container, savedInstanceState)

        var view = inflater!!.inflate(R.layout.fragment_card_list, container, false)


        cardRecyclerView = view.findViewById<RecyclerView>(R.id.card_recycler_view) as RecyclerView
        val activity = activity
       // linearLayoutManager = LinearLayoutManager(context)
        cardRecyclerView.layoutManager = LinearLayoutManager(activity)
        //card_recycler_view.layoutManager = LinearLayoutManager(activity)

        updateUI()
        return view
    }

    fun updateUI()
    {
        cardAdapter = CardAdapter(cardArrayList,context)
        cardRecyclerView.adapter = cardAdapter
    }
    internal inner class CardHolder (v: View) :RecyclerView.ViewHolder(v) {
//        private var cardBack: TextView
//        private var cardFront: TextView

        init {
//            cardFront = itemView.findViewById<TextView>(R.id.cardfront) as TextView
//            cardBack = itemView.findViewById<TextView>(R.id.cardback) as TextView
        }

        fun bind(card: Card) {
//            cardBack.setText(card.back)
//            cardFront.setText(card.front)
            cardfront.setText(card.back)
            cardback.setText(card.front)

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
           return cards!!.size
        }

    }





}