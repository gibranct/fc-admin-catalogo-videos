package com.fullcycle.admin.catalogo.infrastructure.api.controllers

import com.fullcycle.admin.catalogo.application.castmember.create.CreateCastMemberCommand
import com.fullcycle.admin.catalogo.application.castmember.create.CreateCastMemberUseCase
import com.fullcycle.admin.catalogo.application.castmember.delete.DeleteCastMemberUseCase
import com.fullcycle.admin.catalogo.application.castmember.retrieve.get.GetCastMemberByIdUseCase
import com.fullcycle.admin.catalogo.application.castmember.retrieve.list.ListCastMembersUseCase
import com.fullcycle.admin.catalogo.application.castmember.update.UpdateCastMemberCommand
import com.fullcycle.admin.catalogo.application.castmember.update.UpdateCastMemberUseCase
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType
import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery
import com.fullcycle.admin.catalogo.infrastructure.api.CastMemberAPI
import com.fullcycle.admin.catalogo.infrastructure.castmember.models.CastMemberListResponse
import com.fullcycle.admin.catalogo.infrastructure.castmember.models.CastMemberResponse
import com.fullcycle.admin.catalogo.infrastructure.castmember.models.CreateCastMemberRequest
import com.fullcycle.admin.catalogo.infrastructure.castmember.models.UpdateCastMemberRequest
import com.fullcycle.admin.catalogo.infrastructure.castmember.presenters.CastMemberPresenter
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class CastMemberController(
    private val createCastMemberUseCase: CreateCastMemberUseCase,
    private val getCastMemberByIdUseCase: GetCastMemberByIdUseCase,
    private val updateCastMemberUseCase: UpdateCastMemberUseCase,
    private val deleteCastMemberUseCase: DeleteCastMemberUseCase,
    private val listCastMembersUseCase: ListCastMembersUseCase
) : CastMemberAPI {

    override fun create(input: CreateCastMemberRequest): ResponseEntity<*> {
        val outputEither = createCastMemberUseCase.execute(
            CreateCastMemberCommand.with(
                aName = input.name,
                aType = CastMemberType.valueOf(input.type),
            )
        )
        return outputEither
            .fold(
                { ResponseEntity.unprocessableEntity().body(it) },
                { ResponseEntity.created(URI.create("/cast_members/${it.id}")).body(it) }
            )
    }

    override fun list(
        search: String?,
        page: Int?,
        perPage: Int?,
        sort: String?,
        direction: String?
    ): Pagination<CastMemberListResponse> {
        return listCastMembersUseCase.execute(
            SearchQuery(
                page = page ?: 0,
                perPage = perPage ?: 10,
                term = search ?: "",
                sort = sort ?: "name",
                direction = direction ?: "asc",
            )
        ).map(CastMemberPresenter::present)
    }

    override fun getById(id: String): CastMemberResponse {
       return CastMemberPresenter.present(getCastMemberByIdUseCase.execute(id))
    }

    override fun updateById(id: String, aBody: UpdateCastMemberRequest): ResponseEntity<*> {
        return updateCastMemberUseCase.execute(UpdateCastMemberCommand(
            id = id,
            name = aBody.name,
            type = CastMemberType.valueOf(aBody.type)
        )).fold(
            { ResponseEntity.unprocessableEntity().body(it) },
            { ResponseEntity.ok().body(it) }
        )
    }

    override fun deleteById(id: String) {
        deleteCastMemberUseCase.execute(id)
    }
}