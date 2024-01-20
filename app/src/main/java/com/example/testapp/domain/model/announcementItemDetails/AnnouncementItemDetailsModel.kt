package com.example.testapp.domain.model.announcementItemDetails

import com.example.testapp.domain.model.announcement.AnnouncementItemModel

class AnnouncementItemDetailsModel(
    var id: Int,
    var title: String,
    var price: String ,
    var oldPrice: Any?,
    var address: String ,
    var categoryId: Int?,
    var categoryName: String?,
    var totalCategoryName: String,
    var districtId: Int?,
    var districtName: String,
    var coordinateX: String?,
    var coordinateY: String?,
    var description: String,
    var link: String = "https://bisyor.uz",
    var date: String?,
    var isFavorite: Boolean = false,
    var viewedTotal: Int? ,
    var imgCount: Int = 0,
    var images: ArrayList<String> = ArrayList(),
    var properties: List<ItemDynamicPropertyModel> = ArrayList(),

    var userId: Int = 0,
    var storeId: Any? = null,
    var storeName: String? = null,
    var userName: String = "",
    var userAvatar: String? = null,
    var phones: List<String>? = null,
    var isUserOnline: Boolean = false,
    var similarItems: List<AnnouncementItemModel>?= null,
    var storeItems: List<AnnouncementItemModel>?= null
)