package com.fullcycle.admin.catalogo.domain.video

import com.fullcycle.admin.catalogo.domain.event.DomainEvent
import com.fullcycle.admin.catalogo.domain.utils.InstantUtils
import java.time.Instant

data class VideoMediaCreated constructor(
    val resourceId: String,
    val filePath: String,
    val occurredOn: Instant,
) : DomainEvent {

    companion object {

        fun with(
            resourceId: String,
            filePath: String,
        ) = VideoMediaCreated(resourceId, filePath, InstantUtils.now())

    }

    override fun ocorred() = occurredOn

}
