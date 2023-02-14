package com.fullcycle.admin.catalogo.application.castmember.retrieve.list

import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway
import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery

class DefaultListCastMembersUseCase(
    private val castMemberGateway: CastMemberGateway
) : ListCastMembersUseCase() {
    override fun execute(anIn: SearchQuery): Pagination<CastMemberListOutput> {
        return castMemberGateway.findAll(anIn)
            .map(CastMemberListOutput::from)
    }
}