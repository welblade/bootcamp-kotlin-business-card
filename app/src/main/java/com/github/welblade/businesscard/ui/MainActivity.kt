package com.github.welblade.businesscard.ui

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.welblade.businesscard.App
import com.github.welblade.businesscard.databinding.ActivityMainBinding
import com.github.welblade.businesscard.util.Image

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
        setUpPermissions()
        mainBinding.rvCardList.layoutManager = LinearLayoutManager(this)
        mainBinding.rvCardList.adapter = adapter
        getAllBusinessCards()
        insertListeners()
    }

    override fun onRestart() {
        super.onRestart()
        getAllBusinessCards()
    }

    private fun setUpPermissions() {
        // write permission to access the storage
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            1
        )
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            1
        )
    }
    private fun insertListeners(){
        mainBinding.fbAddCard.setOnClickListener {
            val intent = Intent(
                this@MainActivity,
                AddBusinessCardActivity::class.java
            )
            startActivity(intent)
        }
        adapter.shareListener = {
                card -> Image.share(this@MainActivity,card)
        }
    }

    private fun getAllBusinessCards(){
        mainViewModel.getAll().observe(
            this, { businessCards -> adapter.submitList(businessCards)}
        )
    }
}