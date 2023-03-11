package com.fullcycle.admin.catalogo.domain.video

interface MediaSourceGateway {

    fun storeAudioVideo(videoID: VideoID, resource: Resource) : AudioVideoMedia

    fun storeImage(videoID: VideoID, resource: Resource) : ImageMedia

    fun clearResources(videoID: VideoID)

}