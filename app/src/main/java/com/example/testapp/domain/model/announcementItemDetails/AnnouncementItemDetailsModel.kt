package com.example.testapp.domain.model.announcementItemDetails

import com.example.testapp.domain.model.announcement.AnnouncementItemModel

class AnnouncementItemDetailsModel(
    var id: Int? = null,
    var title: String? = null,
    var price: String? = null,
    var oldPrice: Any? = null,
    var address: String? = null,
    var categoryId: Int? = null,
    var categoryName: String? = null,
    var totalCategoryName: String? = null,
    var districtId: Int? = null,
    var districtName: String? = null,
    var coordinateX: String? = null,
    var coordinateY: String? = null,
    var description: String? = null,
    var link: String = "https://bisyor.uz",
    var date: String? = null,
    var isFavorite: Boolean = false,
    var viewedTotal: Int? = null,
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
    var similarItems: List<AnnouncementItemModel>? = null,
    var storeItems: List<AnnouncementItemModel>? = null
)
