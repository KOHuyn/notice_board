package com.example.appcar.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.PropertyName

@Entity
data class NoticeBoard(
    @PrimaryKey
    val id: Long = 0,
    val name: String = "",
    @get:PropertyName("id_ban")
    @set:PropertyName("id_ban")
    var idBan: String = "",
    @get:PropertyName("description")
    @set:PropertyName("description")
    var description: String = "",
    @get:PropertyName("img_src")
    @set:PropertyName("img_src")
    var imgSrc: String = "",
    @get:PropertyName("type_notice")
    @set:PropertyName("type_notice")
    @ColumnInfo(name = "type_notice")
    var typeNotice: Int = 0
)
