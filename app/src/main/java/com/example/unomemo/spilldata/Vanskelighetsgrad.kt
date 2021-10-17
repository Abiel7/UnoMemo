package com.example.unomemo.spilldata

enum class Vanskelighetsgrad(val antallkort:Int) {
ENKEL(8), MIDDELS(12), KRVENDE(16);

    // info om lengde og bredde tilsvarende til hver enkelt brett
    // sånn når bruker endrer vanskelighetes grad så endres brette også
    //tilsvarende til Vanskelighetsgrad

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




}