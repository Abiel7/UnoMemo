package com.example.unomemo.spillKort

import com.google.firebase.firestore.PropertyName
//https://code.luasoftware.com/tutorials/google-cloud-firestore/firestore-change-kotlin-pojo-property-name-mapping/
// https://stackoverflow.com/questions/38681260/firebase-propertyname-doesnt-work
data class ImageList(@PropertyName("images") val images :List<String>?  =null)
