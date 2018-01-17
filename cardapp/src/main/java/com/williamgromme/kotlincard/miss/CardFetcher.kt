package com.williamgromme.kotlincard.miss

import android.util.Log
import com.williamgromme.kotlincard.db.FlashCard
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


/**
 * Created by Dad on 11/3/2017.
 * Import JSON from url.
 */
class CardFetcher() {
    private final val TAG = "CardFetcher"

    private fun getUrlString(urlString: String):String {
        //Creates URL connection to json to import flashcards

        var url = URL(urlString)
        var connection = url.openConnection() as HttpURLConnection
        try {
            var out: ByteArrayOutputStream = ByteArrayOutputStream()
            var code = connection.responseCode
            Log.i(TAG, "The code is: " + code)
            if (code != HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "the result code: " + code)
                throw IOException(connection.responseMessage + ": with " + urlString)
            }

            var result: String = ""
            val inputStream: InputStream = connection.inputStream
            val isReader = InputStreamReader(inputStream)
            var buffReader = BufferedReader(isReader, 1024)
            var bytesRead: Int = 0
            result = buffReader.readText()
            return result

        }
        catch (e: Exception ){
            Log.d(TAG,"Exception:" +e )
        }
        finally {
            //Log.d(TAG,"finally")
            connection.disconnect()
        }
        return "error"
    }

    public fun getJson(urlS : String):String {
        val jsonString = getUrlString(urlS)
        return jsonString
    }

    public fun parseItems( jsonString: String): ArrayList<FlashCard>  {
         //Get JSON into cards
        //val turnsType = object : TypeToken<List<Turns>>() {}.type
        //val turns = Gson().fromJson<List<Turns>>(pref.turns, turnsType)

        var cardItemType: Type = object : TypeToken<kotlin.collections.ArrayList<FlashCard>>() {}.type
        var cards = ArrayList<FlashCard>()

        var gson = Gson()
        try {
            val jsonObject:JSONObject = JSONObject(jsonString)
            val jsonArray:JSONArray = jsonObject.getJSONArray("cards")
            val jsonArrayString = jsonArray.toString()
            val cardsList:ArrayList<FlashCard> = gson.fromJson(jsonArrayString,cardItemType)
            cards.addAll(cardsList)

        } catch (e: JSONException) {
            Log.e(TAG, "Failed to parse JSON: " + e)
        }
        return cards
    }

    public fun fetchCards(url:String):ArrayList<FlashCard> {
        var cards = ArrayList<FlashCard>()
        var jsonString = getUrlString(url)
        cards = parseItems( jsonString)
        // loop cards set show to true
        for( i in 0 .. cards.size -1){
            cards[i].show = true
        }
        return cards
    }
}