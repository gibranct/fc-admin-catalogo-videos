package com.fullcycle.admin.catalogo.application.castmember.create

import arrow.core.getOrElse
import com.fullcycle.admin.catalogo.application.Fixture
import com.fullcycle.admin.catalogo.application.UseCaseTest
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import java.util.*

internal class CreateCastMemberUseCaseTest : UseCaseTest() {

    @InjectMocks
    lateinit var useCase: DefaultCreateCastMemberUseCase

    @Mock
    lateinit var castMemberGateway: CastMemberGateway

    override fun getMocks(): List<Any> {
        return listOf(castMemberGateway)
    }

    @Test
    fun givenAValidCommand_whenCallsCreateCastMember_shouldReturnIt() {
        // given
        val expectedName = Fixture.name()
        val expectedType = Fixture.Companion.CastMembers.type()
        val aCommand = CreateCastMemberCommand.with(expectedName, expectedType)
        whenever(castMemberGateway.create(any())).thenAnswer {
            it.arguments[0]
        }

        // when
        val actualOutput = useCase.execute(aCommand).getOrElse { return  }

        // then
        assertNotNull(actualOutput)
        assertNotNull(actualOutput.id)
        verify(castMemberGateway).create(argThat { aMember ->
            (Objects.nonNull(aMember.id)
                    && Objects.equals(expectedName, aMember.name)
                    && Objects.equals(expectedType, aMember.type)
                    && Objects.nonNull(aMember.createdAt)
                    && Objects.nonNull(aMember.updatedAt))
        })
    }

}