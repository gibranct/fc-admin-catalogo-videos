package com.fullcycle.admin.catalogo.infrastructure.configuration.usecases

import com.fullcycle.admin.catalogo.application.genre.create.DefaultCreateGenreUseCase
import com.fullcycle.admin.catalogo.application.genre.delete.DefaultDeleteGenreUseCase
import com.fullcycle.admin.catalogo.application.genre.retrieve.get.DefaultGetGenreByIdUseCase
import com.fullcycle.admin.catalogo.application.genre.retrieve.list.DefaultListGenresUseCase
import com.fullcycle.admin.catalogo.application.genre.update.DefaultUpdateGenreUseCase
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GenreUseCaseConfig(
    private val genreGateway: GenreGateway,
    private val categoryGateway: CategoryGateway
) {

    @Bean
    fun  createGenreUseCase(): DefaultCreateGenreUseCase {
        return DefaultCreateGenreUseCase(genreGateway, categoryGateway)
    }

    @Bean
    fun  createUpdateGenreUseCase(): DefaultUpdateGenreUseCase{
        return DefaultUpdateGenreUseCase(genreGateway, categoryGateway)
    }

    @Bean
    fun  createGetGenreByIdUseCase(): DefaultGetGenreByIdUseCase {
        return DefaultGetGenreByIdUseCase(genreGateway)
    }

    @Bean
    fun  createListGenresUseCase(): DefaultListGenresUseCase {
        return DefaultListGenresUseCase(genreGateway)
    }

    @Bean
    fun  createDeleteGenreUseCase(): DefaultDeleteGenreUseCase {
        return DefaultDeleteGenreUseCase(genreGateway)
    }

}