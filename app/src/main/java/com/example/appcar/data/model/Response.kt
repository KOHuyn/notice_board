package com.example.appcar.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by KO Huyn on 29/10/2021.
 */
data class Response(
    @Expose
    @SerializedName("car_type")
    val varType: List<CarType>? = null,
    @Expose
    @SerializedName("notice_board")
    val noticeBoard: List<NoticeBoard>? = null,
    @Expose
    @SerializedName("road")
    val road: List<Road>? = null,
)