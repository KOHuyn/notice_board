package com.example.appcar.ui.notice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.appcar.data.model.NoticeType
import com.example.appcar.databinding.FragmentNoticeBoardBinding
import com.example.appcar.ui.notice.pager.adapter.NoticeBoardPagerAdapter
import com.example.appcar.ui.base.BaseFragment
import com.example.appcar.util.LoadStateBuilder
import com.example.appcar.util.StatusLoading
import com.example.appcar.util.listeningState
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class NoticeBoardFragment : BaseFragment() {

    companion object {
        fun newInstance() = NoticeBoardFragment()
    }

    private val vm: NoticeBoardViewModel by viewModels()

    private lateinit var binding: FragmentNoticeBoardBinding

    private lateinit var stateHandle: LoadStateBuilder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoticeBoardBinding.inflate(inflater)
        stateHandle = LoadStateBuilder(binding.loadState)
            .setOnRetryClickListener { vm.resetNoticeType() }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelHandle()
    }

    private fun viewModelHandle() {
        lifecycleScope.launchWhenCreated {
            vm.arrNoticeType.collectLatest {
                setupViewPager(it)
            }
        }

        vm.arrNoticeTypeState.observe(viewLifecycleOwner) { state ->
            Timber.e("$state")
            stateHandle.setState(state)
        }
    }

    private fun setupViewPager(arrNoticeType: List<NoticeType>) {
        Timber.i("[###] setupViewPager with $arrNoticeType")
        val adapter = NoticeBoardPagerAdapter(this, arrNoticeType)
        binding.viewpager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            tab.text = arrNoticeType[position].title
        }.attach()
    }
}