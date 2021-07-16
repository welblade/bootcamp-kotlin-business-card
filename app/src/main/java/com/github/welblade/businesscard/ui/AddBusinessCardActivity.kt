package com.github.welblade.businesscard.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.github.welblade.businesscard.App
import com.github.welblade.businesscard.R
import com.github.welblade.businesscard.data.BusinessCard
import com.github.welblade.businesscard.databinding.ActivityAddBusinessCardBinding
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener

class AddBusinessCardActivity : AppCompatActivity(), ColorPickerDialogListener {
    private val addCardBinding : ActivityAddBusinessCardBinding by lazy{
        ActivityAddBusinessCardBinding.inflate(layoutInflater)
    }
    private val mainViewModel: MainViewModel by viewModels{
        MainViewModelFactory((application as App).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(addCardBinding.root)
        setUpCard()
        insertListeners()
        changeBtnColorValues(customBackgroundColor)
    }
    private var customBackgroundColor:Int = Color.CYAN
    private var cardId = 0
    private var dialogId = 0

    private fun setUpCard(){
        cardId = intent.getIntExtra("cardId",0)
        if (cardId == 0){
            return
        }
        Thread {
            try {
                val businessCard: BusinessCard? = mainViewModel.findBussinessCardById(cardId)
                cardId = businessCard?.id ?: -1
                runOnUiThread {
                    addCardBinding.tvTitle.text = getString(R.string.edit_business_card)
                    addCardBinding.tilName.editText?.setText(businessCard?.name)
                    addCardBinding.tilPhone.editText?.setText(businessCard?.phone)
                    addCardBinding.tilEmail.editText?.setText(businessCard?.email)
                    addCardBinding.tilCompany.editText?.setText(businessCard?.company)
                    val color = Color.parseColor(businessCard?.customBackground)
                    changeBtnColorValues(color)
                }
            } catch (err: Exception) {
                Log.e("DBHELPER", "Consulta: " + err.message.toString())
            }
        }.start()
    }
    private fun insertListeners(){
        addCardBinding.btnClose.setOnClickListener{
            finish()
        }
        addCardBinding.btnColor.setOnClickListener {
            ColorPickerDialog.newBuilder()
                .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
                .setDialogId(dialogId)
                .setAllowPresets(false)
                .setColor(customBackgroundColor)
                .setShowAlphaSlider(true)
                .show(this)
        }
        addCardBinding.btnSave.setOnClickListener {
            val businessCard = BusinessCard(
                id = cardId,
                name = addCardBinding.tilName.editText?.text.toString(),
                phone = addCardBinding.tilPhone.editText?.text.toString(),
                email = addCardBinding.tilEmail.editText?.text.toString(),
                company = addCardBinding.tilCompany.editText?.text.toString(),
                customBackground =  getString(
                    R.string.color_hexadecimal,
                    Integer.toHexString(customBackgroundColor).uppercase()
                )
            )
            if(cardId == 0){
                mainViewModel.insert(businessCard)
            } else {
                mainViewModel.update(businessCard)
            }

            Toast.makeText(this, R.string.save_card_successful, Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun changeBtnColorValues(color: Int){
        addCardBinding.btnColor.setBackgroundColor(color)
        addCardBinding.btnColor.text = getString(R.string.color_value, Integer.toHexString(color).uppercase())
        customBackgroundColor = color
    }

    override fun onColorSelected(dialogId: Int, color: Int) {
        changeBtnColorValues(color)
    }

    override fun onDialogDismissed(dialogId: Int) {
        Log.d("COLORPICKER", "onDialogDismissed() called with: dialogId = [$dialogId]")
    }
}