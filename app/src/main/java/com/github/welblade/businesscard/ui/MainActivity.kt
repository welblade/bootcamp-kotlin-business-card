package com.github.welblade.businesscard.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.welblade.businesscard.App
import com.github.welblade.businesscard.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val mainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val mainViewModel: MainViewModel by viewModels{
        MainViewModelFactory((application as App).repository)
    }
    private val adapter: BusinessCardAdapter by lazy{
        BusinessCardAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)
        mainBinding.rvCardList.layoutManager = LinearLayoutManager(this)
        mainBinding.rvCardList.adapter = adapter
        getAllBusinessCards()
        insertListeners()
    }

    override fun onRestart() {
        super.onRestart()
        getAllBusinessCards()
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

    private fun getAllBusinessCards(){
        mainViewModel.getAll().observe(
            this, { businessCards -> adapter.submitList(businessCards)}
        )
    }
}