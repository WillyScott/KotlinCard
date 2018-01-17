package com.example.dad.kotlincard.startcards

import android.animation.*
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.FrameLayout
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
    private var reverseFrontBack = false
    private var frontVisible = true
    private lateinit var frontbackCard:TextView
    private lateinit var page:TextView
    private lateinit var knowCard:TextView
    private lateinit var currentFlashCard: FlashCard
    private lateinit var frameContainer:FrameLayout
    private lateinit var setRightOut:AnimatorSet
    private lateinit var setLeftIn:AnimatorSet
    private lateinit var setLeftOut:AnimatorSet
    private lateinit var setRightin:AnimatorSet

    companion object {
        private final var ARG_CARD_ID = "setcard_id"
        private final var ARG_POSITION = "postion"
        private final var ARG_COUNT = "count"
        private final var ARG_REVERSE = "reverse"
        fun newInstance(uid: Int, pos : Int, count: Int, reverse: Boolean):StartCardsFragment{
            val args = Bundle()
            args.putInt(ARG_CARD_ID,uid)
            args.putInt(ARG_POSITION,pos)
            args.putInt(ARG_COUNT,count)
            args.putBoolean(ARG_REVERSE, reverse)
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
        Log.d(TAG, "card_id: " + card_id)
        Log.d(TAG, "position: "+ position)
        Log.d(TAG, "count: " + count)
        reverseFrontBack = arguments.getBoolean(ARG_REVERSE)

      //  Log.d(TAG, "The flashcard ID is: " + card_id)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater!!.inflate(R.layout.fragment_start_cards,container,false)
        frontbackCard = view.findViewById<TextView>(R.id.frontbackView) as TextView
        page = view.findViewById<TextView>(R.id.pagenum) as TextView
        knowCard = view.findViewById<TextView>(R.id.knowit) as TextView
        frameContainer = view.findViewById<FrameLayout>(R.id.container) as FrameLayout

        // Single click whick will call animation and flip card to the back
        //Swipe up which will mark the card as "Know it!".

        view.setOnTouchListener(object : OnSwipeTouchListener(activity) {
            override fun onClick() {
                super.onClick()
                Log.d(TAG,"single click")
                //switch to the other side of card and animate the change
                onStartAnimationtoBack()
            }

            override fun onSwipeUp() {
                super.onSwipeUp()
                Log.d(TAG, "view swipe up")
                //set form and and show field to false

                currentFlashCard.show = false
                saveFlashCard(currentFlashCard)
                knowCard.setText("Know it")
            }

            override fun onSwipeDown() {
                super.onSwipeDown()
                currentFlashCard.show = true
                saveFlashCard(currentFlashCard)
                knowCard.setText("")

            }
        })


        // query database for flashcard
       Single.fromCallable {
           MyApp.dataBase.flashCardDao().get(card_id)
       }       .subscribeOn(Schedulers.io())
               .subscribeBy(
                       onSuccess = { flashcard ->
                           currentFlashCard = flashcard
                           if (flashcard.show == true ) {
                               knowCard.setText("")

                           }else {
                               knowCard.setText("Know it")
                           }
                           //Reverse the front and back is reverse field is set to true.

                           // Reverse the cards if the reverse field is set to true
                           if (reverseFrontBack){
                               frontbackCard.setText(currentFlashCard.backcard)
                           } else {
                               frontbackCard.setText(currentFlashCard.frontcard)
                           }

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
        Log.d(TAG, "onStartAnimationtoBack")
//        val animatorSet = AnimatorSet()
//        val animation = ValueAnimator.ofFloat()
//        frameContainer.animate().rotationY(90f).setDuration(1000L).start()
//        frontbackCard.setText("back card")
       // frontbackCard.animate().rotationY(-90f).setDuration(1000L).start()
        loadAnimations()
        changeCameraDistance()

        setLeftIn.setTarget(frameContainer)
        setLeftOut.setTarget(frameContainer)


        if (frontVisible) {
            frontVisible = false
//            setRightOut.setTarget(frameContainer)
//            setLeftIn.setTarget(frameContainer)

//            setRightOut.start()
//            setLeftIn.start()
            setLeftOut.start()
            setLeftIn.start()

            //change background color so user knows its the back of the card
            frontbackCard.setBackgroundColor(resources.getColor(R.color.grey))
            page.setBackgroundColor(resources.getColor(R.color.grey))
            knowCard.setBackgroundColor(resources.getColor(R.color.grey))
            // Reverse the cards if reverse field is set to true

            //frontbackCard.setText(currentFlashCard.backcard)

            // Reverse the cards if the reverse field is set to true
            if (reverseFrontBack){
                frontbackCard.setText(currentFlashCard.frontcard)
            } else {
                frontbackCard.setText(currentFlashCard.backcard)
            }

        }else {
            frontVisible = true
//            setRightOut.setTarget(frameContainer)
//            setLeftIn.setTarget(frameContainer)

//            setRightOut.start()
//            setLeftIn.start()
            setRightOut.setTarget(frameContainer)
            setRightin.setTarget(frameContainer)

            setRightin.start()
            setRightOut.start()


            frontbackCard.setBackgroundColor(resources.getColor(R.color.green))
            page.setBackgroundColor(resources.getColor(R.color.green))
            knowCard.setBackgroundColor(resources.getColor(R.color.green))

            //frontbackCard.setText(currentFlashCard.frontcard)

            // Reverse the cards if the reverse field is set to true
            if (reverseFrontBack){
                frontbackCard.setText(currentFlashCard.backcard)
            } else {
                frontbackCard.setText(currentFlashCard.frontcard)
            }
        }
    }

    fun loadAnimations() {

//        setRightOut = AnimatorInflater.loadAnimator(context,R.animator.out_animation) as AnimatorSet
//        setLeftIn = AnimatorInflater.loadAnimator(context,R.animator.in_animation) as AnimatorSet

        setLeftIn = AnimatorInflater.loadAnimator(context,R.animator.card_flip_left_in) as AnimatorSet
        setLeftOut = AnimatorInflater.loadAnimator(context,R.animator.card_flip_left_out) as AnimatorSet

        setRightOut = AnimatorInflater.loadAnimator(context,R.animator.card_flip_right_in) as AnimatorSet
        setRightin = AnimatorInflater.loadAnimator(context,R.animator.card_flip_right_out) as AnimatorSet



    }

    fun changeCameraDistance() {
        val distance:Int = 8000;
        var scale:Float = getResources().getDisplayMetrics().density * distance;
       frameContainer.setCameraDistance(scale);

    }


    //TODO: finish
    // Sets the camera distance back to hid something?

       fun saveFlashCard(flashCard: FlashCard) {
        SingleFromCallable {
            //update the flashcard from database to current values, the only value changing is the "Know-it" or  show field on flashcards.

            // var flashcard = flashcard(frontTextView.text.toString(), backTextView.text.toString(), flash_card_id, showCheckBox.isChecked)
            MyApp.dataBase.flashCardDao().update(flashCard)
        }       .subscribeOn(Schedulers.io())
                .subscribeBy (
                        onSuccess = {intCode ->
                            Log.d(TAG, "Wrote flashcard to database: " + intCode)
                        },
                        onError = { error ->
                            Log.e(TAG, "Could not update the flashcard: "+ error)
                        }
                )
    }
}