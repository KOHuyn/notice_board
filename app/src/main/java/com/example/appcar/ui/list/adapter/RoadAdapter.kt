package com.example.appcar.ui.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appcar.data.model.Road
import com.example.appcar.databinding.ItemRoadListBinding

/**
 * Created by KO Huyn on 29/10/2021.
 */
class RoadAdapter : RecyclerView.Adapter<RoadAdapter.RoadVH>() {

    var items = mutableListOf<Road>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class RoadVH(val binding: ItemRoadListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoadVH {
        return RoadVH(
            ItemRoadListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RoadVH, position: Int) {
        val item = items[position]
        with(holder) {
            binding.txtTitle.text = item.name
            binding.txtDescription.text = item.description
        }
    }

    override fun getItemCount(): Int = items.size
}