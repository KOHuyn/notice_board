package com.example.appcar.ui.map

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.appcar.R
import com.example.appcar.data.model.Road
import com.example.appcar.databinding.MapFragmentBinding
import com.example.appcar.ui.map.adapter.ItemCarTypeAdapter
import com.example.appcar.ui.map.adapter.ItemMapRoadAdapter
import com.example.appcar.ui.base.BaseFragment
import com.example.appcar.util.LoadStateBuilder
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MapFragment : BaseFragment(), OnMapReadyCallback {
    private val roadAdapter by lazy { ItemMapRoadAdapter() }
    private val carTypeAdapter by lazy { ItemCarTypeAdapter() }

    private lateinit var googleMap: GoogleMap

    private lateinit var binding: MapFragmentBinding
    private lateinit var stateHandle: LoadStateBuilder

    companion object {
        fun newInstance() = MapFragment()
    }

    private val vm: MapViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MapFragmentBinding.inflate(inflater)
        stateHandle = LoadStateBuilder(binding.loadState).setOnRetryClickListener { vm.retryAll() }
        val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.mapContainer) as SupportMapFragment
        supportMapFragment.getMapAsync(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        handleClick()
        observerViewModel()
    }

    private fun setUpViews() {
        binding.rlvListRoad.adapter = roadAdapter
        val startSnapHelper: SnapHelper = PagerSnapHelper()
        startSnapHelper.attachToRecyclerView(binding.rlvListRoad)
        binding.rlvListType.adapter = carTypeAdapter
    }

    private fun handleClick() {
        roadAdapter.setOnItemClickListener {
            onClickRoadItem(it)
        }

        binding.btnSearch.setOnClickListener {
            search()
        }
        binding.btnClearSearch.setOnClickListener {
            binding.edtSearch.editableText.clear()
            binding.edtSearch.clearFocus()
            val imm =
                activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(it.windowToken, 0)
            search()
        }

        carTypeAdapter.setOnCarTypeChangeListener {
            search()
        }

        roadAdapter.setOnItemFavoriteListener {
            vm.favRoad(it)
        }

        binding.edtSearch.doOnTextChanged { text, start, before, count ->
            binding.btnSearch.alpha = if (text.isNullOrBlank()) 0.5F else 1F
            binding.btnSearch.isEnabled = text?.isNotBlank() ?: false
        }
    }

    private fun search() {
        var search: String? = binding.edtSearch.text?.toString()
        if (search.isNullOrBlank()) {
            search = null
        }
        vm.searchRoad(carTypeAdapter.arrTypeSelected, search)
    }

    private fun observerViewModel() {
        lifecycleScope.launchWhenCreated {
            vm.arrCarType.collect {
                carTypeAdapter.submitList(it)
            }
        }
        vm.allRoadByFilter.observe(viewLifecycleOwner) {
            roadAdapter.submitList(it)
        }
        vm.stateData.observe(viewLifecycleOwner) {
            stateHandle.setState(it)
        }
    }

    private fun onClickRoadItem(road: Road) {
        val position = LatLng(road.startLat, road.startLng)
        val markerOptions = MarkerOptions()
        markerOptions.position(position)
        markerOptions.title(road.name)
        googleMap.addMarker(markerOptions)
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15F))
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    21.022736, 105.8019441
                ), 15F
            )
        )
    }

}