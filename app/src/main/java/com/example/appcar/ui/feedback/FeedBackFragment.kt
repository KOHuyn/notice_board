package com.example.appcar.ui.feedback

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.appcar.R
import com.example.appcar.databinding.FragmentFeedbackBinding
import com.example.appcar.ui.base.BaseFragment
import com.example.appcar.util.BaseResponse
import com.example.appcar.util.StatusError
import com.example.appcar.util.StatusLoading
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeedBackFragment : BaseFragment() {

    companion object {
        fun newInstance() = FeedBackFragment()
    }

    private val vm: FeedBackViewModel by viewModels()

    private lateinit var binding: FragmentFeedbackBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedbackBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
    }

    private fun updateUI() {
        binding.btnSubmit.setOnClickListener {
            val title = binding.edtTitle.text.toString()
            val content = binding.edtContent.text.toString()
            if (title.isNotBlank() && content.isNotBlank()) {
                vm.submit(title, content)
            } else {
                toast("Tiêu đề hoặc nội dung không được để trống")
            }
        }
        lifecycleScope.launch {
            vm.state.collect { state ->
                binding.btnSubmit.isEnabled = state !is StatusLoading
                binding.btnSubmit.alpha = if (state is StatusLoading) 0.5F else 1F
                when (state) {
                    is StatusError -> {
                        toast(state.error.message.toString())
                    }
                    is BaseResponse.Success -> {
                        binding.edtTitle.editableText.clear()
                        binding.edtContent.editableText.clear()
                        toast("Gửi phản hồi thành công")
                    }
                    else -> Unit
                }
            }
        }
    }
}