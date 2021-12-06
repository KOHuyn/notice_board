package com.example.appcar.ui.road.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appcar.R
import com.example.appcar.data.model.BaseItemRoad
import com.example.appcar.data.model.Road
import com.example.appcar.databinding.ItemHeaderBinding
import com.example.appcar.databinding.ItemRoadListBinding
import com.example.appcar.ui.road.adapter.RoadAdapter.OnCheckedChangeListener

/**
 * Created by KO Huyn on 29/10/2021.
 */
class RoadAdapter : ListAdapter<BaseItemRoad, RecyclerView.ViewHolder>(DIFF_ROAD) {
    companion object {
        val DIFF_ROAD = object : DiffUtil.ItemCallback<BaseItemRoad>() {
            override fun areItemsTheSame(oldItem: BaseItemRoad, newItem: BaseItemRoad): Boolean {
                return compareRoad(oldItem, newItem) || compareCarType(oldItem, newItem)
            }

            override fun areContentsTheSame(oldItem: BaseItemRoad, newItem: BaseItemRoad): Boolean {
                return oldItem == newItem
            }
        }

        private fun compareRoad(oldItem: BaseItemRoad, newItem: BaseItemRoad): Boolean {
            return oldItem is BaseItemRoad.RoadItem && newItem is BaseItemRoad.RoadItem && oldItem.road.id == newItem.road.id
        }

        private fun compareCarType(oldItem: BaseItemRoad, newItem: BaseItemRoad): Boolean {
            return oldItem is BaseItemRoad.CategoryItem && newItem is BaseItemRoad.CategoryItem && oldItem.carType.id == newItem.carType.id
        }
    }

    inner class RoadVH(val binding: ItemRoadListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: BaseItemRoad.RoadItem) {
            val item = data.road
            binding.txtTitle.text = item.name
            binding.txtDescription.text = item.description
            binding.cbFav.isChecked = item.isFavorite
            binding.cbFav.setOnClickListener {
                item.isFavorite = !item.isFavorite
                onFavChangeListener?.setOnCheckedChangeListener(item, item.isFavorite)
            }
        }
    }


    inner class CategoryVH(private val binding: ItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BaseItemRoad.CategoryItem) {
            binding.txtHeader.text = item.carType.description
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is BaseItemRoad.RoadItem -> R.layout.item_road_list
            is BaseItemRoad.CategoryItem -> R.layout.item_header
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_header -> CategoryVH(
                ItemHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.item_road_list -> RoadVH(
                ItemRoadListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("No view inflater")
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNullOrEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            if (holder is RoadVH) {
                with(holder) {
                    val item = getItem(position)
                    if (item is BaseItemRoad.RoadItem) {
                        binding.cbFav.isChecked = item.road.isFavorite
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when {
            holder is RoadVH && item is BaseItemRoad.RoadItem -> holder.bind(item)
            holder is CategoryVH && item is BaseItemRoad.CategoryItem -> holder.bind(item)
        }
    }

    fun setOnCheckedChangeFavoriteListener(callback: (road: Road, isChecked: Boolean) -> Unit) {
        onFavChangeListener = OnCheckedChangeListener(callback)
    }

    private var onFavChangeListener: OnCheckedChangeListener? = null

    private fun interface OnCheckedChangeListener {
        fun setOnCheckedChangeListener(road: Road, isChecked: Boolean)
    }
}