package com.example.testapp.presentation.announcementDetail.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.common.Resource
import com.example.testapp.domain.model.announcementItemDetails.AnnouncementItemDetailsModel
import com.example.testapp.domain.use_case.announcementDetails.GetAnnouncementDetailsUseCase
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AnnouncementDetailViewModel @Inject constructor(
    private val getAnnouncementDetailsUseCase: GetAnnouncementDetailsUseCase
): ViewModel() {
    private val _announcementDetail = MutableStateFlow<Resource<AnnouncementItemDetailsModel>>(Resource.Loading())
    val announcementDetail: StateFlow<Resource<AnnouncementItemDetailsModel>> get() = _announcementDetail.asStateFlow()

    fun getAnnouncementDetails(itemId: Int){
        viewModelScope.launch {
            try {
                val announcementDetail = getAnnouncementDetailsUseCase.invoke(itemId = itemId)
                _announcementDetail.value = Resource.Success(announcementDetail)
            } catch (e: HttpException) {
                (Resource.Error( "An unexpected error occured", null))
            }catch (e: IOException) {
                (Resource.Error("Couldn't reach server. Check your internet connection.", null))
            }
        }
    }
}