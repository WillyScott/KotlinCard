package com.example.dad.kotlincard.setcard

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.example.dad.kotlincard.SingleFragmentActivity

/**
 * Created by Dad on 11/27/2017.
 */
class SetCardListActivity: SingleFragmentActivity()  {

    companion object {
        fun newIntent(context: Context):Intent{
            var intent = Intent(context,SetCardListActivity::class.java)
            return intent
        }
    }

    override fun createFragment(): Fragment {
        return SetCardFragment.newInstance()
    }

}