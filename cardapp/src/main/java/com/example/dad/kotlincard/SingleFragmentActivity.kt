package com.example.dad.kotlincard

import android.app.Fragment
import android.app.FragmentManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by Dad on 10/22/2017.
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