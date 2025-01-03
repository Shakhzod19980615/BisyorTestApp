package com.example.testapp.presentation.searching.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FragmentUniversalListVM @Inject constructor(): ViewModel() {
    // Backing property for itemShowStyle
    private val _itemShowStyle = MutableLiveData(ItemRepresentationStyle.styleGallery)
    val itemShowStyle: LiveData<ItemRepresentationStyle> = _itemShowStyle

    // Method to update the style
    fun updateItemShowStyle(style: ItemRepresentationStyle) {
        _itemShowStyle.value = style
    }
    enum class ItemRepresentationStyle(val value:Int){
        styleGallery(1),
        styleMosaic(2),
        styleList(3);

        companion object{
            fun getInstance(value :Int):ItemRepresentationStyle{
                return when(value){
                    1 -> styleGallery
                    2 -> styleMosaic
                    3 -> styleList
                    else -> styleList
                }
            }
        }

    }
}