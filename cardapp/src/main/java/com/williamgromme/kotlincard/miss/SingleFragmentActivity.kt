package com.williamgromme.kotlincard.miss

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.williamgromme.kotlincard.R

/**
 * Created by Dad on 10/22/2017.
 * BNR
 */
abstract class  SingleFragmentActivity: AppCompatActivity() {

    abstract protected fun createFragment():android.support.v4.app.Fragment

     override  protected fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.fragment_container)

         val fm = supportFragmentManager
         var fragment = fm.findFragmentById(R.id.fragment_container)

         if (fragment == null) {
             fragment = createFragment()
             supportFragmentManager
                     .beginTransaction()
                     .add(R.id.fragment_container,fragment)
                     .commit()
         }
    }


}