package com.example.unomemo.kategori

import com.example.unomemo.R

class KategoriData {

    companion object{
        @JvmStatic
        var kategoribilde: Array<Int> = arrayOf(
            R.drawable.norge,
            R.drawable.dyr,
            R.drawable.bil,
            R.drawable.mat
        )
        var kategorinavn: Array<String> = arrayOf(
            "Flagg",
            "bil",
            "Dyr",
            "Mat"
        )
        var spillerliste: Array<String> = arrayOf(
            "Jens",
            "Anne",
            "Halvor"
        )
        var spillerpoengliste: Array<String> = arrayOf(
            "500",
            "400",
            "350"
        )
        var spillerplassingliste: Array<String> = arrayOf(
            "1",
            "2",
            "3"
        )
    }
}