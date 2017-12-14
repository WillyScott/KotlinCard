package com.example.dad.kotlincard.startcards

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.TextView
import com.example.dad.kotlincard.R
import com.example.dad.kotlincard.SingleFragmentActivity
import com.example.dad.kotlincard.db.FlashCard
import com.example.dad.kotlincard.db.MyApp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.Single
import io.reactivex.internal.operators.single.SingleFromCallable
import io.reactivex.rxkotlin.subscribeBy
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by Dad on 12/7/2017.
 */
class StartCardsFragment:Fragment() {
    private final var TAG = "StartCardsFragment"
    private var card_id: Int = -1
    private var position: Int = -1
    private var count: Int = -1
    private lateinit var frontbackCard:TextView
    private lateinit var page:TextView
    private lateinit var knowCard:TextView
    private lateinit var currentFlashCard: FlashCard


    companion object {
        private final var ARG_CARD_ID = "setcard_id"
        private final var ARG_POSITION = "postion"
        private final var ARG_COUNT = "count"
        fun newInstance(uid: Int, pos : Int, count: Int):StartCardsFragment{
            val args = Bundle()
            args.putInt(ARG_CARD_ID,uid)
            args.putInt(ARG_POSITION,pos)
            args.putInt(ARG_COUNT,count)
            val fragment = StartCardsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        card_id = arguments.getInt(ARG_CARD_ID)
        position = arguments.getInt(ARG_POSITION)
        count = arguments.getInt(ARG_COUNT)

        Log.d(TAG, "The flashcard ID is: " + card_id)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater!!.inflate(R.layout.fragment_start_cards,container,false)
        frontbackCard = view.findViewById<TextView>(R.id.frontbackView) as TextView
        page = view.findViewById<TextView>(R.id.pagenum) as TextView
        knowCard = view.findViewById<TextView>(R.id.knowit) as TextView



        // Single click whick will call animation and flip card to the back
        //Swipe up which will mark the card as "Know it!".

        view.setOnTouchListener(object : OnSwipeTouchListener(activity) {
            override fun onClick() {
                super.onClick()
                Log.d(TAG,"view single click")
                onStartAnimationtoBack()
            }

            override fun onSwipeUp() {
                super.onSwipeUp()
                Log.d(TAG, "view swipe up")
            }
        })


        // query database for FlashCard
       Single.fromCallable {
           MyApp.dataBase.flashCardDao().get(card_id)
       }       .subscribeOn(Schedulers.io())
               .subscribeBy(
                       onSuccess = { flashcard ->
//                           currentFlashCard = flashcard
//                           if (flashcard.show == true ) {
//                              // knowCard.setText("")
//
//                           }else {
//                              // knowCard.setText("Know it")
//                           }

                           frontbackCard.setText(flashcard.frontcard)
                           page.setText((position +1).toString() + " of " + count.toString())
                       },
                       onError = {error ->
                            Log.e(TAG, "Couldn't get the flashcard: ",error)
                       }
               )

       return view
    }

    fun onStartAnimationtoBack () {
        //frontbackCard.setText(currentFlashCard.backcard)
    }

    fun onStartAnimationtoFront() {

    }

}