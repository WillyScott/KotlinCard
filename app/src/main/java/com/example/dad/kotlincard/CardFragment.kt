package com.example.dad.kotlincard

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * Created by Dad on 10/22/2017.
 */
class CardFragment:Fragment() {

private lateinit var  cardRecyclerView:RecyclerView

    companion object {
        fun newInstance():CardFragment {
            return CardFragment()
        }
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       // return super.onCreateView(inflater, container, savedInstanceState)

        var view = inflater!!.inflate(R.layout.fragment_card_list, container, false)

        //cardRecyclerView = find<RecyclerView>(R.id.recyclerView)

        cardRecyclerView = view.findViewById<RecyclerView>(R.id.card_recycler_view) as RecyclerView
        val activity = activity
        cardRecyclerView.layoutManager = LinearLayoutManager(activity);
        return view
    }

    private class CardHolder (v: View) :RecyclerView.ViewHolder(v) {



    }



}