package com.example.unomemo.data.model

import com.google.firebase.auth.FirebaseUser
import org.jetbrains.annotations.NotNull

data class LoginUser(@NotNull val id:String, @NotNull val user: FirebaseUser?)