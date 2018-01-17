package com.williamgromme.kotlincard.setcard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.williamgromme.kotlincard.miss.SingleFragmentActivity

/**
 * Created by Dad on 12/5/2017.
 */
class EditSetCardActivity: SingleFragmentActivity() {
    //private lateinit var set_Card_ID:Int


    companion object {
        private final val TAG = "com.williamgromme.kotlincard.set_card_id"
        fun newIntent(context: Context, uid: Int):Intent {
            var intent = Intent(context, EditSetCardActivity::class.java)
            intent.putExtra(TAG, uid)
            return intent
        }
    }

    override fun createFragment(): Fragment {
        var set_Card_ID = intent.getIntExtra(TAG, -1)
        return EditSetCardFragment.newInstance(set_Card_ID)
        //get the set_card

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("Edit Set")
    }
}
