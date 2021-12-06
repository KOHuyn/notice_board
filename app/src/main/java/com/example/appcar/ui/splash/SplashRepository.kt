package com.example.appcar.ui.splash

import com.example.appcar.data.db.CarTypeDao
import com.example.appcar.data.db.NoticeTypeDao
import com.example.appcar.data.db.RoadDao
import javax.inject.Inject

class SplashRepository @Inject constructor(
    private val roadDao: RoadDao,
    private val carTypeDao: CarTypeDao,
    private val relationDao: NoticeTypeDao
) {
}