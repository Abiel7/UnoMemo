package com.example.unomemo.LastOppBilde

import com.google.firebase.firestore.PropertyName

data class ImageUploadList(@PropertyName("images")
                           val images: List<String>?  =  null
)
