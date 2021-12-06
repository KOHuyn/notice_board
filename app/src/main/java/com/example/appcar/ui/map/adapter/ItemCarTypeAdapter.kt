package com.example.appcar.ui.map.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appcar.R
import com.example.appcar.data.model.CarType
import com.example.appcar.databinding.ItemCarTypeBinding

class ItemCarTypeAdapter :
    ListAdapter<CarType, ItemCarTypeAdapter.MapCarTypeViewHolder>(DIFF) {
    companion object {
        val DIFF = object : DiffUtil.ItemCallback<CarType>() {
            override fun areItemsTheSame(oldItem: CarType, newItem: CarType): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CarType, newItem: CarType): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapCarTypeViewHolder {
        return MapCarTypeViewHolder(
            ItemCarTypeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class MapCarTypeViewHolder(private val binding: ItemCarTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(carType: CarType) {
            binding.nameCarType.text = carType.title
            binding.root.setBackgroundResource(if (carType.isSelected) R.drawable.bg_icon_type_selected else R.drawable.bg_icon_type_unselected)
            binding.nameCarType.setTextColor(itemView.context.getColor(if (carType.isSelected) android.R.color.white else R.color.bgBottomNavigation))
        }
    }

    val arrTypeSelected get() = currentList.filter { it.isSelected }.map { it.type }

    private fun interface OnCarTypeListener {
        fun setOnCarTypeListener(arrType: List<Int>)
    }

    private var onCarTypeClick: OnCarTypeListener? = null
    fun setOnCarTypeChangeListener(callback: (arrType: List<Int>) -> Unit) {
        onCarTypeClick = OnCarTypeListener(callback)
    }

    override fun onBindViewHolder(holder: MapCarTypeViewHolder, position: Int) {
        holder.bindView(getItem(position))
        holder.itemView.setOnClickListener {
            val carType = getItem(position)
            carType.isSelected = !carType.isSelected
            notifyItemChanged(position)
            onCarTypeClick?.setOnCarTypeListener(arrTypeSelected)
        }
    }
}