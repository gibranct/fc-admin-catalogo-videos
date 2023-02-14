package com.fullcycle.admin.catalogo.infrastructure.castmember.presenters

import com.fullcycle.admin.catalogo.application.castmember.retrieve.get.CastMemberOutput
import com.fullcycle.admin.catalogo.application.castmember.retrieve.list.CastMemberListOutput
import com.fullcycle.admin.catalogo.infrastructure.castmember.models.CastMemberListResponse
import com.fullcycle.admin.catalogo.infrastructure.castmember.models.CastMemberResponse


sealed interface CastMemberPresenter {

   companion object {

       fun present(aMember: CastMemberOutput): CastMemberResponse {
           return CastMemberResponse(
               aMember.id,
               aMember.name,
               aMember.type.name,
               aMember.createdAt.toString(),
               aMember.updatedAt.toString()
           )
       }

       fun present(aMember: CastMemberListOutput): CastMemberListResponse {
           return CastMemberListResponse(
               aMember.id,
               aMember.name,
               aMember.type.name,
               aMember.createdAt.toString()
           )
       }

   }

}