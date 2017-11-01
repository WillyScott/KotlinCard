package com.example.dad.kotlincard

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment

class KotlinListActivity : SingleFragmentActivity() {

    override fun creatFragment(): Fragment {
        return CardFragment.newInstance()
    }

//    override  protected fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_container)
//
//        val fm = supportFragmentManager
//        var fragment = fm.findFragmentById(R.id.fragment_container)
//
//        if (fragment == null) {
//            fragment = CardFragment.newInstance()
//            supportFragmentManager
//                    .beginTransaction()
//                    .add(R.id.fragment_container,fragment)
//                    .commit()
//        }
//    }
}
