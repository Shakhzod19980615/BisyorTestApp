package com.example.testapp.domain.use_case

import com.example.testapp.common.Resource
import com.example.testapp.data.remote.dto.CategoryDto
import com.example.testapp.data.remote.dto.CategoryDtoItem
import com.example.testapp.data.remote.dto.toCategoryItem
import com.example.testapp.domain.model.CategoryItem
import com.example.testapp.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
     suspend fun invoke():List<CategoryItem>{
        return repository.getAllMainCategories().map { it.toCategoryItem() }
    }
}