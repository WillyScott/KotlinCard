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
import io.reactivex.schedulers.Schedulers

/**
 * Created by Dad on 12/7/2017.
 */
class StartCardsActivity:AppCompatActivity() {
    private final var TAG = "StartCardsActivity"
    private lateinit var flashcardsStart :ArrayList<FlashCard>
    private lateinit var viewPager:ViewPager
    private lateinit var pageAdaper:FlashCardPageAdapter



    companion object {
        private final val EXTRA_SETCARD_ID = "com.example.dad.kotlincard"
        fun newIntent(context: Context,uid:Int): Intent {
            val intent = Intent(context,StartCardsActivity::class.java)
            intent.putExtra(EXTRA_SETCARD_ID, uid)
            return intent
        }
    }
//
//    override fun createFragment(): Fragment {
//        //Get the Intent extra
//        val setCard_ID = intent.getIntExtra(StartCardsActivity.EXTRA_SETCARD_ID,-1)
//        return StartCardsFragment.newInstance(setCard_ID)
//    }



    override  protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"OnCreate")
        setContentView(R.layout.activity_start_cards)
        val setCard_ID = intent.getIntExtra(StartCardsActivity.EXTRA_SETCARD_ID,-1)
        setTitle("StartCards")
        flashcardsStart = ArrayList<FlashCard>()

        viewPager = findViewById<ViewPager>(R.id.start_cards_view_pager)
        val fm = supportFragmentManager

        //query the database for the flashcards from the set

                MyApp.dataBase.flashCardDao().getAll(setCard_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    flashCards ->
                    flashcardsStart.clear()
                    flashcardsStart.addAll(flashCards)
                    Log.d(TAG, "Query run againThe count of flashcards is: " + flashcardsStart.size)
                    pageAdaper = FlashCardPageAdapter(fm,flashcardsStart)
                    viewPager.adapter = pageAdaper
                }
    }

    internal inner class FlashCardPageAdapter( fragmentManager: FragmentManager, flashcards: ArrayList<FlashCard>):FragmentStatePagerAdapter(fragmentManager) {


        override fun getCount(): Int {
            return flashcardsStart.size
        }

        override fun getItem(position: Int): Fragment {
            return StartCardsFragment.newInstance(flashcardsStart[position].uid, position, count)
        }

    }
}