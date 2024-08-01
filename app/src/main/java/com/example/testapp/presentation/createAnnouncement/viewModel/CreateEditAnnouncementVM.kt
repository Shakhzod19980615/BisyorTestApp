package com.example.testapp.presentation.createAnnouncement.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.domain.use_case.activeCategoryUseCase.ActiveCategoryUseCase
import com.example.testapp.presentation.createAnnouncement.adapter.CreateAnnouncementImage
import com.example.testapp.presentation.createAnnouncement.adapter.UploadState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateEditAnnouncementVM @Inject constructor(

): ViewModel() {

    private val _isUserActive = MutableStateFlow(true) // Initial state
    val isUserActive: StateFlow<Boolean> get() = _isUserActive.asStateFlow()
    private val _selectedImages = MutableStateFlow<List<CreateAnnouncementImage>>(emptyList())
    val selectedImages: StateFlow<List<CreateAnnouncementImage>> = _selectedImages

    fun addImages(uris: List<Uri>) {
        _selectedImages.update { currentImages ->
            val newImages = uris.map { CreateAnnouncementImage(it, UploadState.UPLOADED) }
            if (currentImages.size + newImages.size > 8) {
                val availableSlots = 8 - currentImages.size
                currentImages + newImages.take(availableSlots)
            } else {
                currentImages + newImages
            }
        }
    }
    fun removeImage(image: CreateAnnouncementImage) {
        _selectedImages.update { currentImages ->
            currentImages.toMutableList().apply {
                remove(image)
            }
        }
    }
    fun setActiveSegment(isActive: Boolean) {
        viewModelScope.launch {
            _isUserActive.emit(isActive)
        }
    }


}