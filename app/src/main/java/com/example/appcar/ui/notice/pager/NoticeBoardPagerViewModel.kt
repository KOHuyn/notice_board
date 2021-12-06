package com.example.appcar.ui.notice.pager

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.appcar.data.model.NoticeBoard
import com.example.appcar.data.repository.NoticeBoardRepository
import com.example.appcar.util.BaseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by KO Huyn on 05/12/2021.
 */
@HiltViewModel
class NoticeBoardPagerViewModel @Inject constructor(private val noticeBoardRepository: NoticeBoardRepository) :
    ViewModel() {
    private val noticeLoader =
        MutableSharedFlow<Unit>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val noticeSearch =
        MutableSharedFlow<Pair<Int, String?>>(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )

    init {
        viewModelScope.launch {
            if (noticeBoardRepository.isDbEmpty()) {
                resetNotice()
            }
        }
    }

    fun resetNotice() {
        noticeLoader.tryEmit(Unit)
    }

    val arrNoticeState: LiveData<BaseResponse<List<NoticeBoard>>> = noticeLoader.flatMapLatest {
        noticeBoardRepository.getAllNoticeBoardFromFirebase()
    }.asLiveData()

    val arrNoticeSearch: LiveData<List<NoticeBoard>> =
        noticeSearch.flatMapLatest { (type, search) ->
            if (search.isNullOrEmpty()) {
                noticeBoardRepository.getAllNoticeBoardByType(type)
            } else {
                noticeBoardRepository.getSearchNotice(type, search)
            }
        }.asLiveData()

    fun searchNotice(type: Int, search: String? = null) {
        noticeSearch.tryEmit(type to search)
    }
}