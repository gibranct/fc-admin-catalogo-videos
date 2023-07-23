package com.fullcycle.admin.catalogo.infrastructure.video

import com.fullcycle.admin.catalogo.domain.resource.Resource
import com.fullcycle.admin.catalogo.domain.video.AudioVideoMedia
import com.fullcycle.admin.catalogo.domain.video.ImageMedia
import com.fullcycle.admin.catalogo.domain.video.MediaResourceGateway
import com.fullcycle.admin.catalogo.domain.video.VideoID
import com.fullcycle.admin.catalogo.domain.video.VideoMediaType
import com.fullcycle.admin.catalogo.domain.video.VideoResource
import com.fullcycle.admin.catalogo.infrastructure.configuration.properties.storage.StorageProperties
import com.fullcycle.admin.catalogo.infrastructure.services.StorageService
import org.springframework.stereotype.Component

@Component
class DefaultMediaResourceGateway(
    private val storageProperties: StorageProperties,
    private val storageService: StorageService,
) : MediaResourceGateway {


    override fun storeAudioVideo(videoID: VideoID, resource: VideoResource): AudioVideoMedia {
        val filepath = filePath(videoID, resource.type)
        val aResource = resource.resource
        store(filepath, aResource)
        return AudioVideoMedia.with(aResource.checksum, aResource.name, filepath)
    }

    override fun storeImage(videoID: VideoID, resource: VideoResource): ImageMedia {
        val filepath = filePath(videoID, resource.type)
        val aResource = resource.resource
        store(filepath, aResource)
        return ImageMedia.with(aResource.checksum, aResource.name, filepath)
    }

    override fun getResource(anId: VideoID, type: VideoMediaType): Resource? {
        return this.storageService.get(filePath(anId, type))
    }

    override fun clearResources(videoID: VideoID) {
        val ids = this.storageService.list(folder(videoID))
        this.storageService.deleteAll(ids)
    }

    private fun filename(videoMediaType: VideoMediaType): String {
        return storageProperties.filenamePattern!!.replace("{type}", videoMediaType.name)
    }

    private fun folder(videoID: VideoID): String {
        return storageProperties.locationPattern!!.replace("{videoId}", videoID.value)
    }

    private fun filePath(videoID: VideoID, videoMediaType: VideoMediaType): String {
        return folder(videoID)
            .plus("/")
            .plus(filename(videoMediaType))
    }

    private fun store(filepath: String, aResource: Resource) {
        storageService.store(filepath, aResource)
    }
}