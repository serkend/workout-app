package com.example.a5_workout_app

import android.app.Application

class WorkOutApp:Application() {

    val db by lazy {
        HistoryDatabase.getInstance(this)
    }
}