package com.example.appcar.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appcar.databinding.RoadFragmentPagerBinding
import com.example.appcar.ui.list.adapter.RoadAdapter
import com.example.appcar.util.getRepo

/**
 * Created by KO Huyn on 29/10/2021.
 */
class RoadPagerFragment : Fragment() {

    companion object {
        private object Arg {
            const val ARG_IS_FAVORITE = "ARG_IS_FAVORITE"
        }

        fun newInstance(isFavorite: Boolean): RoadPagerFragment {
            val bundle = bundleOf(Pair(Arg.ARG_IS_FAVORITE, isFavorite))
            return RoadPagerFragment().apply {
                arguments = bundle
            }
        }
    }

    private lateinit var binding: RoadFragmentPagerBinding

    private val adapter by lazy { RoadAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RoadFragmentPagerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
    }

    private fun updateUI() {
        setupRcv()
        loadData()
    }

    private fun setupRcv() {
        binding.rcvRoad.adapter = adapter
        binding.rcvRoad.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun loadData() {
        getRepo()?.road?.let { road ->
            adapter.items = road.toMutableList()
        }
    }
}