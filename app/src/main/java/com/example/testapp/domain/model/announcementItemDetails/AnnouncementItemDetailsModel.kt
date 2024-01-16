package com.example.testapp.domain.model.announcementItemDetails

import com.example.testapp.domain.model.announcement.AnnouncementItemModel

class AnnouncementItemDetailsModel(
    var id: Int = -1,
    var title: String = "",
    var price: String = "",
    var oldPrice: Any? = null,
    var address: String = "",
    var categoryId: Int? = null,
    var categoryName: String = "",
    var totalCategoryName: String = "",
    var districtId: Int? = null,
    var districtName: String = "",
    var coordinateX: String? = null,
    var coordinateY: String? = null,
    var description: String = "",
    var link: String = "https://bisyor.uz",
    var date: String = "",
    var isFavorite: Boolean = false,
    var viewedTotal: Int = 0,
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