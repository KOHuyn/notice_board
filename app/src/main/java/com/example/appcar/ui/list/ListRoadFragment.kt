package com.example.appcar.ui.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.appcar.R
import com.example.appcar.databinding.ListRoadFragmentBinding
import com.example.appcar.ui.adapter.ViewPagerAdapter
import javax.inject.Inject

class ListRoadFragment : Fragment() {

    companion object {
        fun newInstance() = ListRoadFragment()
    }

//    @Inject
//    private lateinit var viewModel: ListRoadViewModel

    private lateinit var binding: ListRoadFragmentBinding

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