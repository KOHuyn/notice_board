package com.example.appcar.ui.road

import androidx.lifecycle.*
import com.example.appcar.R
import com.example.appcar.data.model.BaseItemRoad
import com.example.appcar.data.model.CarType
import com.example.appcar.data.repository.RoadRepository
import com.example.appcar.data.model.Road
import com.example.appcar.data.repository.CarTypeRepository
import com.example.appcar.util.MyApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ListRoadViewModel @Inject constructor(
    private val roadRepository: RoadRepository,
    private val carTypeRepository: CarTypeRepository
) :
    ViewModel() {
    private val _isRefresh =
        MutableSharedFlow<Boolean>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    init {
        Timber.i("[initialize] ListRoadViewModel")
        viewModelScope.launch {
            if (roadRepository.isDbEmpty()) {
                refresh(false)
            }
            if (carTypeRepository.isDbEmpty()) {
                carTypeRepository.getAllFromFirebase().collect()
            }
        }
    }

    val allRoad: LiveData<List<BaseItemRoad>> =
        roadRepository.getAllRoadDb().combineAsBaseItemRoad().asLiveData()

    val allRoadFav: LiveData<List<BaseItemRoad>> =
        roadRepository.getAllRoadFavorite().combineAsBaseItemRoad().asLiveData()

    val stateResponse = _isRefresh.flatMapLatest { isRefresh ->
        roadRepository.updateRoadFromApi(isRefresh)
    }.asLiveData()

    fun refresh(isRefresh: Boolean) {
        Timber.d("[###] retry get road")
        _isRefresh.tryEmit(isRefresh)
    }

    fun addFavorite(road: Road, isFavorite: Boolean) {
        viewModelScope.launch {
            road.isFavorite = isFavorite
            roadRepository.updateRoad(road)
        }
    }

    private fun Flow<List<Road>>.combineAsBaseItemRoad(): Flow<List<BaseItemRoad>> {
        return this.combine(carTypeRepository.getAllCarType()) { roads, carTypes ->
            val arrRoadBase = mutableListOf<BaseItemRoad>()
            carTypes.forEach { carType ->
                val itemsRoad =
                    roads.filter { it.type == carType.type }.map { BaseItemRoad.RoadItem(it) }
                if (itemsRoad.isNotEmpty()) {
                    arrRoadBase.add(BaseItemRoad.CategoryItem(carType))
                    arrRoadBase.addAll(itemsRoad)
                }
            }
            val otherItems =
                roads.filterNot { carTypes.map { car -> car.type }.contains(it.type) }
                    .map { BaseItemRoad.RoadItem(it) }
            if (otherItems.isNotEmpty()) {
                arrRoadBase.add(
                    BaseItemRoad.CategoryItem(
                        CarType(
                            description = MyApplication.INSTANCE.applicationContext
                                .getString(R.string.label_other)
                        )
                    )
                )
                arrRoadBase.addAll(otherItems)
            }
            arrRoadBase.toList()
        }
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("[onCleared] ListRoadViewModel")
    }
}