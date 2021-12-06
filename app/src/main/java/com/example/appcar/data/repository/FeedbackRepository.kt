package com.example.appcar.data.repository

import com.example.appcar.data.anotation.FirebaseFeedbackDb
import com.example.appcar.util.BaseResponse
import com.example.appcar.util.StatusError
import com.example.appcar.util.StatusLoading
import com.example.appcar.util.StatusNone
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by KO Huyn on 06/12/2021.
 */
class FeedbackRepository @Inject constructor(@FirebaseFeedbackDb val feedbackDb: DatabaseReference) {

    init {
        Timber.i("[initialize] FeedbackRepository")
    }
    val stateSubmitForm = MutableStateFlow<BaseResponse<Any>>(StatusNone())

    fun submitForm(title: String, content: String) {
        stateSubmitForm.value = StatusLoading()
        feedbackDb.child(title).updateChildren(mapOf("title" to title, "content" to content))
            .addOnSuccessListener {
                stateSubmitForm.value = BaseResponse.Success(it)
            }.addOnFailureListener {
                stateSubmitForm.value = StatusError(it)
            }
    }
}