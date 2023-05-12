package com.fullcycle.admin.catalogo.infrastructure.video.presenters

import com.fullcycle.admin.catalogo.application.video.media.upload.UploadMediaOutput
import com.fullcycle.admin.catalogo.application.video.retrieve.get.VideoOutput
import com.fullcycle.admin.catalogo.application.video.retrieve.list.VideoListOutput
import com.fullcycle.admin.catalogo.application.video.update.UpdateVideoOutput
import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.domain.video.AudioVideoMedia
import com.fullcycle.admin.catalogo.domain.video.ImageMedia
import com.fullcycle.admin.catalogo.infrastructure.video.models.*


interface VideoApiPresenter {

    companion object {

        fun present(output: VideoOutput): VideoResponse {
            return VideoResponse(
                output.id,
                output.title,
                output.description,
                output.launchedAt,
                output.duration,
                output.opened,
                output.published,
                output.rating.name,
                output.createdAt,
                output.updatedAt,
                present(output.banner),
                present(output.thumbnail),
                present(output.thumbnailHalf),
                present(output.video),
                present(output.trailer),
                output.categories,
                output.genres,
                output.members,
            )
        }

        private fun present(media: AudioVideoMedia?): AudioVideoMediaResponse? {
            return if (media == null) {
                null
            } else AudioVideoMediaResponse(
                media.id,
                media.checksum,
                media.name,
                media.rawLocation,
                media.encodedLocation,
                media.status.name
            )
        }

        private fun present(image: ImageMedia?): ImageMediaResponse? {
            return if (image == null) {
                null
            } else ImageMediaResponse(
                image.id,
                image.checksum,
                image.name,
                image.location
            )
        }

        fun present(output: UpdateVideoOutput): UpdateVideoResponse? {
            return UpdateVideoResponse(output.id)
        }

        private fun present(output: VideoListOutput): VideoListResponse {
            return VideoListResponse(
                output.id,
                output.title,
                output.description,
                output.createdAt,
                output.updatedA,
            )
        }

        fun present(page: Pagination<VideoListOutput>): Pagination<VideoListResponse> {
            return page.map(VideoApiPresenter::present)
        }

        fun present(output: UploadMediaOutput): UploadMediaResponse {
            return UploadMediaResponse(output.videoId, output.mediaType)
        }

    }

}