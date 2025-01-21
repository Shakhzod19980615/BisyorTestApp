package com.example.testapp.presentation.createAnnouncement.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.common.ErrorParser
import com.example.testapp.common.Resource
import com.example.testapp.data.request.createAnnouncement.AnnouncementPropertiesRequest
import com.example.testapp.domain.model.createAnnouncement.AnnouncementDynamicPropertyModel
import com.example.testapp.domain.use_case.createAnnouncement.CreateAnnouncementUseCase
import com.example.testapp.presentation.createAnnouncement.adapter.CreateAnnouncementImage
import com.example.testapp.presentation.createAnnouncement.adapter.UploadState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CreateEditAnnouncementVM @Inject constructor(
    private val createAnnouncementUseCase: CreateAnnouncementUseCase,
    private val errorParser: ErrorParser
): ViewModel() {

    private val _isUserActive = MutableStateFlow(true) // Initial state
    val isUserActive: StateFlow<Boolean> get() = _isUserActive.asStateFlow()
    private val _selectedImages = MutableStateFlow<List<CreateAnnouncementImage>>(emptyList())
    val selectedImages: StateFlow<List<CreateAnnouncementImage>> = _selectedImages
    private val _dynamicProperties = MutableStateFlow<Resource<List<AnnouncementDynamicPropertyModel>>>(Resource.Loading())
    val dynamicProperties: StateFlow<Resource<List<AnnouncementDynamicPropertyModel>>> = _dynamicProperties
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
  fun getItemFields(categoryId:Int,id:Int){
      val request = AnnouncementPropertiesRequest(lang = "uz",
          categoryId = categoryId, editableAnnouncementId = id)
      viewModelScope.launch {
          withContext(Dispatchers.IO){
              kotlin.runCatching {
                  createAnnouncementUseCase.getAnnouncementDynamicProperties(request)
              }.onSuccess {
                  _dynamicProperties.value = Resource.Success(it)
                  Log.d("SuccessDebug", "Response: $it")
              }.onFailure{throwable ->
                  Log.e("ErrorDebug", "Error occurred: ${throwable.message}", throwable)
                  when (throwable) {
                      is retrofit2.HttpException -> {
                          val errorResponse: Response<*>? = throwable.response()
                          if (errorResponse?.errorBody() != null) {
                              val parsedError = errorParser.parseError(errorResponse)
                              Log.e("HttpErrorDebug", "Response Code: ${errorResponse?.code()}")
                              if (parsedError != null) {
                                  Log.e("ParsedErrorDebug", "Parsed Error: ${parsedError.message}")
                                  _dynamicProperties.value = Resource.Error(parsedError.message)
                              } else {
                                  _dynamicProperties.value = Resource.Error("Unknown error")
                              }
                          } else {
                              _dynamicProperties.value = Resource.Error("Unknown error")
                          }
                      }
                  }

              }
          }
      }

  }


}