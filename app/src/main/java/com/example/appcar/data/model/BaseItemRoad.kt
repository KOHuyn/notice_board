package com.example.appcar.data.model

/**
 * Created by KO Huyn on 06/12/2021.
 */
sealed class BaseItemRoad {
    data class RoadItem(val road: Road) : BaseItemRoad()
    data class CategoryItem(val carType: CarType) : BaseItemRoad()
}