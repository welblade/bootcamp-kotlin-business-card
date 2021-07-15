package com.github.welblade.businesscard.ui

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.welblade.businesscard.data.BusinessCard
import com.github.welblade.businesscard.databinding.ListItemBusinessCardBinding

class BusinessCardAdapter : ListAdapter<BusinessCard, BusinessCardAdapter.ViewHolder> (DiffCallback()) {
    var cardClickListener: (View) -> Unit = {}
    var shareListener: (View) -> Unit = {}
    var deleteListener: (View) -> Unit = {}
    var editListener: (View) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val listItemBinding = ListItemBusinessCardBinding.inflate(inflater, parent, false)

        return ViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder (
        private val listItemBinding: ListItemBusinessCardBinding
        ): RecyclerView.ViewHolder(listItemBinding.root) {
        fun bind(item: BusinessCard){
            try {
                val color = Color.parseColor(item.customBackground)
                listItemBinding.cvCard.setCardBackgroundColor(color)
            }catch (err: Exception){
                Log.e("CARD_ADAPTER", err.message.toString())
            }catch (err: IllegalArgumentException ){
                Log.e("CARD_ADAPTER", err.message.toString())
            }
            listItemBinding.tvName.text = item.name
            listItemBinding.tvPhone.text = item.phone
            listItemBinding.tvEmail.text = item.email
            listItemBinding.tvCompany.text = item.company
            listItemBinding.btnShare.setOnClickListener{
                shareListener(it)
            }
            listItemBinding.cvCard.setOnClickListener {
                cardClickListener(it)
            }
        }
    }
}
class DiffCallback: DiffUtil.ItemCallback<BusinessCard>(){
    override fun areItemsTheSame(oldItem: BusinessCard, newItem: BusinessCard): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BusinessCard, newItem: BusinessCard): Boolean {
        return  oldItem.id == newItem.id
    }

}