package com.github.welblade.businesscard

import android.app.Application
import com.github.welblade.businesscard.data.AppDatabase
import com.github.welblade.businesscard.data.BusinessCardRepository

class App : Application() {
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }
    val repository: BusinessCardRepository by lazy {
        BusinessCardRepository(database.businessCardDao())
    }
}