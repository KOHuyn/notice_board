package com.example.appcar.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.appcar.R
import com.example.appcar.databinding.ItemLoadStateBinding
import com.example.appcar.util.*
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

/**
 * Created by KO Huyn on 30/11/2021.
 */
class LoadStateAdapter(private val recyclerView: RecyclerView) :
    RecyclerView.Adapter<LoadStateAdapter.LoadStateVH>() {

    private var state: BaseResponse<Any> = StatusNone()
    private var stateLoadingLatest: BaseResponse<Any> = StatusLoading()
    private var refreshLayout: SwipeRefreshLayout? = null

    @DrawableRes
    var srcEmpty: Int? = null
    var textEmpty: String? = recyclerView.context.getString(R.string.data_empty)
    var isShowButtonEmpty: Boolean = false
    var textButtonEmpty: String? = recyclerView.context.getString(R.string.add)

    fun attachWithRefreshLayout(refreshLayout: SwipeRefreshLayout) {
        this.refreshLayout = refreshLayout
    }


    fun setState(stateNew: BaseResponse<Any>) {
        Timber.d("setState $stateNew")
        refreshLayout?.isRefreshing = stateNew is StatusRefresh
        if (stateNew is BaseLoading) {
            stateLoadingLatest = stateNew
        }
        if (stateLoadingLatest is StatusRefresh && stateNew is StatusError) {
            Snackbar.make(recyclerView, stateNew.error.message ?: "", Snackbar.LENGTH_LONG).show()
            state = stateNew
            return
        }
        if (stateNew != state) {
            val oldItem = displayLoadAsItem(state)
            val newItem = displayLoadAsItem(stateNew)

            if (oldItem && !newItem) {
                notifyItemRemoved(0)
            } else if (newItem && !oldItem) {
                notifyItemInserted(0)
            } else if (newItem && oldItem) {
                notifyItemChanged(0)
            }
            state = stateNew
        }
    }

    fun showEmptyData(isEmpty: Boolean) {
        if (isEmpty) {
            setState(StatusEmpty())
        } else {
            setState(StatusNone())
        }
    }

    private fun displayLoadAsItem(item: BaseResponse<Any>): Boolean {
        return item is BaseResponse.StatusVisible
    }

    inner class LoadStateVH(private val binding: ItemLoadStateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: BaseResponse<Any>) {
            val rootLayoutParams = binding.root.layoutParams as RecyclerView.LayoutParams
            rootLayoutParams.height =
                if (stateLoadingLatest is StatusLoading) RecyclerView.LayoutParams.MATCH_PARENT
                else RecyclerView.LayoutParams.WRAP_CONTENT

            binding.progressBar.isVisible = data is StatusLoading || data is StatusLoadMore

            binding.btnRetry.isVisible =
                data is StatusError || (data is StatusEmpty && isShowButtonEmpty)

            val isImageStateVisible = data is StatusEmpty && srcEmpty != null
            binding.imgState.isVisible = isImageStateVisible
            if (isImageStateVisible) {
                Glide.with(binding.imgState)
                    .load(srcEmpty)
                    .into(binding.imgState)
            }

            binding.errorMsg.isVisible =
                data is StatusError || (data is StatusEmpty && textEmpty != null)
            binding.errorMsg.text = when (data) {
                is StatusError -> data.error.message
                is StatusEmpty -> textEmpty
                else -> ""
            }

            binding.btnRetry.text =
                if (data is StatusEmpty) textButtonEmpty else itemView.context.getString(R.string.retry)
            binding.btnRetry.setOnClickListener {
                when (data) {
                    is StatusError -> {
                        onRetryListener?.setOnClickStateListener()
                    }
                    is StatusEmpty -> {
                        onEmptyListener?.setOnClickStateListener()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun interface OnClickState {
        fun setOnClickStateListener()
    }

    private var onRetryListener: OnClickState? = null
    private var onEmptyListener: OnClickState? = null

    fun setOnRetryClickListener(callback: () -> Unit) {
        onRetryListener = OnClickState(callback)
    }

    fun setOnEmptyClickListener(callback: () -> Unit) {
        onEmptyListener = OnClickState(callback)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadStateVH {
        return LoadStateVH(
            ItemLoadStateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LoadStateVH, position: Int) {
        holder.bind(state)
    }

    override fun getItemCount(): Int = if (displayLoadAsItem(state)) 1 else 0
}