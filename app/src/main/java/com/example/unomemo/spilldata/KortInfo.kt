package com.example.unomemo.spilldata

data class KortInfo(
    val id : Int,
    val questionURL: String ? = null,
    var isUp:Boolean = false,
    var isaMatch:Boolean =false
)
