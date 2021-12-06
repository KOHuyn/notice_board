package com.example.appcar.util

sealed class BaseResponse<out DATA> {
    data class Success<out DATA>(val data: DATA) : BaseResponse<DATA>()

    abstract class StatusVisible<out DATA> : BaseResponse<DATA>() {
        data class Error<out DATA>(val error: Throwable) : StatusVisible<DATA>()
        class Empty<out DATA> : StatusVisible<DATA>()
        class Loading<out DATA> : StatusVisible<DATA>(), BaseLoading
        class LoadMore<out DATA> : StatusVisible<DATA>(), BaseLoading
    }

    abstract class StatusInvisible<out DATA> : BaseResponse<DATA>() {
        class None<out DATA> : StatusInvisible<DATA>()
        class Refresh<out DATA> : StatusInvisible<DATA>(), BaseLoading
    }
}

interface BaseLoading

typealias StatusError<DATA> = BaseResponse.StatusVisible.Error<DATA>
typealias StatusEmpty<DATA> = BaseResponse.StatusVisible.Empty<DATA>
typealias StatusLoading<DATA> = BaseResponse.StatusVisible.Loading<DATA>
typealias StatusLoadMore<DATA> = BaseResponse.StatusVisible.LoadMore<DATA>

typealias StatusNone<DATA> = BaseResponse.StatusInvisible.None<DATA>
typealias StatusRefresh<DATA> = BaseResponse.StatusInvisible.Refresh<DATA>