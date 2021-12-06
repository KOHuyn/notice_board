package com.example.appcar.ui.feedback

import androidx.lifecycle.ViewModel
import com.example.appcar.data.repository.FeedbackRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedBackViewModel @Inject constructor(private val feedbackRepository: FeedbackRepository) :
    ViewModel() {
    val state = feedbackRepository.stateSubmitForm

    fun submit(title: String, content: String) {
        feedbackRepository.submitForm(title, content)
    }
}