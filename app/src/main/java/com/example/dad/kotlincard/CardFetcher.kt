package com.example.dad.kotlincard

import android.util.Log
import com.example.dad.kotlincard.card.Card
import com.example.dad.kotlincard.db.FlashCard
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
 */
class CardFetcher() {
    private final val TAG = "CardFetcher"

    private fun getUrlString(urlString: String):String {
        //  https://raw.githubusercontent.com/WillyScott/FlashCardsData/master/git.json

        //var url = URL("https://raw.githubusercontent.com/WillyScott/FlashCardsData/master/Swift_KeywordsV3_0_1.json")
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

    private fun getURlBytes (urlString: String) {

        var url = URL("https://raw.githubusercontent.com/WillyScott/FlashCardsData/master/Swift_KeywordsV3_0_1.json")
        var connection = url.openConnection() as HttpURLConnection

        try {
            var out: ByteArrayOutputStream = ByteArrayOutputStream()
            var code = connection.responseCode
            if (code!= HttpURLConnection.HTTP_OK) {
                throw  IOException(connection.responseMessage + ": with "+ urlString)
            }

            val inputStream: InputStream = connection.inputStream
            val isReader:InputStreamReader = InputStreamReader(inputStream)
            val buffReader = BufferedReader(isReader,1024)
            var bytesRead:Int = 0

        }catch (e: Exception) {
            Log.d(TAG,"Exception: " + e)

        } finally {
            connection.disconnect()
        }



    }

    public fun getJson(urlS : String):String {

        val jsonString = getUrlString(urlS)


        return jsonString
    }

    private fun parseItems(cards: ArrayList<FlashCard>, jsonString: String)  {
         //Get JSON into cards
        //val turnsType = object : TypeToken<List<Turns>>() {}.type
        //val turns = Gson().fromJson<List<Turns>>(pref.turns, turnsType)

        var cardItemType: Type = object : TypeToken<kotlin.collections.ArrayList<FlashCard>>() {}.type


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

    }

    public fun fetchCards(url:String):ArrayList<FlashCard> {
        var cards = ArrayList<FlashCard>()
        var jsonString = getUrlString(url)
        parseItems(cards, jsonString)
        // loop cards set show to true
        for( i in 0 .. cards.size -1){
            cards[i].show = true
        }
        return cards

    }



}