package com.fullcycle.admin.catalogo.application.castmember.retrieve.list

import com.fullcycle.admin.catalogo.application.UseCase
import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery

abstract class ListCastMembersUseCase : UseCase<SearchQuery, Pagination<CastMemberListOutput>>() {
}