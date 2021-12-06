package com.example.appcar.di

import com.example.appcar.data.anotation.*
import com.example.appcar.data.firebase.DbConstants
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by KO Huyn on 03/12/2021.
 */
@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return Firebase.database
    }

    @Provides
    @FirebaseRoadDb
    fun provideFirebaseDatabaseRoad(firebase: FirebaseDatabase): DatabaseReference {
        return firebase.getReference(DbConstants.DbName.ROAD_DB)
    }

    @Provides
    @FirebaseNoticeBoardDb
    fun provideFirebaseDatabaseNoticeBoard(firebase: FirebaseDatabase): DatabaseReference {
        return firebase.getReference(DbConstants.DbName.NOTICE_BOARD_DB)
    }

    @Provides
    @FirebaseCarTypeDb
    fun provideFirebaseDatabaseCarType(firebase: FirebaseDatabase): DatabaseReference {
        return firebase.getReference(DbConstants.DbName.CAR_TYPE_DB)
    }

    @Provides
    @FirebaseFeedbackDb
    fun provideFirebaseDatabaseFeedback(firebase: FirebaseDatabase): DatabaseReference {
        return firebase.getReference(DbConstants.DbName.FEEDBACK_DB)
    }
    @Provides
    @FirebaseNoticeTypeDb
    fun provideFirebaseDatabaseNoticeType(firebase: FirebaseDatabase): DatabaseReference {
        return firebase.getReference(DbConstants.DbName.NOTICE_TYPE_DB)
    }
}