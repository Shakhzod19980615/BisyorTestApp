package com.example.testapp.domain.use_case.favourite

import com.example.testapp.domain.model.announcement.AnnouncementItemModel
import com.example.testapp.domain.repository.favourite.FavouriteRepository
import javax.inject.Inject

class UserRemoteFavoritesUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {
    suspend fun getFavouriteItems(lang:String,page:Int) :List<AnnouncementItemModel> {
       return repository.getFavouriteItems(lang,page)
    }
}