package com.williamgromme.kotlincard.setcard

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.williamgromme.kotlincard.R

/**
 * Created by Dad on 1/3/2018.
 *
 * from BigNerdRanch modified
 */
class SplashActivity:AppCompatActivity() {
    private final val TAG = "SplashActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_container)
        var intent = SetCardListActivity.newIntent(baseContext)
        startActivity(intent)
        finish()
        Log.d(TAG, "onCreate")
//        var intent = Intent(this,SetCardListActivity::class.java )
//        startActivity(intent)
//        Log.d(TAG, "onCreate")
//        finish()
    }
}