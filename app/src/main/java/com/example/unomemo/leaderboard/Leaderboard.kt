package com.example.unomemo.leaderboard

import com.google.firebase.firestore.Blob

//Modell klassa for leaderboard
data class Leaderboard(
    var uid: String="",
    var navn: String ="",
    var poengsum: Long =-1
)
