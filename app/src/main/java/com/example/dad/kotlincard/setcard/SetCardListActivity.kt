package com.example.dad.kotlincard.setcard

import android.support.v4.app.Fragment
import com.example.dad.kotlincard.SingleFragmentActivity

/**
 * Created by Dad on 11/27/2017.
 */
class SetCardListActivity: SingleFragmentActivity()  {
    override fun createFragment(): Fragment {
        //var setCard_id:Int = intent.getIntExtra()
        return SetCardFragment.newInstance()
    }

}