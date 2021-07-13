package com.github.welblade.businesscard.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.welblade.businesscard.data.BusinessCard
import com.github.welblade.businesscard.data.BusinessCardRepository

class MainViewModel(private val businessCardRepository: BusinessCardRepository) : ViewModel() {
    fun getAll() : LiveData<List<BusinessCard>> {
        return businessCardRepository.getAll()
    }

    fun insert(businessCard: BusinessCard){
        businessCardRepository.insert(businessCard)
    }
}

class MainViewModelFactory(private val repository: BusinessCardRepository):
    ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java))
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        throw IllegalArgumentException("Unknow ViewModel class")
    }


}