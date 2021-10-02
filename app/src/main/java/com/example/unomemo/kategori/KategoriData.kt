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
            "Kim",
            "Abiel",
            "Svein Torger"
        )
    }
}