package com.example.unomemo.spilldata

enum class Vanskelighetsgrad(val antallkort:Int) {
ENKEL(8), MIDDELS(12), KRVENDE(16);

    // info om lengde og bredde tilsvarende til hver enkelt brett
    // sånn når bruker endrer vanskelighetes grad så endres brette også
    //tilsvarende til Vanskelighetsgrad
    companion object {
        @JvmStatic
        fun getVal(value : Int)  = values().first {it.antallkort == value}
    }


    fun getBredde() :Int{
        return  when(this){
            ENKEL -> 2
            MIDDELS -> 3
            KRVENDE -> 4
        }
    }

    fun getHoyde() : Int{
        return antallkort / getBredde()
    }


    /**
     *  finner ut hvor mange par det skal være basert på vankelighets grad brukeren har valgt
     *  så 8 kort 4 svar
    12 kort  6  svar
    16 kort 8 svar
     */
    fun sumMuchOnGame() : Int{
        return  antallkort / 2

    }

}