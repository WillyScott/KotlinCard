package com.example.dad.kotlincard.startcards

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.dad.kotlincard.R
import com.example.dad.kotlincard.SingleFragmentActivity
import com.example.dad.kotlincard.db.FlashCard
import com.example.dad.kotlincard.db.MyApp
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.operators.single.SingleFromCallable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 * Created by Dad on 12/7/2017.
 */
class StartCardsActivity:AppCompatActivity() {
    private final var TAG = "StartCardsActivity"
    private lateinit var flashcardsStart :ArrayList<FlashCard>
    private lateinit var viewPager:ViewPager
    private lateinit var pageAdaper:FlashCardPageAdapter
    private var setCard_reverse = false



    companion object {
        private final val EXTRA_SETCARD_ID = "com.example.dad.kotlincard"
        private final val EXTRA_SETCARD_RANDOMIZE = "com.example.dad.kotlincard.randomize"
        private final val EXTRA_SETCARD_REVERSE = "com.example.dad.kotlincard.reverse"
        //TODO add randomize, reverse
        fun newIntent(context: Context,uid:Int, randomize:Boolean, reverse:Boolean): Intent {
            val intent = Intent(context,StartCardsActivity::class.java)
            intent.putExtra(EXTRA_SETCARD_ID, uid)
            intent.putExtra(EXTRA_SETCARD_RANDOMIZE, randomize)
            intent.putExtra(EXTRA_SETCARD_REVERSE, reverse)
            return intent
        }
    }

    override  protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"OnCreate")
        setContentView(R.layout.activity_start_cards)
        //Get the Intent extras
        val setCard_ID = intent.getIntExtra(StartCardsActivity.EXTRA_SETCARD_ID,-1)
        val setCard_randomize = intent.getBooleanExtra(StartCardsActivity.EXTRA_SETCARD_RANDOMIZE, false)
        setCard_reverse = intent.getBooleanExtra(EXTRA_SETCARD_REVERSE,false)
        setTitle("Flash cards")
        flashcardsStart = ArrayList<FlashCard>()

        viewPager = findViewById<ViewPager>(R.id.start_cards_view_pager)
        val fm = supportFragmentManager

        //query the database for the flashcards from the set


        SingleFromCallable{
            if (setCard_randomize) {
                MyApp.dataBase.flashCardDao().getAllStartRandom(setCard_ID)
            } else {
                MyApp.dataBase.flashCardDao().getAllStart(setCard_ID)
            }
        }.subscribeOn(Schedulers.io())
                .subscribeBy(
                        onSuccess = { flashcards ->
                            Log.d(TAG, "Flashcard found.")
                    flashcardsStart.clear()
                    flashcardsStart.addAll(flashcards)
                    Log.d(TAG, "Query run againThe count of flashcards is: " + flashcardsStart.size)
                    pageAdaper = FlashCardPageAdapter(fm,flashcardsStart)
                    viewPager.adapter = pageAdaper
                        },
                        onError = {error ->
                            Log.e(TAG, "Flashcards not found.", error)
                        }
                )
    }

    internal inner class FlashCardPageAdapter( fragmentManager: FragmentManager, flashcards: ArrayList<FlashCard>):FragmentStatePagerAdapter(fragmentManager) {
        override fun getCount(): Int {
            return flashcardsStart.size
        }

        override fun getItem(position: Int): Fragment {
            return StartCardsFragment.newInstance(flashcardsStart[position].uid, position, count, setCard_reverse)
        }
    }
}