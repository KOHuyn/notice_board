package com.example.appcar.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity
data class Road(
    @Expose
    @PrimaryKey val id: Int,
    @Expose
    var name: String,
    @Expose
    @SerializedName("start_lat")
    var startLat: Double,
    @Expose
    @SerializedName("start_lng")
    var startLng: Double,
    @Expose
    @SerializedName("end_lat")
    var endLat: Double,
    @Expose
    @SerializedName("end_lng")
    var endLng: Double,
    @Expose
    var description: String,
    @Expose
    var type: Int,
    @Expose
    @SerializedName("is_favourite")
    @ColumnInfo(name = "is_favourite") var isFavorite: Boolean
)