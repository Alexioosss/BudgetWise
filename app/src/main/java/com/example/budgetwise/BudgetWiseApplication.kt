package com.example.budgetwise

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi

class BudgetWiseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContainer.init(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        AppContainer.terminate()
    }
}