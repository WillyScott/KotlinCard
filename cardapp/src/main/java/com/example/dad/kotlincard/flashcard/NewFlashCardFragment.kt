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
import io.reactivex.internal.operators.single.SingleFromCallable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 * Created by Dad on 12/7/2017.
 */
class NewFlashCardFragment:Fragment() {

    private final val TAG = "NewFlashCardFragment"
    private var saveSelected = false
    private lateinit var frontTextView:TextView
    private lateinit var backTextView:TextView
    private lateinit var showCheckBox:CheckBox
    private var set_ID:Int = -1


    companion object {
        private final val SETCARD_ID = "setcard_id"
        fun newInstance(uid: Int): NewFlashCardFragment {
            var args:Bundle = Bundle()
            args.putInt(SETCARD_ID,uid)
            var fragment = NewFlashCardFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Get the set id
        set_ID = arguments.getInt(SETCARD_ID)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater!!.inflate(R.layout.fragment_flashcard,container, false)
        // initialize the form object to be able to save latter
        frontTextView = view.findViewById<TextView>(R.id.frontflashcard)
        backTextView = view.findViewById<TextView>(R.id.backflashcard)
        showCheckBox = view.findViewById<CheckBox>(R.id.showcardcheckbox)


        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.fragment_flashcard,menu)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.save) {
            Log.d(TAG, "Save toolbar item selected: " + saveSelected)
            if (saveSelected == false) {
                activity.invalidateOptionsMenu()
                saveNewFlashCard()
                saveSelected = true
                return true

            }else {
                return true
            }

            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        if (saveSelected == true){
            menu!!.clear()
        }
    }

    fun saveNewFlashCard() {
        Log.d(TAG, "saveNewFlashCard()")
        // Save the new flashcard

        SingleFromCallable{
            val flashCard = FlashCard(frontTextView.text.toString(), backTextView.text.toString(), set_ID, showCheckBox.isChecked)
            MyApp.dataBase.flashCardDao().insert(flashCard)
        }       .subscribeOn(Schedulers.io())
                .subscribeBy(
                        onSuccess = { intCode ->
                            Log.d(TAG, "flashcard added: " + intCode)
                        },
                        onError = {error ->
                            Log.e(TAG, "Couldn't write flashcard to database", error)
                        }
                )

    }


}