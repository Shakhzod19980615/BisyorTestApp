package com.example.testapp.data.remote.dto.announcementItemDetails

import com.example.testapp.domain.model.announcementItemDetails.ItemDynamicPropertyModel

data class DynamicField(
    val title: String,
    val type: Int,
    val typeName: String,
    val value: Any
)
fun DynamicField.toItemDynamicPropertyModel() = ItemDynamicPropertyModel(
    title = title,
    type = type,
    typeName = typeName,
    value = value
)