package com.example.appcar.ui.notice.pager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appcar.R
import com.example.appcar.data.model.NoticeBoard
import com.example.appcar.databinding.ItemNoticeBinding
import com.example.appcar.util.GlideApp
import com.example.appcar.util.GlideAppModule
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

/**
 * Created by KO Huyn on 03/12/2021.
 */
class NoticeAdapter : ListAdapter<NoticeBoard, NoticeAdapter.NoticeVH>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<NoticeBoard>() {
            override fun areItemsTheSame(oldItem: NoticeBoard, newItem: NoticeBoard): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: NoticeBoard, newItem: NoticeBoard): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class NoticeVH(private val binding: ItemNoticeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NoticeBoard) {
            binding.txtTitleNotice.text = item.name
            binding.txtCodeNotice.text =
                itemView.context.getString(R.string.value_code_of, item.idBan)
            binding.txtDescriptionNotice.text = item.description
            val path: StorageReference =
                Firebase.storage.reference.child("notice").child(item.imgSrc)
            GlideApp.with(itemView.context)
                .load(path)
                .into(binding.imgNotice)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeVH {
        return NoticeVH(
            ItemNoticeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NoticeVH, position: Int) {
        holder.bind(getItem(position))
    }
}