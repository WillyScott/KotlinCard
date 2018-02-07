package com.williamgromme.kotlincard.setcard

import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import com.williamgromme.kotlincard.miss.CardFetcher
import com.williamgromme.kotlincard.miss.SingleFragmentActivity
import com.williamgromme.kotlincard.db.FlashCard
import com.williamgromme.kotlincard.db.MyApp
import com.williamgromme.kotlincard.db.SetCard
import com.williamgromme.kotlincard.import.PreloadedData
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File
import java.io.InputStream
import java.nio.charset.Charset
import java.util.*

/**
 * Created by Dad on 11/27/2017.
 */
class SetCardListActivity: SingleFragmentActivity() {

    private final val TAG = "SetCardListActivity"

    companion object {
        fun newIntent(context: Context): Intent {
            var intent = Intent(context, SetCardListActivity::class.java)
            return intent
        }
    }

    override fun createFragment(): Fragment {
        return SetCardFragment.newInstance()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val filename = "datainstallFile"
        var file = File(filesDir, filename)
        file = File(applicationContext.filesDir, filename)
        file.appendText("version2", Charset.defaultCharset())

        //var cardList = ArrayList<flashcard>()
        var assets: AssetManager = getAssets()
        for (i in 0..PreloadedData.fileNames.size - 1) {

            var streamJson: InputStream = assets.open(PreloadedData.fileNames[i])
            var stringJson = streamJson.readTextAndClose()
            val cards = CardFetcher().parseItems(stringJson)
            // add cards to database
            if (cards.size > 0) {
                //Log.d(TAG, "loop: " + i + "Cards found:" + cards.size)
                //add the set and cards to database
                addFlashCardsfromFile(cards, i)
            }
        }

//        if (file.isFile) {
//           // Log.d(TAG, "emptyfile found JSON data already inported from file")
//        } else {
//            //create the file
//            //add some text or the empty file is delete
//            //Log.d(TAG, "emptyfile not found, creating file")
//            file = File(applicationContext.filesDir, filename)
//            file.appendText("test", Charset.defaultCharset())
//
//            //var cardList = ArrayList<flashcard>()
//            var assets: AssetManager = getAssets()
//            for (i in 0..PreloadedData.fileNames.size - 1) {
//
//                var streamJson: InputStream = assets.open(PreloadedData.fileNames[i])
//                var stringJson = streamJson.readTextAndClose()
//                val cards = CardFetcher().parseItems(stringJson)
//                // add cards to database
//                if (cards.size > 0) {
//                    //Log.d(TAG, "loop: " + i + "Cards found:" + cards.size)
//                    //add the set and cards to database
//                    addFlashCardsfromFile(cards, i)
//                }
//            }
//        }
    }

    // from https://stackoverflow.com/questions/39500045/in-kotlin-how-do-i-read-the-entire-contents-of-an-inputstream-into-a-string
    fun InputStream.readTextAndClose(charset: Charset = Charsets.UTF_8): String {
        return this.bufferedReader(charset).use { it.readText() }
    }

    fun addFlashCardsfromFile(flashcards: ArrayList<FlashCard>, arrayPostion: Int) {
        //random number to get the Set just created
        val randomNum = Random().nextInt()
        //database off the main thread
        doAsync {

            //create Set but set name to random number
            val newSetCard = SetCard(randomNum.toString(), PreloadedData.setSection[arrayPostion], PreloadedData.setNames[arrayPostion], PreloadedData.fileURLs[arrayPostion],
                    false, false)
            MyApp.dataBase.setCardDao().insert(newSetCard)
            //query the set on the random number
            //should only be one set returned
            val setDatabase: SetCard = MyApp.dataBase.setCardDao().getSetUniqueName(randomNum.toString())
            // Update the set name replacing the random number
            setDatabase.name = PreloadedData.setNames[arrayPostion]
            setDatabase.count = flashcards.size
            MyApp.dataBase.setCardDao().update(setDatabase)
            //update the flashcards to the set id
            for (i in 0..flashcards.size - 1) {
                flashcards[i].set_uid = setDatabase.uid
                flashcards[i].show = true
            }
            //insert the flashcards
            MyApp.dataBase.flashCardDao().insertAll(flashcards)
            uiThread {
                // TODO Remove this nothing to do on main thread
            }
        }
    }
}
