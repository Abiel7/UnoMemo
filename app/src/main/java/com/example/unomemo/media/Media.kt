package com.example.unomemo.media

import android.util.Log

import com.example.unomemo.spilldata.KortInfo
import com.example.unomemo.spilldata.START_FLAGS
import com.example.unomemo.spilldata.Vanskelighetsgrad

class Media(private val gameSize : Vanskelighetsgrad , image :List<String>? ) {

    var cardINFO:List<KortInfo>

    private var index :Int? =  null

    private  var matches =  0

     var moves = 0
    companion object {
        const val TAG = "Media"
    }

    init {
        // get picture from
        if(image == null) {
            val flags: List<Int> = START_FLAGS.shuffled().take(gameSize.sumMuchOnGame())
            val shuffledItems = (flags + flags).shuffled()
            cardINFO = shuffledItems.map { KortInfo(it.hashCode()) }
        }

        // get from firestorage

        else {
            val shuffledItems  = (image + image).shuffled()
            cardINFO = shuffledItems.map { KortInfo(it.hashCode(),it) }
        }

    }

    fun flipCards(pos: Int):Boolean{
        this.moves++
        val kort  = cardINFO?.get(pos)
        var matchFound = false
        if(index == null){
            // case1 og case2
            resetCard()
            index = pos

            print(" index in first if statement  $index")

        } else {
            matchFound = check(index!!,pos)

            index=null
            print( "Else statment $index")
        }
        kort!!.isUp = !kort.isUp
        return matchFound
    }

    /**
     * metode som sjekker om to kort er likt basert på id
     */
    private fun check(pos1:Int,pos2:Int) :Boolean {
        val idIfno1  =  cardINFO!![pos1].id
        val idIfno2 = cardINFO!![pos2].id

        if(cardINFO!![pos1].id != cardINFO!![pos2].id){
            Log.i(TAG, "$idIfno1 $idIfno2")
            return false
        }
        cardINFO!![pos1].isaMatch =  true
        cardINFO!![pos2].isaMatch =  true
        matches++
        print( "this is antal kort $matches" )
        return true

    }


    /**
    * set kort tilbake til orginal bilde,
    *
    */
    private fun resetCard() {
        for (i in cardINFO!! ){
            if(!i.isaMatch){
                i.isUp= false
            }
        }
    }

    /**
     * hvor mange kort  er oppe
     */
    fun matchPares( ):Boolean{
        return matches == gameSize.sumMuchOnGame()
    }

    /**
     * om en kort er flipped eller ikke
     */
    fun isUpCards(pos: Int): Boolean {
        return cardINFO?.get(pos)?.isUp == true
    }


    fun getAmountMoves():Int{
        return this.moves.times(2)
    }





}