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
import io.reactivex.internal.operators.single.SingleFromCallable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 * Created by Dad on 12/7/2017.
 */
class NewSetCardFragment:Fragment (){

    private final val TAG = "NewSetCardFragment"
    private var setcard_id:Int = -1
    private var saveSelected = false
    private lateinit var nameEditText:EditText
    private lateinit var descriptionEditText:EditText
    private lateinit var sectionEditText:EditText
    private lateinit var urlEditText:EditText
    private lateinit var randomizeCheckBox:CheckBox
    private lateinit var reverseCheckBox:CheckBox



    companion object {
        private final val SET_ID = "set_id"
        fun newInstance(uid:Int): NewSetCardFragment {
//            var args = Bundle()
//            args.putInt(SET_ID,uid)
            var fragment = NewSetCardFragment()
//            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setcard_id = arguments.getInt(SET_ID,-1)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // inflate menu why false attachToRoot?
        var view = inflater!!.inflate(R.layout.fragment_setcard,container,false)
        //inflate views
        nameEditText = view.findViewById<EditText>(R.id.set_name)
        descriptionEditText = view.findViewById<EditText>(R.id.set_description)
        sectionEditText = view.findViewById<EditText>(R.id.set_section)
        urlEditText = view.findViewById<EditText>(R.id.set_import_url)
        randomizeCheckBox = view.findViewById<CheckBox>(R.id.set_checkbox_randomize)
        reverseCheckBox = view.findViewById<CheckBox>(R.id.set_checkbox_reverse)

        //menu option
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
                saveNewSetCard()
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
//
    fun saveNewSetCard() {
        Log.d(TAG, "saveNewFlashCard()")
        // Save the new flashcard

        SingleFromCallable{
            val setcard = SetCard(nameEditText.text.toString(),sectionEditText.text.toString(), descriptionEditText.text.toString(),
                    urlEditText.text.toString(), randomizeCheckBox.isChecked, reverseCheckBox.isChecked )
            //val flashCard = FlashCard(frontTextView.text.toString(), backTextView.text.toString(), set_ID, showCheckBox.isChecked)
            MyApp.dataBase.setCardDao().insert(setcard)
        }       .subscribeOn(Schedulers.io())
                .subscribeBy(
                        onSuccess = { intCode ->
                            Log.d(TAG, "SetCard added: " + intCode)
                        },
                        onError = {error ->
                            Log.e(TAG, "Couldn't write SetCard to database", error)
                        }
                )

    }
}
