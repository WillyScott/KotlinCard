package com.example.dad.kotlincard.setcard

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.dad.kotlincard.R
import com.example.dad.kotlincard.setcard.SetCardListActivity
import kotlinx.android.synthetic.main.fragment_container.view.*

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