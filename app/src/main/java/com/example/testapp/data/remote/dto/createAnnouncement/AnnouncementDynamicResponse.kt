package com.example.testapp.data.remote.dto.createAnnouncement

import com.example.testapp.common.util.MyUtil
import com.example.testapp.domain.model.createAnnouncement.AnnouncementDynamicPropertyModel
import com.example.testapp.domain.model.createAnnouncement.CurrencyModel

data class AnnouncementDynamicResponse(
    val name: String,
    val title: String,
    val value: Any? = null,
    val type: Int,
    val typeName: String? = null,
    val required:Int,
    val variants:Map<String,String>,
    val districtName: String? = null,
    val categoryName: String? = null,
    val shopName: String? = null,
    val maxCount: Int? = null,
    val currencyList:CurrencyResponse
)

fun AnnouncementDynamicResponse.toAnnouncementDynamicPropertyModel() = AnnouncementDynamicPropertyModel(
    name = name,
    title = title,
    value = value,
    isRequired = required==1,
    intType = type,
    variants = variants.toList(),
    typeName = typeName,
    districtName = districtName,
    categoryName = categoryName,
    shopName = shopName,
    maxCount = maxCount,
    currencyModel = currencyList.toCurrencyModel(),
    lastSelectedValue= this.value.let {
        if (type == 1 || type == 2 || type == 10) {
            when (it) {
                is Int -> it.toString()
                is Double -> it.toString()
                else -> it as String
            }
        } else if (type == 4 || type == 5) {
            it as String
        } else if (type == 6 || type == 8 || type == 11) {
            val key = (it as Double).toInt().toString()
            val charSequenceList: Array<String> =
                MyUtil.pairListKeys(variants.toList())
            val result = charSequenceList.indexOf(key).toString()
            if (result != "-1") result else ""
        } else
            ""
    },
    lastSelectedValueArray = this.value.let {
        if (type == 9 && (it is List<*>).not()) {
            val keyValues = it as Map<*, *>
            val keys = keyValues.keys

            val charSequenceList: Array<String> =
                MyUtil.pairListValues(variants.toList())

            val lastValue = mutableListOf<Int>()
            charSequenceList.forEachIndexed { index, s ->
                if (keys.contains(s))
                    lastValue.add(index)
            }

            lastValue.ifEmpty { null }
        } else
            null
    }

)
fun CurrencyResponse.toCurrencyModel() = CurrencyModel(currency = value)
