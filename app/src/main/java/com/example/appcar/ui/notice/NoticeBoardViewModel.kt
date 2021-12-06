package com.example.appcar.ui.notice

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.appcar.data.model.NoticeType
import com.example.appcar.data.repository.NoticeTypeRepository
import com.example.appcar.util.BaseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoticeBoardViewModel @Inject constructor(
    private val noticeTypeRepository: NoticeTypeRepository
) : ViewModel() {

    private val noticeTypeLoader =
        MutableSharedFlow<Unit>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    init {
        viewModelScope.launch {
            if (noticeTypeRepository.isDbEmpty()) {
                resetNoticeType()
            }
        }
    }

    fun resetNoticeType() {
        noticeTypeLoader.tryEmit(Unit)
    }


    val arrNoticeTypeState: LiveData<BaseResponse<List<NoticeType>>> =
        noticeTypeLoader.flatMapLatest {
            noticeTypeRepository.getAllFromFirebase()
        }.asLiveData()


    val arrNoticeType = noticeTypeRepository.getAllNoticeType()

}