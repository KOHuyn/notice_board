package com.example.appcar.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CarType(
    @PrimaryKey
    var id: Int = 0,
    var description: String = "",
    var title: String = "",
    var type: Int = 0,
    var isSelected: Boolean = false
)