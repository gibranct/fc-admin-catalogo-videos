package com.fullcycle.admin.catalogo.domain.castmember

import com.fullcycle.admin.catalogo.domain.AggregateRoot
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException
import com.fullcycle.admin.catalogo.domain.utils.InstantUtils
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification
import java.time.Instant


data class CastMember(
    override val id: CastMemberID,
    var  name: String,
    var  type: CastMemberType,
    val  createdAt: Instant,
    var  updatedAt: Instant,
): AggregateRoot<CastMemberID>(id) {

    companion object {

        fun newMember(aName: String, aType: CastMemberType): CastMember {
            val anId = CastMemberID.unique()
            val now = InstantUtils.now()
            val castMember = CastMember(anId, aName, aType, now, now)
            castMember.selfValidate()
            return castMember
        }

        fun with(
            anId: CastMemberID,
            aName: String,
            aType: CastMemberType,
            aCreationDate: Instant,
            aUpdateDate: Instant
        ): CastMember {
            val castMember = CastMember(anId, aName, aType, aCreationDate, aUpdateDate)
            castMember.selfValidate()
            return castMember
        }

        fun with(aMember: CastMember): CastMember {
            val castMember = CastMember(
                aMember.id,
                aMember.name,
                aMember.type,
                aMember.createdAt,
                aMember.updatedAt
            )
            castMember.selfValidate()
            return castMember
        }
    }

    fun update(aName: String, aType: CastMemberType): CastMember {
        name = aName
        type = aType
        updatedAt = InstantUtils.now()
        selfValidate()
        return this
    }

    override fun validate(handler: ValidationHandler) {
        CastMemberValidator(this, handler).validate()
    }

    private fun selfValidate() {
        val notification = Notification.create()
        validate(notification)
        if (notification.hasError()) {
            throw NotificationException("Failed to create a Aggregate CastMember", notification)
        }
    }
}