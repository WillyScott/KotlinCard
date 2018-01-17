package com.williamgromme.kotlincard.card

import java.io.Serializable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;

/**
 * Created by Dad on 11/2/2017.
 */
data class Card  (@SerializedName ("front") @Expose var front: String?, @SerializedName("back") @Expose var back: String?) :Serializable