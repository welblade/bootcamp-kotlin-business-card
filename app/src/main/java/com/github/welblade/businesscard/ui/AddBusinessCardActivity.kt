package com.github.welblade.businesscard.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.welblade.businesscard.databinding.ActivityAddBusinessCardBinding

class AddBusinessCardActivity : AppCompatActivity() {
    private val addCardBinding : ActivityAddBusinessCardBinding by lazy{
        ActivityAddBusinessCardBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(addCardBinding.root)
        insertListeners()
    }
    private fun insertListeners(){
        addCardBinding.btnClose.setOnClickListener{
            finish()
        }
        addCardBinding.btnSave.setOnClickListener {  }
    }
}