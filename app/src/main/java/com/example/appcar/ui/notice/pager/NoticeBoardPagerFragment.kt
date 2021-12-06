package com.example.appcar.ui.notice.pager

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appcar.R
import com.example.appcar.databinding.FragmentNoticeBoardPagerBinding
import com.example.appcar.ui.base.BaseFragment
import com.example.appcar.ui.notice.pager.adapter.NoticeAdapter
import com.example.appcar.ui.adapter.LoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by KO Huyn on 03/12/2021.
 */

@AndroidEntryPoint
class NoticeBoardPagerFragment : BaseFragment() {
    companion object {
        object Arg {
            const val TYPE_NOTICE = "TYPE_NOTICE"
        }

        fun newInstance(typeNotice: Int): NoticeBoardPagerFragment {
            val arg = bundleOf(Arg.TYPE_NOTICE to typeNotice)
            val fragment = NoticeBoardPagerFragment().apply {
                arguments = arg
            }
            return fragment
        }
    }

    private lateinit var binding: FragmentNoticeBoardPagerBinding
    private val vm by viewModels<NoticeBoardPagerViewModel>()
    private val adapter by lazy { NoticeAdapter() }
    private val stateAdapter by lazy { LoadStateAdapter(binding.rcvNotice) }

    private val typeNotice: Int
        get() = arguments?.getInt(Arg.TYPE_NOTICE) ?: -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoticeBoardPagerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRcv()
        updateData()
        handleClick()
        searchNotice()
    }


    private fun searchNotice(search: String? = null) {
        vm.searchNotice(typeNotice, search)
    }

    private fun setupRcv() {
        binding.rcvNotice.layoutManager = LinearLayoutManager(context)
        binding.rcvNotice.setHasFixedSize(true)
        binding.rcvNotice.adapter = ConcatAdapter(stateAdapter, adapter)
    }

    private fun handleClick() {
        stateAdapter.setOnRetryClickListener {
            vm.resetNotice()
        }
        binding.btnSearch.setOnClickListener {
            searchNotice(binding.edtSearch.text.toString())
        }
        binding.btnClearSearch.setOnClickListener {
            binding.edtSearch.editableText.clear()
            binding.edtSearch.clearFocus()
            val imm =
                activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(it.windowToken, 0)
            searchNotice()
        }
    }

    private fun updateData() {
        vm.arrNoticeSearch.observe(viewLifecycleOwner) { arrNotice ->
            binding.txtTotalResult.text =
                getString(R.string.title_result_count_notice_board, arrNotice.size.toString())
            adapter.submitList(arrNotice)
            stateAdapter.showEmptyData(arrNotice.isEmpty())
        }

        vm.arrNoticeState.observe(viewLifecycleOwner) { state ->
            stateAdapter.setState(state)
        }
    }
}