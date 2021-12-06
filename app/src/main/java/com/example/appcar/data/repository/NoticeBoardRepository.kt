package com.example.appcar.data.repository

import com.example.appcar.data.anotation.FirebaseNoticeBoardDb
import com.example.appcar.data.db.NoticeBoardDao
import com.example.appcar.data.model.NoticeBoard
import com.example.appcar.util.BaseResponse
import com.example.appcar.util.StatusEmpty
import com.example.appcar.util.StatusError
import com.example.appcar.util.StatusLoading
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by KO Huyn on 03/12/2021.
 */
class NoticeBoardRepository @Inject constructor(
    @FirebaseNoticeBoardDb private val noticeBoardDb: DatabaseReference,
    private val noticeBoardDao: NoticeBoardDao
) {
    fun getAllNoticeBoardFromFirebase() = flow<BaseResponse<List<NoticeBoard>>> {
        emit(StatusLoading())
        try {
            val arrNoticeBoard: List<NoticeBoard> =
                noticeBoardDb.get().await().getValue<List<NoticeBoard?>>()?.filterNotNull()
                    ?: listOf()

            noticeBoardDb.get().addOnSuccessListener {
                Timber.e(it.value.toString())
            }

            if (arrNoticeBoard.isEmpty()) {
                emit(StatusEmpty())
            } else {
                noticeBoardDao.insertAll(arrNoticeBoard)
                emit(BaseResponse.Success(arrNoticeBoard))
            }
        } catch (e: Exception) {
            emit(StatusError(e))
        }
    }

    fun getAllNoticeBoardByType(type: Int) = noticeBoardDao.getNoticeBoardDao(type)

    suspend fun isDbEmpty(): Boolean = noticeBoardDao.isNotEmpty().not()

    suspend fun deleteAll() = noticeBoardDao.deleteAll()

    fun getSearchNotice(type: Int, search: String) =
        noticeBoardDao.searchNotice(type, search)
}