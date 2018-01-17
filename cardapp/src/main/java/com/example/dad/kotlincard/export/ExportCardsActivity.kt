package com.example.dad.kotlincard.export

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.dad.kotlincard.SingleFragmentActivity

/**
 * Created by Dad on 12/26/2017.
 */
class ExportCardsActivity :SingleFragmentActivity() {

    companion object {
        private final val SET_ID = "set_id"
        fun newIntent (context:Context, uid:Int): Intent {
            var intent = Intent(context,ExportCardsActivity::class.java)
            intent.putExtra(SET_ID,uid)
            return intent
         }
     }

    override fun createFragment(): Fragment {
        var set_id = intent.getIntExtra(SET_ID,-1)
        var fragment = ExportCardsFragment.newInstance(set_id)
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("Export SetCard Data")
    }
}