package com.fullcycle.admin.catalogo.application.castmember.retrieve.get

import com.fullcycle.admin.catalogo.application.Fixture
import com.fullcycle.admin.catalogo.application.UseCaseTest
import com.fullcycle.admin.catalogo.domain.castmember.CastMember
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock


internal class GetCastMemberByIdUseCaseTest : UseCaseTest() {

    @InjectMocks
    lateinit var useCase: DefaultGetCastMemberByIdUseCase

    @Mock
    lateinit var castMemberGateway: CastMemberGateway

    override fun getMocks(): List<Any> {
       return listOf(castMemberGateway)
    }

    @Test
    fun givenAValidId_whenCallsGetCastMember_shouldReturnIt() {
        // given
        val expectedName = Fixture.name()!!
        val expectedType = Fixture.Companion.CastMembers.type()
        val aMember = CastMember.newMember(expectedName, expectedType)
        val expectedId = aMember.id
        whenever(castMemberGateway.findById(any()))
            .thenReturn(aMember)

        // when
        val actualOutput = useCase.execute(expectedId.value)

        // then
        assertNotNull(actualOutput)
        assertEquals(expectedId.value, actualOutput.id)
        assertEquals(expectedName, actualOutput.name)
        assertEquals(expectedType, actualOutput.type)
        assertEquals(aMember.createdAt, actualOutput.createdAt)
        assertEquals(aMember.updatedAt, actualOutput.updatedAt)
        verify(castMemberGateway).findById(eq(expectedId))
    }

    @Test
    fun givenAInvalidId_whenCallsGetCastMemberAndDoesNotExists_shouldReturnNotFoundException() {
        // given
        val expectedId = CastMemberID.from("123")
        val expectedErrorMessage = "CastMember with id 123 not found"
        whenever(castMemberGateway.findById(any()))
            .thenReturn(null)

        // when
        val actualOutput = assertThrows(NotFoundException::class.java) { useCase.execute(expectedId.value) }

        // then
        assertNotNull(actualOutput)
        assertEquals(expectedErrorMessage, actualOutput.message)
        verify(castMemberGateway).findById(eq(expectedId))
    }
}