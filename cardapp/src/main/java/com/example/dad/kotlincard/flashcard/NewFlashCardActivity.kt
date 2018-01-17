package com.example.dad.kotlincard.flashcard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.dad.kotlincard.SingleFragmentActivity

/**
 * Created by Dad on 12/7/2017.
 */
class NewFlashCardActivity:SingleFragmentActivity() {
    final private var TAG = "NewFlashCardActivity"

    companion object {
        private final val SET_CARD_ID = "com.example.dad.kotlin.flashcard"

        fun newIntent(context: Context,uid : Int):Intent {
            var intent = Intent(context, NewFlashCardActivity::class.java)
            intent.putExtra(SET_CARD_ID,uid)
            return intent
        }
    }

    override fun createFragment(): Fragment {
        val setCard_id = intent.getIntExtra(SET_CARD_ID,-1)
        var fragment = NewFlashCardFragment.newInstance(setCard_id)
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("New flash card")
    }
}