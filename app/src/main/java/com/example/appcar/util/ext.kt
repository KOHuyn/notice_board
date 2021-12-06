package com.example.appcar.util

import android.content.Context
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.appcar.R
import com.example.appcar.data.model.Response
import com.example.appcar.databinding.ItemLoadStateBinding
import com.google.gson.Gson
import java.io.IOException
import java.nio.charset.StandardCharsets

/**
 * Created by KO Huyn on 29/10/2021.
 */

@Throws(IOException::class)
fun loadJSONFromAsset(context: Context, jsonFileName: String): String {
    val manager = context.assets
    val inputStream = manager.open(jsonFileName)
    val size = inputStream.available()
    val buffer = ByteArray(size)
    inputStream.read(buffer)
    inputStream.close()
    return String(buffer, StandardCharsets.UTF_8)
}

fun Fragment.getRepo(): Response? {
    val json = loadJSONFromAsset(requireContext(), "notice_board.json")
    return try {
        Gson().fromJson(json, Response::class.java)
    } catch (e: Exception) {
        null
    }
}

class LoadStateBuilder(private val binding: ItemLoadStateBinding) {
    private var onButtonRetryClick: View.OnClickListener? = null

    init {
        setState(StatusNone())
    }

    fun setState(data: BaseResponse<Any>): LoadStateBuilder {
        binding.root.isVisible = data is BaseResponse.StatusVisible
        binding.progressBar.isVisible = data is StatusLoading

        binding.errorMsg.isVisible =
            data is StatusError
        binding.errorMsg.text = when (data) {
            is StatusError -> data.error.message
            else -> ""
        }

        binding.btnRetry.isVisible =
            data is StatusError
        binding.btnRetry.text =
            binding.btnRetry.context.getString(R.string.retry)
        binding.btnRetry.setOnClickListener(onButtonRetryClick)
        return this
    }

    fun setOnRetryClickListener(callback: () -> Unit): LoadStateBuilder {
        onButtonRetryClick = View.OnClickListener {
            callback.invoke()
        }
        return this
    }
}

fun ItemLoadStateBinding.listeningState(data: BaseResponse<Any>, buttonClick: () -> Unit) {
    val binding = this

}