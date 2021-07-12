package com.github.welblade.businesscard.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.welblade.businesscard.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val mainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)
        insertListeners()
    }
    private fun insertListeners(){
        mainBinding.fbAddCard.setOnClickListener {
            val intent = Intent(
                this@MainActivity,
                AddBusinessCardActivity::class.java
            )
            startActivity(intent)
        }
    }
}