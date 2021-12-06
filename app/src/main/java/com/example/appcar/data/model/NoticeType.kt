package com.example.appcar.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by KO Huyn on 05/12/2021.
 */
@Entity(tableName = "notice_type")
data class NoticeType(
    @PrimaryKey
    val id: Long = 0,
    val title: String = "",
    val type: Int = 0,
)