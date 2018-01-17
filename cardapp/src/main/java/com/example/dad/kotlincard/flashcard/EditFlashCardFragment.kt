package com.example.dad.kotlincard.flashcard

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.CheckBox
import android.widget.TextView
import com.example.dad.kotlincard.R
import com.example.dad.kotlincard.db.FlashCard
import com.example.dad.kotlincard.db.MyApp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.operators.single.SingleFromCallable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 * Created by Dad on 12/5/2017.
 */
class EditFlashCardFragment:Fragment() {

    private var flash_card_id = -1
    private final val TAG = "EditFlashCardFragment"
    private lateinit var frontTextView: TextView
    private lateinit var backTextView: TextView
    private lateinit var showCheckBox: CheckBox
    private var set_ID: Int = -1
    private lateinit var flashcardfromDatabase:FlashCard


    companion object {
        private final val FLASH_CARD_ID = "flash_card_id"
        fun newInstance(uid: Int): Fragment {
            var args = Bundle()
            args.putInt(FLASH_CARD_ID, uid)
            var fragment = EditFlashCardFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // get the argument

        //flash_card_id = arguments.getInt(FLASH_CARD_ID,-1)
        flash_card_id = arguments.getInt(FLASH_CARD_ID)
        Log.d(TAG, "The flashcard id is: " + flash_card_id)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater!!.inflate(R.layout.fragment_flashcard, container, false)
        frontTextView = view.findViewById<TextView>(R.id.frontflashcard)
        backTextView = view.findViewById<TextView>(R.id.backflashcard)
        showCheckBox = view.findViewById<CheckBox>(R.id.showcardcheckbox)

        // Activate menu for fragment
        setHasOptionsMenu(true)
        updateVew()
        return view
    }

    fun updateVew() {
        SingleFromCallable {
            MyApp.dataBase.flashCardDao().get(flash_card_id)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { flashcard ->
                            //update the view
                            flashcardfromDatabase = flashcard
                            frontTextView.setText(flashcard.frontcard)
                            backTextView.setText(flashcard.backcard)
                            //Log.d(TAG, "the value of the store checkbox is: " + flashcard.show)

                            showCheckBox.setChecked(flashcard.show ?: true )
                           // showCheckBox.setChecked(true)

                        },
                        onError = { error ->
                            Log.e(TAG, "Couldn't read flashcard from database: " + error)
                        }
                )
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.fragment_flashcard,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.save) {
            Log.d(TAG, "Save selected")
            saveFlashCard()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun saveFlashCard() {
        SingleFromCallable {
            //update the flashcard from database to current values
            flashcardfromDatabase.backcard = backTextView.text.toString()
            flashcardfromDatabase.frontcard = frontTextView.text.toString()
            flashcardfromDatabase.show = showCheckBox.isChecked
           // var flashcard = flashcard(frontTextView.text.toString(), backTextView.text.toString(), flash_card_id, showCheckBox.isChecked)
            MyApp.dataBase.flashCardDao().update(flashcardfromDatabase)
        }       .subscribeOn(Schedulers.io())
                .subscribeBy (
                        onSuccess = {intCode ->
                            Log.d(TAG, "Wrote flashcard to database: " + intCode)
                        },
                        onError = { error ->
                            Log.e(TAG, "Could not update the flashcard: "+ error)
                        }
                )
    }
}

