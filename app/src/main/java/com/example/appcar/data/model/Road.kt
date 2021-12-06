package com.example.appcar.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.PropertyName
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity
data class Road(
    @PrimaryKey val id: Int = -1,
    var name: String = "",
    @get:PropertyName("start_lat")
    @set:PropertyName("start_lat")
    var startLat: Double = 0.0,
    @get:PropertyName("start_lng")
    @set:PropertyName("start_lng")
    var startLng: Double = 0.0,
    @get:PropertyName("end_lat")
    @set:PropertyName("end_lat")
    var endLat: Double = 0.0,
    @get:PropertyName("end_lng")
    @set:PropertyName("end_lng")
    var endLng: Double = 0.0,
    var description: String = "",
    var type: Int = 0,
    @ColumnInfo(name = "is_favourite") var isFavorite: Boolean = false
) {
}