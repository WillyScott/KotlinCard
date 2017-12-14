package com.example.dad.kotlincard.FlashCard

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.util.Log
import com.example.dad.kotlincard.SingleFragmentActivity

class FlashCardListActivity : SingleFragmentActivity() {
    private final val TAG = "FlashCardListActivity"
   // public final var SET_CARD_ID = "com.example.dad.kotlincard.set_card_name"

    companion object {
        private final var SET_CARD_ID = "com.example.dad.kotlincard.set_card_name"
        fun newIntent(context:Context, uid: Int ):Intent {
            var intent = Intent(context, FlashCardListActivity::class.java)
            intent.putExtra(FlashCardListActivity.SET_CARD_ID,uid)
            return intent
        }
    }

    override fun createFragment(): Fragment {
        Log.d(TAG, "createFragment()")
        var setCard_id = intent.getIntExtra(SET_CARD_ID,-1)
        return FlashCardFragment.newInstance(setCard_id)
    }

}
