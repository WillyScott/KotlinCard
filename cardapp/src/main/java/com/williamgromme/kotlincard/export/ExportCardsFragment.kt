package com.williamgromme.kotlincard.export

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.*
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.williamgromme.kotlincard.R
import com.williamgromme.kotlincard.db.FlashCard
import com.williamgromme.kotlincard.db.MyApp
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONException
import java.lang.reflect.Type


/**
 * Created by Dad on 12/26/2017.
 */
class ExportCardsFragment:Fragment() {

    private final val TAG = "ExportCardsFragment"
    private lateinit var textView: TextView
    private var setcard_id = -1
    private lateinit var flashcards: ArrayList<FlashCard>
    private final val DIALOG_CLIPBOARD = "DialogClipBoard"
    private var jsonString = ""
    private var csvString = ""
    private var jsonStringGson = ""

    companion object {
        private final val SETCARD_ID = "setcard_id"

        fun newInstance(uid: Int): Fragment {
            var args = Bundle()
            args.putInt(SETCARD_ID, uid)
            var fragment = ExportCardsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.fragment_exportcards, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item?.itemId == R.id.filetypejson) {
            textView.setText(jsonString)
            return true
        }else if (item?.itemId == R.id.filetypecvs){
            textView.setText(csvString)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Get the setcard id
        setcard_id = arguments.getInt(SETCARD_ID)
        // Query the database and get the flashcards to export to JSON and CSV format
        flashcards = ArrayList<FlashCard>()
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater!!.inflate(R.layout.fragment_exportcards, container, false)
        textView = view.findViewById<TextView>(R.id.textViewExport)
        textView.movementMethod = ScrollingMovementMethod()
        textView.setTextIsSelectable(true)
        textView.setOnClickListener { v ->

            var cm: ClipboardManager
            cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val cData = ClipData.newPlainText("text", textView.text)
            cm.primaryClip = cData
            val dialog = AlertClipBoardFragment()
            dialog.show(fragmentManager, DIALOG_CLIPBOARD)
        }

        textView.setText("Export Loading")
        jsoncsvFromQuery()
        return view
    }

    fun jsoncsvFromQuery() {
        Single.fromCallable {
            MyApp.dataBase.flashCardDao().getAllNotFlowable(setcard_id)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { flashcardsDB ->
                            flashcards.clear()
                            flashcards.addAll(flashcardsDB)
                            // If there are cards convert to json and csv
                            if (flashcards.size > 0) {

                                //jsonString = arrayListtoJSON()
                                csvString = arrayListtoCSV()
                                jsonStringGson = jsonfromGson()


                                // Test using Gson to convert to Json
                                val testJson = jsonfromGson()
                                //Log.d(TAG, jsonfromGson())
                                textView.setText(jsonStringGson)

                            }
                        },
                        onError = { error  ->
                            //Log.e(TAG, "Error getting data: " + error)
                        }
                )
    }

    //Test to see formate of json string output
    fun jsonfromGson():String {
        var json:String = ""
        var frontJson = "{ \"cards\":\n"
        var backJson = "\n" + "}"
        if (flashcards.size > 0){
            var cardType: Type = object : TypeToken<kotlin.collections.ArrayList<FlashCard>>(){}.type
            try {
                var gsonBuilder = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
                json = gsonBuilder.toJson(flashcards,cardType)

                }catch (e: JSONException){
                    Log.d(TAG, "Failed to convert list to json: " + e.toString())
                }
        }

        // add some line spaces to make json string display nicer
        json = json.replace("},{","},\n{" )
        return (frontJson + json + backJson)
    }

    fun arrayListtoCSV(): String {
        var stringOut = ""

        for (i in 0..flashcards.size - 1) {
            var stringFront = flashcards[i].frontcard
            var stringBack = flashcards[i].backcard

            //replace " with \"
            //replace newline with \n
            //replace carriage
            //replace , with \,
            stringFront = stringFront.replace("\"", "\\\"")
            stringBack = stringBack.replace("\"", "\\\"")
            stringFront = stringFront.replace("\n", "\\n")
            stringBack = stringBack.replace("\n", "\\n")
            stringFront = stringFront.replace(",", "\\,")
            stringBack = stringBack.replace("\n", "\\,")

            stringOut = stringOut + "\"" +  stringFront + "\",\"" + stringBack + "\"\n"
    }
        return stringOut
    }
}