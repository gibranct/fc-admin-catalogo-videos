package com.fullcycle.admin.catalogo.application.castmember.delete

import com.fullcycle.admin.catalogo.application.Fixture
import com.fullcycle.admin.catalogo.application.UseCaseTest
import com.fullcycle.admin.catalogo.domain.castmember.CastMember
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock

internal class DeleteCastMemberUseCaseTest : UseCaseTest() {

    @InjectMocks
    lateinit var useCase: DefaultDeleteCastMemberUseCase

    @Mock
    lateinit var castMemberGateway: CastMemberGateway

    override fun getMocks(): List<Any> {
        return listOf(castMemberGateway)
    }

    @Test
    fun givenAValidId_whenCallsDeleteCastMember_shouldDeleteIt() {
        // given
        val aMember = CastMember.newMember(Fixture.name()!!, Fixture.Companion.CastMembers.type())
        val expectedId = aMember.id
        doNothing()
            .`when`(castMemberGateway).deleteById(any())

        // when
        Assertions.assertDoesNotThrow { useCase.execute(expectedId.value) }

        // then
        verify(castMemberGateway).deleteById(eq(expectedId))
    }

    @Test
    fun givenAnInvalidId_whenCallsDeleteCastMember_shouldBeOk() {
        // given
        val expectedId = CastMemberID.from("123")
        doNothing()
            .`when`(castMemberGateway).deleteById(any())

        // when
        Assertions.assertDoesNotThrow { useCase.execute(expectedId.value) }

        // then
        verify(castMemberGateway).deleteById(eq(expectedId))
    }

    @Test
    fun givenAValidId_whenCallsDeleteCastMemberAndGatewayThrowsException_shouldReceiveException() {
        // given
        val aMember = CastMember.newMember(Fixture.name()!!, Fixture.Companion.CastMembers.type())
        val expectedId = aMember.id
        doThrow(IllegalStateException("Gateway error")).`when`(castMemberGateway).deleteById(any())

        // when
        Assertions.assertThrows(IllegalStateException::class.java) { useCase.execute(expectedId.value) }

        // then
        verify(castMemberGateway).deleteById(eq(expectedId))
    }
}