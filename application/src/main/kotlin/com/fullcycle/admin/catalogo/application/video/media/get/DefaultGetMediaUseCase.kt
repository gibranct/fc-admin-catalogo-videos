package com.fullcycle.admin.catalogo.application.video.media.get

import arrow.core.getOrElse
import arrow.core.toOption
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException
import com.fullcycle.admin.catalogo.domain.video.MediaResourceGateway
import com.fullcycle.admin.catalogo.domain.video.VideoID
import com.fullcycle.admin.catalogo.domain.video.VideoMediaType
import com.fullcycle.admin.catalogo.domain.validation.Error

class DefaultGetMediaUseCase(
    private val mediaResourceGateway: MediaResourceGateway
) : GetMediaUseCase() {

    override fun execute(anIn: GetMediaCommand): MediaOutput {
        val anId = VideoID.from(anIn.videoId)
        val aType = VideoMediaType.valueOf(anIn.mediaType)
            .toOption()
            .getOrElse { throw typeNotFound(anIn.mediaType) }

        val aResource = mediaResourceGateway.getResource(anId, aType)
            .toOption().getOrElse { throw notFound(anIn.videoId, anIn.mediaType) }

        return MediaOutput.with(aResource)
    }

    private fun notFound(anId: String, aType: String): NotFoundException {
        return NotFoundException.with(Error("Resource $aType not found for video $anId"))
    }

    private fun typeNotFound(aType: String): NotFoundException {
        return NotFoundException.with(Error("Media type $aType doesn't exists"))
    }
}