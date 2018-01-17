package com.williamgromme.kotlincard.setcard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.williamgromme.kotlincard.miss.SingleFragmentActivity

/**
 * Created by Dad on 12/7/2017.
 */
class NewSetCardActivity: SingleFragmentActivity() {

     companion object {
         fun newIntent(context: Context):Intent{
             var intent = Intent(context,NewSetCardActivity::class.java)
             return intent
         }
     }

    override fun createFragment(): Fragment {
        var fragment = NewSetCardFragment()
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("New SetCard")
    }

}