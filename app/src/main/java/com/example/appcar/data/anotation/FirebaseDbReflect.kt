package com.example.appcar.data.anotation

import javax.inject.Qualifier

/**
 * Created by KO Huyn on 03/12/2021.
 */

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FirebaseRoadDb

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FirebaseCarTypeDb

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FirebaseNoticeBoardDb

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FirebaseFeedbackDb

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FirebaseNoticeTypeDb