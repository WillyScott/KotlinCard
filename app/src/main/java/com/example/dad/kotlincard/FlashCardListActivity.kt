package com.example.dad.kotlincard

import android.support.v4.app.Fragment
import android.util.Log

class FlashCardListActivity : SingleFragmentActivity() {
    private final val TAG = "FlashCardListActivity"


    override fun createFragment(): Fragment {
        Log.d(TAG, "createFragment()")
        return FlashCardFragment.newInstance()
    }


}
