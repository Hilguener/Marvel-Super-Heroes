package com.hilguener.marvelsuperheroes

import android.app.Application
import com.google.firebase.FirebaseApp

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Inicialize o Firebase no seu aplicativo
        FirebaseApp.initializeApp(this)
    }
}
