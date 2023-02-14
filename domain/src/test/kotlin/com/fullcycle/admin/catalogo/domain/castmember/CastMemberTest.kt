package com.fullcycle.admin.catalogo.domain.castmember

import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


internal class CastMemberTest {

    @Test
    fun givenAValidParams_whenCallsNewMember_thenInstantiateACastMember() {
        val expectedName = "Vin Diesel"
        val expectedType = CastMemberType.ACTOR
        val actualMember = CastMember.newMember(expectedName, expectedType)
        Assertions.assertNotNull(actualMember)
        Assertions.assertNotNull(actualMember.id)
        Assertions.assertEquals(expectedName, actualMember.name)
        Assertions.assertEquals(expectedType, actualMember.type)
        Assertions.assertNotNull(actualMember.createdAt)
        Assertions.assertNotNull(actualMember.updatedAt)
        Assertions.assertEquals(actualMember.createdAt, actualMember.updatedAt)
    }

    @Test
    fun givenAInvalidEmptyName_whenCallsNewMember_shouldReceiveANotification() {
        val expectedName = " "
        val expectedType = CastMemberType.ACTOR
        val expectedErrorCount = 1
        val expectedErrorMessage = "'name' should not be empty"
        val actualException = Assertions.assertThrows(NotificationException::class.java) { CastMember.newMember(expectedName, expectedType) }
        Assertions.assertNotNull(actualException)
        Assertions.assertEquals(expectedErrorCount, actualException.errors.size)
        Assertions.assertEquals(expectedErrorMessage, actualException.errors[0].message)
    }

    @Test
    fun givenAInvalidNameWithLengthMoreThan255_whenCallsNewMember_shouldReceiveANotification() {
        val expectedName =  """
                Gostaria de enfatizar que o consenso sobre a necessidade de qualificação auxilia a preparação e a
                composição das posturas dos órgãos dirigentes com relação às suas atribuições.
                Do mesmo modo, a estrutura atual da organização apresenta tendências no sentido de aprovar a
                manutenção das novas proposições.
                """
        val expectedType = CastMemberType.ACTOR
        val expectedErrorCount = 1
        val expectedErrorMessage = "'name' must be between 3 and 255 characters"
        val actualException = Assertions.assertThrows(NotificationException::class.java) { CastMember.newMember(expectedName, expectedType) }
        Assertions.assertNotNull(actualException)
        Assertions.assertEquals(expectedErrorCount, actualException.errors.size)
        Assertions.assertEquals(expectedErrorMessage, actualException.errors[0].message)
    }

    @Test
    fun givenAValidCastMember_whenCallUpdate_shouldReceiveUpdated() {
        val expectedName = "Vin Diesel"
        val expectedType = CastMemberType.ACTOR
        val actualMember = CastMember.newMember("vind", CastMemberType.DIRECTOR)
        Assertions.assertNotNull(actualMember)
        Assertions.assertNotNull(actualMember.id)
        val actualID = actualMember.id
        val actualCreatedAt = actualMember.createdAt
        val actualUpdatedAt = actualMember.updatedAt
        actualMember.update(expectedName, expectedType)
        Assertions.assertEquals(actualID, actualMember.id)
        Assertions.assertEquals(expectedName, actualMember.name)
        Assertions.assertEquals(expectedType, actualMember.type)
        Assertions.assertEquals(actualCreatedAt, actualMember.createdAt)
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualMember.updatedAt))
    }

    @Test
    fun givenAValidCastMember_whenCallUpdateWithInvalidEmptyName_shouldReceiveNotification() {
        val expectedName = " "
        val expectedType = CastMemberType.ACTOR
        val expectedErrorCount = 1
        val expectedErrorMessage = "'name' should not be empty"
        val actualMember = CastMember.newMember("vind", CastMemberType.DIRECTOR)
        Assertions.assertNotNull(actualMember)
        Assertions.assertNotNull(actualMember.id)
        val actualException = Assertions.assertThrows(
            NotificationException::class.java
        ) { actualMember.update(expectedName, expectedType) }
        Assertions.assertNotNull(actualException)
        Assertions.assertEquals(expectedErrorCount, actualException.errors.size)
        Assertions.assertEquals(expectedErrorMessage, actualException.errors[0].message)
    }

    @Test
    fun givenAValidCastMember_whenCallUpdateWithLengthMoreThan255_shouldReceiveNotification() {
        val expectedName = """
                Gostaria de enfatizar que o consenso sobre a necessidade de qualificação auxilia a preparação e a
                composição das posturas dos órgãos dirigentes com relação às suas atribuições.
                Do mesmo modo, a estrutura atual da organização apresenta tendências no sentido de aprovar a
                manutenção das novas proposições.
                """
        val expectedType = CastMemberType.ACTOR
        val expectedErrorCount = 1
        val expectedErrorMessage = "'name' must be between 3 and 255 characters"
        val actualMember = CastMember.newMember("vind", CastMemberType.DIRECTOR)
        Assertions.assertNotNull(actualMember)
        Assertions.assertNotNull(actualMember.id)
        val actualException = Assertions.assertThrows(NotificationException::class.java) { actualMember.update(expectedName, expectedType) }
        Assertions.assertNotNull(actualException)
        Assertions.assertEquals(expectedErrorCount, actualException.errors.size)
        Assertions.assertEquals(expectedErrorMessage, actualException.errors[0].message)
    }

}