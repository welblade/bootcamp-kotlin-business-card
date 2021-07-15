package com.github.welblade.businesscard.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.app.ActivityCompat
import com.github.welblade.businesscard.App
import com.github.welblade.businesscard.databinding.ActivityMainBinding
import com.github.welblade.businesscard.util.Image
import com.github.welblade.businesscard.util.ListItemBusinessCardAnimator
import com.google.android.material.card.MaterialCardView
import com.hirayclay.Align as StackLayoutAlign
import com.hirayclay.Config as StackLayoutConfig
import com.hirayclay.StackLayoutManager as StackLayoutManagerHC

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
        val config: StackLayoutConfig = StackLayoutConfig().apply{
            secondaryScale = 0.95f
            scaleRatio = 0.4f
            maxStackCount = 5
            initialStackCount = 1
            space = 45
            parallex = 1.5f //parallex factor
            align= StackLayoutAlign.TOP
        }
        mainBinding.rvCardList.layoutManager = StackLayoutManagerHC(config)
        mainBinding.rvCardList.adapter = adapter
        getAllBusinessCards()
        insertListeners()
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

        adapter.cardClickListener = { card ->
            ListItemBusinessCardAnimator.rotateCardView(card as MaterialCardView)
        }
        adapter.shareListener = {
            button -> run {
                val card = button.parent.parent as MaterialCardView
                ListItemBusinessCardAnimator.rotateCardView(card)?.doOnEnd {
                    Image.share(this@MainActivity, card)
                }
            }
        }
    }

    private fun getAllBusinessCards(){
        mainViewModel.getAll().observe(
            this, { businessCards -> adapter.submitList(businessCards)}
        )
    }
}