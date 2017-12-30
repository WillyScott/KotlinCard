package com.example.dad.kotlincard.FlashCard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.dad.kotlincard.SingleFragmentActivity

/**
 * Created by Dad on 12/5/2017.
 */
class EditFlashCardActivity: SingleFragmentActivity() {

    final private val TAG = "EditFlashCardActivity"

    companion object {
        private final var FLASH_CARD_ID = "com.example.dad.kotlincard.flash_card_id"
        fun newIntent(context: Context, uid: Int): Intent {
            var intent =  Intent(context, EditFlashCardActivity::class.java)
            intent.putExtra(FLASH_CARD_ID, uid)
            return intent
        }
    }

    override fun createFragment(): Fragment {
        var flash_card_id = intent.getIntExtra(FLASH_CARD_ID,-1)
        return EditFlashCardFragment.newInstance(flash_card_id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("Edit Flashcard")
    }
}