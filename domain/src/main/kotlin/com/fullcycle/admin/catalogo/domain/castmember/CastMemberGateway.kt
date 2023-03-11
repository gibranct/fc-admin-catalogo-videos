package com.fullcycle.admin.catalogo.domain.castmember

import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery

interface CastMemberGateway {

    fun create(aCastMember: CastMember): CastMember

    fun deleteById(anId: CastMemberID)

    fun findById(anId: CastMemberID): CastMember?

    fun update(aCastMember: CastMember): CastMember

    fun findAll(aQuery: SearchQuery): Pagination<CastMember>

    fun existsByIds(castMemberIds: Iterable<CastMemberID>): List<CastMemberID>
}