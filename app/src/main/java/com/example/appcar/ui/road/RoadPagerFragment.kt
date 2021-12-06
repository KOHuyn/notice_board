package com.example.appcar.ui.road

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.*
import com.example.appcar.databinding.RoadFragmentPagerBinding
import com.example.appcar.ui.base.BaseFragment
import com.example.appcar.ui.adapter.LoadStateAdapter
import com.example.appcar.ui.road.adapter.RoadAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 * Created by KO Huyn on 29/10/2021.
 */
@AndroidEntryPoint
class RoadPagerFragment : BaseFragment() {

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

    private val isFavorite: Boolean by lazy {
        var isFavoriteZ = false
        lifecycleScope.launchWhenCreated {
            isFavoriteZ = arguments?.getBoolean(Arg.ARG_IS_FAVORITE) ?: false
        }
        isFavoriteZ
    }

    private lateinit var binding: RoadFragmentPagerBinding

    private val vm by activityViewModels<ListRoadViewModel>()

    private val adapter by lazy { RoadAdapter() }
    private val loadStateAdapter by lazy {
        LoadStateAdapter(binding.rcvRoad)
    }

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
        handleClick()
    }

    private fun handleClick() {
        binding.refreshLayout.setOnRefreshListener {
            if (isFavorite) {
                binding.refreshLayout.isRefreshing = false
            } else {
                vm.refresh(true)
                Timber.d("vm.refresh -> refreshLayout.setOnRefreshListener")
            }
        }
        adapter.setOnCheckedChangeFavoriteListener { road, isChecked ->
            vm.addFavorite(road, isChecked)
        }

        loadStateAdapter.setOnEmptyClickListener {
            toast("No")
        }
        loadStateAdapter.setOnRetryClickListener {
            vm.refresh(false)
            Timber.d("vm.refresh -> setOnRetryClickListener")
        }
    }

    private fun setupRcv() {
        val concatAdapter = ConcatAdapter(adapter, loadStateAdapter)
        binding.rcvRoad.adapter = concatAdapter
        binding.rcvRoad.layoutManager = LinearLayoutManager(requireContext())
        loadStateAdapter.attachWithRefreshLayout(binding.refreshLayout)
        binding.refreshLayout.isEnabled = !isFavorite
    }

    private fun loadData() {
        kotlin.run { if (isFavorite) vm.allRoadFav else vm.allRoad }
            .observe(viewLifecycleOwner) { allRoad ->
                adapter.submitList(allRoad)
                if (isFavorite) {
                    loadStateAdapter.showEmptyData(allRoad.isEmpty())
                }
            }
        if (isFavorite.not()) {
            vm.stateResponse.observe(viewLifecycleOwner) { state ->
                loadStateAdapter.setState(state)
            }
        }
    }
}