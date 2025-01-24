package com.example.testapp.domain.model.createAnnouncement

data class AnnouncementDynamicPropertyModel(
    val name: String,
    val title: String,
    val value: Any? = null,
    val isRequired: Boolean = false,
    val intType: Int = 0,
    val variants: List<Pair<String, String>>? = null,
    var lastSelectedValue: String = "",
    var lastSelectedValueArray: List<Int>? = null,
    val currencyModel: CurrencyModel,
    val districtName: String? = null, // New field
    val categoryName: String? = null, // New field
    val shopName: String? = null,     // New field
    val maxCount: Int? = null,        // New field
    val typeName: String? = null
)
