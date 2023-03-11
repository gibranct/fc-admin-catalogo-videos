package com.fullcycle.admin.catalogo.infrastructure.configuration.usecases

import com.fullcycle.admin.catalogo.application.castmember.create.CreateCastMemberUseCase
import com.fullcycle.admin.catalogo.application.castmember.create.DefaultCreateCastMemberUseCase
import com.fullcycle.admin.catalogo.application.castmember.delete.DefaultDeleteCastMemberUseCase
import com.fullcycle.admin.catalogo.application.castmember.delete.DeleteCastMemberUseCase
import com.fullcycle.admin.catalogo.application.castmember.retrieve.get.DefaultGetCastMemberByIdUseCase
import com.fullcycle.admin.catalogo.application.castmember.retrieve.get.GetCastMemberByIdUseCase
import com.fullcycle.admin.catalogo.application.castmember.retrieve.list.DefaultListCastMembersUseCase
import com.fullcycle.admin.catalogo.application.castmember.retrieve.list.ListCastMembersUseCase
import com.fullcycle.admin.catalogo.application.castmember.update.DefaultUpdateCastMemberUseCase
import com.fullcycle.admin.catalogo.application.castmember.update.UpdateCastMemberUseCase
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class CastMemberUseCaseConfig(
    private val castMemberGateway: CastMemberGateway,
) {

    @Bean
    fun createCastMemberUseCase(): CreateCastMemberUseCase {
        return DefaultCreateCastMemberUseCase(castMemberGateway)
    }

    @Bean
    fun deleteCastMemberUseCase(): DeleteCastMemberUseCase {
        return DefaultDeleteCastMemberUseCase(castMemberGateway)
    }

    @Bean
    fun getCastMemberByIdUseCase(): GetCastMemberByIdUseCase {
        return DefaultGetCastMemberByIdUseCase(castMemberGateway)
    }

    @Bean
    fun listCastMembersUseCase(): ListCastMembersUseCase {
        return DefaultListCastMembersUseCase(castMemberGateway)
    }

    @Bean
    fun updateCastMemberUseCase(): UpdateCastMemberUseCase {
        return DefaultUpdateCastMemberUseCase(castMemberGateway)
    }

}