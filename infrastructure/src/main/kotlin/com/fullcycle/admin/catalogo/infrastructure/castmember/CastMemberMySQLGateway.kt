package com.fullcycle.admin.catalogo.infrastructure.castmember

import com.fullcycle.admin.catalogo.domain.castmember.CastMember
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID
import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery
import com.fullcycle.admin.catalogo.infrastructure.castmember.persistence.CastMemberJpaEntity
import com.fullcycle.admin.catalogo.infrastructure.castmember.persistence.CastMemberRepository
import com.fullcycle.admin.catalogo.infrastructure.utils.SpecificationUtils
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import java.util.Optional


@Component
class CastMemberMySQLGateway(
    private val castMemberRepository: CastMemberRepository
) : CastMemberGateway {


    override fun create(aCastMember: CastMember): CastMember {
        return castMemberRepository.save(CastMemberJpaEntity.from(aCastMember)).toAggregate()
    }

    override fun deleteById(anId: CastMemberID) {
        castMemberRepository.deleteById(anId.value)
    }

    override fun findById(anId: CastMemberID): CastMember? {
        val entityOptional = castMemberRepository.findById(anId.value)
        return when(entityOptional.isPresent) {
            true -> entityOptional.get().toAggregate()
            false -> null
        }
    }

    override fun update(aCastMember: CastMember): CastMember {
        return create(aCastMember)
    }

    override fun findAll(aQuery: SearchQuery): Pagination<CastMember> {
        val page: PageRequest = PageRequest.of(
            aQuery.page,
            aQuery.perPage,
            Sort.by(Sort.Direction.fromString(aQuery.direction), aQuery.sort)
        )

        val where = Optional.ofNullable(aQuery.term)
            .filter { it.isNotBlank()  }
            .map(this::assembleSpecification)
            .orElse(null)

        val pageResult = castMemberRepository.findAll(where, page)

        return Pagination(
            pageResult.number,
            pageResult.size,
            pageResult.totalElements,
            pageResult.map { obj: CastMemberJpaEntity? -> obj!!.toAggregate() }.toList()
        )
    }

    override fun existsById(castMemberIds: Iterable<CastMemberID>): List<CastMemberID> {
        val ids = castMemberIds.map { it.value }
        return castMemberRepository.existsByIds(ids).orEmpty().map { CastMemberID.from(it!!) }
    }

    private fun assembleSpecification(terms: String): Specification<CastMemberJpaEntity> {
        return SpecificationUtils.like("name", terms)
    }
}