package com.vaneezaahmad.sheworks

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class OfflineSupport : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}