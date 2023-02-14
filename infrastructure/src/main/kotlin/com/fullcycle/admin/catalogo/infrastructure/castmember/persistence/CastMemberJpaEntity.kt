package com.fullcycle.admin.catalogo.infrastructure.castmember.persistence

import com.fullcycle.admin.catalogo.domain.castmember.CastMember
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "CastMember")
@Table(name = "cast_members")
class CastMemberJpaEntity private constructor(
    @Id val id: String,
    @Column(name = "name", nullable = false) val name: String,
    @Enumerated(EnumType.STRING) @Column(name = "type", nullable = false) private var type: CastMemberType,
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)") val createdAt: Instant,
    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)") val updatedAt: Instant,
){


    fun toAggregate(): CastMember {
        return CastMember.with(
            CastMemberID.from(id),
            name,
            type,
            createdAt,
            updatedAt
        )
    }

    companion object {
        fun from(aMember: CastMember): CastMemberJpaEntity {
            return CastMemberJpaEntity(
                aMember.id.value,
                aMember.name,
                aMember.type,
                aMember.createdAt,
                aMember.updatedAt
            )
        }
    }


}