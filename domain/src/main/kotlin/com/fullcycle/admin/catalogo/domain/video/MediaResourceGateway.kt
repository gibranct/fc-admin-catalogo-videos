package com.fullcycle.admin.catalogo.domain.video

import com.fullcycle.admin.catalogo.domain.resource.Resource


interface MediaResourceGateway {

    fun storeAudioVideo(videoID: VideoID, resource: VideoResource) : AudioVideoMedia

    fun storeImage(videoID: VideoID, resource: VideoResource) : ImageMedia

    fun getResource(anId: VideoID, type: VideoMediaType): Resource?

    fun clearResources(videoID: VideoID)

}