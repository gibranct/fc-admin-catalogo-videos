package com.fullcycle.admin.catalogo.infrastructure.configuration.usecases

import com.fullcycle.admin.catalogo.application.category.create.DefaultCreateCategoryUseCase
import com.fullcycle.admin.catalogo.application.category.delete.DefaultDeleteCategoryUseCase
import com.fullcycle.admin.catalogo.application.category.retrieve.get.DefaultGetCategoryByIdUseCase
import com.fullcycle.admin.catalogo.application.category.retrieve.list.DefaultListCategoriesUseCase
import com.fullcycle.admin.catalogo.application.category.update.DefaultUpdateCategoryUseCase
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CategoryUseCaseConfig(
    private val categoryGateway: CategoryGateway
) {

    @Bean
    fun  createCategoryUseCase(): DefaultCreateCategoryUseCase {
        return DefaultCreateCategoryUseCase(categoryGateway)
    }

    @Bean
    fun  createUpdateCategoryUseCase(): DefaultUpdateCategoryUseCase {
        return DefaultUpdateCategoryUseCase(categoryGateway)
    }

    @Bean
    fun  createGetCategoryByIdUseCase(): DefaultGetCategoryByIdUseCase {
        return DefaultGetCategoryByIdUseCase(categoryGateway)
    }

    @Bean
    fun  createListCategoriesUseCase(): DefaultListCategoriesUseCase {
        return DefaultListCategoriesUseCase(categoryGateway)
    }

    @Bean
    fun  createDeleteCategoryUseCase(): DefaultDeleteCategoryUseCase {
        return DefaultDeleteCategoryUseCase(categoryGateway)
    }

}