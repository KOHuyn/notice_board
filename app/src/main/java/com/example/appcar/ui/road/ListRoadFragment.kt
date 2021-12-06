package com.example.appcar.ui.road

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.appcar.databinding.ListRoadFragmentBinding
import com.example.appcar.ui.adapter.ViewPagerAdapter
import com.example.appcar.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListRoadFragment : BaseFragment() {

    companion object {
        fun newInstance() = ListRoadFragment()
    }

    private lateinit var binding: ListRoadFragmentBinding

    val viewModel: ListRoadViewModel by viewModels()

    private val adapter by lazy {
        ViewPagerAdapter(
            childFragmentManager,
            listOf(
                RoadPagerFragment.newInstance(false),
                RoadPagerFragment.newInstance(true)
            ),
            listOf("Đường cấm", "Danh sách của tôi")
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListRoadFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
    }

    private fun updateUI() {
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = adapter.count
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }
}