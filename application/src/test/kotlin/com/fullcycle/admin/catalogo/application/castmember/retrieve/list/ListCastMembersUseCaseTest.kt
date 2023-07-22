package com.fullcycle.admin.catalogo.application.castmember.retrieve.list

import com.fullcycle.admin.catalogo.application.Fixture
import com.fullcycle.admin.catalogo.application.UseCaseTest
import com.fullcycle.admin.catalogo.domain.castmember.CastMember
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway
import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock

internal class ListCastMembersUseCaseTest : UseCaseTest() {

    @InjectMocks
    lateinit var useCase: DefaultListCastMembersUseCase

    @Mock
    lateinit var castMemberGateway: CastMemberGateway

    override fun getMocks(): List<Any> {
        return listOf(castMemberGateway)
    }

    @Test
    fun givenAValidQuery_whenCallsListCastMembers_shouldReturnAll() {
        // given
        val members = listOf(
            CastMember.newMember(Fixture.name(), Fixture.Companion.CastMembers.type()),
            CastMember.newMember(Fixture.name(), Fixture.Companion.CastMembers.type())
        )
        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = "Algo"
        val expectedSort = "createdAt"
        val expectedDirection = "asc"
        val expectedTotal = 2L
        val expectedItems = members.stream()
            .map(CastMemberListOutput::from)
            .toList()
        val expectedPagination = Pagination(
            expectedPage,
            expectedPerPage,
            expectedTotal,
            members
        )
        whenever(castMemberGateway.findAll(any())).thenReturn(expectedPagination)
        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        // when
        val actualOutput = useCase.execute(aQuery)

        // then
        assertEquals(expectedPage, actualOutput.currentPage)
        assertEquals(expectedPerPage, actualOutput.perPage)
        assertEquals(expectedTotal, actualOutput.total)
        assertEquals(expectedItems, actualOutput.items)
        verify(castMemberGateway).findAll(eq(aQuery))
    }

    @Test
    fun givenAValidQuery_whenCallsListCastMembersAndIsEmpty_shouldReturn() {
        // given
        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = "Algo"
        val expectedSort = "createdAt"
        val expectedDirection = "asc"
        val expectedTotal = 0L
        val members = emptyList<CastMember>()
        val expectedItems = emptyList<CastMemberListOutput>()
        val expectedPagination = Pagination(
            expectedPage,
            expectedPerPage,
            expectedTotal,
            members
        )
        whenever(castMemberGateway.findAll(any())).thenReturn(expectedPagination)
        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        // when
        val actualOutput = useCase.execute(aQuery)

        // then
        assertEquals(expectedPage, actualOutput.currentPage)
        assertEquals(expectedPerPage, actualOutput.perPage)
        assertEquals(expectedTotal, actualOutput.total)
        assertEquals(expectedItems, actualOutput.items)
        verify(castMemberGateway).findAll(eq(aQuery))
    }

    @Test
    fun givenAValidQuery_whenCallsListCastMembersAndGatewayThrowsRandomException_shouldException() {
        // given
        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = "Algo"
        val expectedSort = "createdAt"
        val expectedDirection = "asc"
        val expectedErrorMessage = "Gateway error"
        whenever(castMemberGateway.findAll(any())).thenThrow(IllegalStateException(expectedErrorMessage))
        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        // when
        val actualException = assertThrows(IllegalStateException::class.java) { useCase.execute(aQuery) }

        // then
        assertEquals(expectedErrorMessage, actualException.message)
        verify(castMemberGateway).findAll(eq(aQuery))
    }
}