package com.example.dad.kotlincard.setcard

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.CheckBox
import android.widget.EditText
import com.example.dad.kotlincard.R
import com.example.dad.kotlincard.db.MyApp
import com.example.dad.kotlincard.db.SetCard
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.operators.single.SingleFromCallable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 * Created by Dad on 12/5/2017.
 */
class EditSetCardFragment:Fragment() {

    private final var TAG = "EditSetCardFragment"
    private var set_card_id = -1
    private lateinit var setCard:SetCard
    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var sectionEditText: EditText
    private lateinit var urlEditText: EditText
    private lateinit var randomizeCheckBox: CheckBox
    private lateinit var reverseCheckBox: CheckBox
    private lateinit var setcardfromDatabase:SetCard

    companion object {
        private final val SET_ID = "set_id"
        fun newInstance(uid:Int):Fragment{
            var arg =Bundle()
            arg.putInt(SET_ID,uid)
            var fragment = EditSetCardFragment()
            fragment.arguments = arg
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //get the intent extra
        set_card_id = arguments.getInt(SET_ID)
        Log.d(TAG,"The set id is: " + SET_ID)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater!!.inflate(R.layout.fragment_setcard,container,false)

        // set fragment views
        nameEditText = view.findViewById<EditText>(R.id.set_name)
        descriptionEditText = view.findViewById<EditText>(R.id.set_description)
        sectionEditText = view.findViewById<EditText>(R.id.set_section)
        urlEditText = view.findViewById<EditText>(R.id.set_import_url)
        randomizeCheckBox = view.findViewById<CheckBox>(R.id.set_checkbox_randomize)
        reverseCheckBox = view.findViewById<CheckBox>(R.id.set_checkbox_reverse)

        //menu option
        setHasOptionsMenu(true)
        //get the selected setcard from database
        updateVew()

        return view
    }

    fun updateVew() {
        SingleFromCallable {
            MyApp.dataBase.setCardDao().getSet(set_card_id)
        }       .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { setcard ->
                            //update the view
                            setcardfromDatabase = setcard
                            nameEditText.setText(setcardfromDatabase.name)
                            descriptionEditText.setText(setcardfromDatabase.description)
                            sectionEditText.setText(setcardfromDatabase.section)
                            urlEditText.setText(setcardfromDatabase.urlString)
                            //If boolean is null set it to true
                            randomizeCheckBox.setChecked(setcardfromDatabase.randomize ?: true)
                            reverseCheckBox.setChecked(setcardfromDatabase.reverse ?: true)


                        },
                        onError = { error ->
                            Log.e(TAG, "Couldn't read SetCard from database: " + error)
                        }
                )
    }



   // fun saveEditSaveCard
    fun saveSetCard() {
       SingleFromCallable {
           //update the flashcard from database to current values
           setcardfromDatabase.name = nameEditText.text.toString()
           setcardfromDatabase.description = descriptionEditText.text.toString()
           setcardfromDatabase.section = sectionEditText.text.toString()
           setcardfromDatabase.urlString = urlEditText.text.toString()
           setcardfromDatabase.randomize = randomizeCheckBox.isChecked()
           setcardfromDatabase.reverse = reverseCheckBox.isChecked()


           // var flashcard = FlashCard(frontTextView.text.toString(), backTextView.text.toString(), flash_card_id, showCheckBox.isChecked)
           MyApp.dataBase.setCardDao().update(setcardfromDatabase)
       }       .subscribeOn(Schedulers.io())
               .subscribeBy (
                       onSuccess = {intCode ->
                           Log.d(TAG, "Wrote setcard to database: " + intCode)
                       },
                       onError = { error ->
                           Log.e(TAG, "Could not update the setcard: "+ error)
                       }
               )
   }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.save) {
            Log.d(TAG, "Save selected")
            saveSetCard()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.fragment_flashcard,menu)
    }


}






