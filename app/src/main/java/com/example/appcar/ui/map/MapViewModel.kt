package com.example.appcar.ui.map

import androidx.lifecycle.*
import com.example.appcar.data.model.Road
import com.example.appcar.data.repository.CarTypeRepository
import com.example.appcar.data.repository.RoadRepository
import com.example.appcar.util.BaseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val roadRepository: RoadRepository,
    private val carTypeRepository: CarTypeRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            if (roadRepository.isDbEmpty() || carTypeRepository.isDbEmpty()) {
                retryAll()
            }
        }
    }

    val arrCarType = carTypeRepository.getAllCarType()

    fun retryAll() {
        _stateRoad.value = Unit
    }

    private val _stateRoad = MutableLiveData<Unit>()

    val stateData: LiveData<BaseResponse<Any>> = _stateRoad.switchMap {
        viewModelScope.launch {
            carTypeRepository.getAllFromFirebase().collect()
        }
        roadRepository.updateRoadFromApi(false).asLiveData()
    }

    private val _stateFilterRoad =
        MutableLiveData<Pair<List<Int>, String?>>()

    fun searchRoad(types: List<Int>, search: String?) {
        Timber.e("SEARCH : ${types.joinToString(",")} AND $search")
        _stateFilterRoad.value = (types to search)
    }

    fun favRoad(road: Road) {
        viewModelScope.launch {
            roadRepository.updateRoad(road)
        }
    }

    val allRoadByFilter = _stateFilterRoad.switchMap { (type, search) ->
        Timber.e("[###] types -> ${type.joinToString(",")}")
        roadRepository.searchRoad(type, search).asLiveData()
    }
}