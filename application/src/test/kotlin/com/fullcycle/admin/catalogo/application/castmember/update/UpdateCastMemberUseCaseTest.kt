package com.fullcycle.admin.catalogo.application.castmember.update

import arrow.core.getOrElse
import com.fullcycle.admin.catalogo.application.UseCaseTest
import com.fullcycle.admin.catalogo.domain.castmember.CastMember
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import java.util.*

internal class UpdateCastMemberUseCaseTest : UseCaseTest() {

    @InjectMocks
    lateinit var useCase: DefaultUpdateCastMemberUseCase

    @Mock
    lateinit var castMemberGateway: CastMemberGateway

    override fun getMocks(): List<Any> {
        return listOf(castMemberGateway)
    }

    @Test
    fun givenAValidCommand_whenCallsUpdateCastMember_shouldReturnItsIdentifier() {
        // given
        val aMember = CastMember.newMember("vin diesel", CastMemberType.DIRECTOR)
        val expectedId = aMember.id
        val expectedName = Fixture.name()!!
        val expectedType = CastMemberType.ACTOR
        val aCommand = UpdateCastMemberCommand.with(
            expectedId.value,
            expectedName,
            expectedType
        )
        whenever(castMemberGateway.findById(any())).thenReturn(CastMember.with(aMember))
        whenever(castMemberGateway.update(any())).thenAnswer { it.arguments[0] }

        // when
        val actualOutput = useCase.execute(aCommand).getOrElse { return }

        // then
        assertNotNull(actualOutput)
        assertEquals(expectedId.value, actualOutput.id)
        verify(castMemberGateway).findById(eq(expectedId))
        verify(castMemberGateway).update(argThat { aUpdatedMember ->
            (Objects.equals(expectedId, aUpdatedMember.id)
                    && Objects.equals(expectedName, aUpdatedMember.name)
                    && Objects.equals(expectedType, aUpdatedMember.type)
                    && Objects.equals(aMember.updatedAt, aUpdatedMember.createdAt)
                    && aMember.updatedAt.isBefore(aUpdatedMember.updatedAt))
        })
    }

    @Test
    fun givenAInvalidId_whenCallsUpdateCastMember_shouldThrowsNotFoundException() {
        // given
        val expectedId = CastMemberID.from("123")
        val expectedName = Fixture.name()!!
        val expectedType = Fixture.Companion.CastMembers.type()
        val expectedErrorMessage = "CastMember with id 123 not found"
        val aCommand = UpdateCastMemberCommand.with(
            expectedId.value,
            expectedName,
            expectedType
        )
        whenever(castMemberGateway.findById(any())).thenReturn(null)

        // when
        val actualException = assertThrows(NotFoundException::class.java) { useCase.execute(aCommand) }

        // then
        assertNotNull(actualException)
        assertEquals(expectedErrorMessage, actualException.message)
        verify(castMemberGateway).findById(eq(expectedId))
        verify(castMemberGateway, times(0)).update(any())
    }
}