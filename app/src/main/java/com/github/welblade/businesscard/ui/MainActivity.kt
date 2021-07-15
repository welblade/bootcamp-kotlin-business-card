package com.github.welblade.businesscard.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.app.ActivityCompat
import com.github.welblade.businesscard.App
import com.github.welblade.businesscard.R
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

    private val layoutManager: StackLayoutManager by lazy {
        StackLayoutManager(ScrollOrientation.TOP_TO_BOTTOM,7).apply {
            setItemOffset(32)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)
        setUpPermissions()
        setUpListLayout()
        insertListeners()
    }

    override fun onStart() {
        super.onStart()
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
    private fun setUpListLayout(){
        mainBinding.rvCardList.layoutManager = layoutManager
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

        adapter.editListener = { cardView, businessCard ->
            val intent = Intent(
                this@MainActivity,
                AddBusinessCardActivity::class.java
            )
            intent.putExtra("cardId", businessCard.id)
            startActivity(intent)
            ListItemBusinessCardAnimator.rotateCardView(cardView as MaterialCardView)
        }

        adapter.cardClickListener = { card ->
            run {
                ListItemBusinessCardAnimator.rotateCardView(card as MaterialCardView)
            }
        }
        adapter.shareListener = { cardView ->
            run {
                val card = cardView as MaterialCardView
                ListItemBusinessCardAnimator.rotateCardView(card)?.doOnEnd {
                    Image.share(this@MainActivity, card)
                }
            }
        }
        adapter.deleteListener = { cardView, businessCard ->
            run {
                val mCardView = cardView as MaterialCardView
                val layout = mCardView.parent as ConstraintLayout
                AlertDialog.Builder(this@MainActivity).apply {
                    setMessage(R.string.are_you_sure_you_want_delete_this_card)
                    setCancelable(false)
                    setPositiveButton(R.string.yes) { _, _ ->
                       ListItemBusinessCardAnimator.fadeOutCard(mCardView).doOnEnd {
                           mainBinding.rvCardList.removeView(layout)
                           mainViewModel.delete(businessCard)
                       }
                    }
                    setNegativeButton(R.string.no) { dialog, _ -> dialog.dismiss() }
                    create()
                }.show()
            }
        }
    }
    private fun getAllBusinessCards(){
        mainViewModel.getAll().observe(
            this, { businessCards -> run {
                adapter.submitList(businessCards)
                }
            }
        )
    }
}