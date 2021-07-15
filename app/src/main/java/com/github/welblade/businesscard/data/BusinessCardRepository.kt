package com.github.welblade.businesscard.data

import android.os.AsyncTask
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class BusinessCardRepository(private val dao: BusinessCardDao) {
    fun insert(businessCard: BusinessCard) = runBlocking {
        launch(Dispatchers.IO){
            dao.insert(businessCard)
        }
    }
    fun update(businessCard: BusinessCard) = runBlocking {
        launch(Dispatchers.IO){
            dao.update(businessCard)
        }
    }
    fun delete(businessCard: BusinessCard) = runBlocking {
        launch(Dispatchers.IO){
            dao.delete(businessCard)
        }
    }

    fun getAll() = dao.getAll()

    fun findById(businessCardId: Int):BusinessCard = dao.findById(businessCardId)
}