package com.example.dad.kotlincard

/**
 * Created by Dad on 11/2/2017.
 */
object Cards {
    public lateinit var cardsList :ArrayList<Card>
    init {
        for (i in 1 .. 100){
            val temp = Card("front" + i,"back" + i)
            cardsList.add(temp)

        }

    }
}