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
import com.littlemango.stacklayoutmanager.StackLayoutManager
import com.littlemango.stacklayoutmanager.StackLayoutManager.ScrollOrientation

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
        setUpListLayout()
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
    private fun setUpListLayout(){
        mainBinding.rvCardList.layoutManager = StackLayoutManager(ScrollOrientation.TOP_TO_BOTTOM).apply {
            //setVisibleItemCount(5)
            setItemOffset(50)
        }
        mainBinding.rvCardList.adapter = adapter
    }
    private fun insertListeners() {
        mainBinding.fbAddCard.setOnClickListener {
            val intent = Intent(
                this@MainActivity,
                AddBusinessCardActivity::class.java
            )
            startActivity(intent)
        }

        adapter.cardClickListener = { card -> run {
            ListItemBusinessCardAnimator.rotateCardView(card as MaterialCardView)
        }

        }
        adapter.shareListener = {
            button -> run {
                val card = button.parent.parent as MaterialCardView
                ListItemBusinessCardAnimator.rotateCardView(card)?.doOnEnd {
                    Image.share(this@MainActivity, card)
                }
            }
        }
        adapter.deleteListener = { _, card ->
            mainViewModel.delete(card)
        }
    }

    private fun getAllBusinessCards(){
        mainViewModel.getAll().observe(
            this, { businessCards -> run {
                setUpListLayout()
                adapter.submitList(businessCards)
                mainBinding.rvCardList.invalidate()
                }
            }
        )
    }
}