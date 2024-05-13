package com.vaneezaahmad.sheworks

import android.app.Application

class OfflineSupport : Application() {
    override fun onCreate() {
        super.onCreate()
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}