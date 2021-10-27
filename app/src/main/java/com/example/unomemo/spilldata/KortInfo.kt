package com.example.unomemo.spilldata

data class KortInfo(
    val id : Int,
    val questionList: List<MutableList<Int>>? = null,
    var isUp:Boolean = false,
    var isaMatch:Boolean =false
)
