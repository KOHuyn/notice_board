package com.example.appcar.ui.map.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appcar.data.model.Road
import com.example.appcar.databinding.ItemMapInfoBinding

class ItemMapRoadAdapter : ListAdapter<Road, ItemMapRoadAdapter.ItemMapViewHolder>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Road>() {
            override fun areItemsTheSame(oldItem: Road, newItem: Road): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Road, newItem: Road): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMapViewHolder {
        return ItemMapViewHolder(
            ItemMapInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemMapViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemMapViewHolder(private val binding: ItemMapInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Road) {
            binding.titleItemMap.text = item.name
            binding.descriptionItemMap.text = item.description
            binding.cbFavorite.isChecked = item.isFavorite
            binding.root.setOnClickListener {
                onRoadClick?.setOnRoadListener(item)
            }
            binding.cbFavorite.setOnClickListener {
                item.isFavorite = !item.isFavorite
                onRoadFav?.setOnRoadListener(item)
            }
        }

    }

    private fun interface OnRoadListener {
        fun setOnRoadListener(road: Road)
    }

    private var onRoadClick: OnRoadListener? = null
    fun setOnItemClickListener(callback: (road: Road) -> Unit) {
        onRoadClick = OnRoadListener(callback)
    }

    private var onRoadFav: OnRoadListener? = null
    fun setOnItemFavoriteListener(callback: (road: Road) -> Unit) {
        onRoadFav = OnRoadListener(callback)
    }

}

