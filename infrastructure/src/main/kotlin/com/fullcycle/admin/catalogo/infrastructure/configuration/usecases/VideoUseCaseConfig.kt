package com.fullcycle.admin.catalogo.infrastructure.configuration.usecases

import com.fullcycle.admin.catalogo.application.video.create.CreateVideoUseCase
import com.fullcycle.admin.catalogo.application.video.create.DefaultCreateVideoUseCase
import com.fullcycle.admin.catalogo.application.video.delete.DefaultDeleteVideoUseCase
import com.fullcycle.admin.catalogo.application.video.delete.DeleteVideoUseCase
import com.fullcycle.admin.catalogo.application.video.media.get.DefaultGetMediaUseCase
import com.fullcycle.admin.catalogo.application.video.media.get.GetMediaUseCase
import com.fullcycle.admin.catalogo.application.video.media.update.DefaultUpdateMediaStatusUseCase
import com.fullcycle.admin.catalogo.application.video.media.update.UpdateMediaStatusUseCase
import com.fullcycle.admin.catalogo.application.video.media.upload.DefaultUploadMediaUseCase
import com.fullcycle.admin.catalogo.application.video.media.upload.UploadMediaUseCase
import com.fullcycle.admin.catalogo.application.video.retrieve.get.DefaultGetVideoByIdUseCase
import com.fullcycle.admin.catalogo.application.video.retrieve.get.GetVideoByIdUseCase
import com.fullcycle.admin.catalogo.application.video.retrieve.list.DefaultListVideosUseCase
import com.fullcycle.admin.catalogo.application.video.retrieve.list.ListVideosUseCase
import com.fullcycle.admin.catalogo.application.video.update.DefaultUpdateVideoUseCase
import com.fullcycle.admin.catalogo.application.video.update.UpdateVideoUseCase
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway
import com.fullcycle.admin.catalogo.domain.video.MediaResourceGateway
import com.fullcycle.admin.catalogo.domain.video.VideoGateway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class VideoUseCaseConfig(
    private val categoryGateway: CategoryGateway,
    private val castMemberGateway: CastMemberGateway,
    private val genreGateway: GenreGateway,
    private val mediaResourceGateway: MediaResourceGateway,
    private val videoGateway: VideoGateway,
) {

    @Bean
    fun createVideoUseCase(): CreateVideoUseCase? {
        return DefaultCreateVideoUseCase(
            categoryGateway,
            castMemberGateway,
            genreGateway,
            mediaResourceGateway,
            videoGateway
        )
    }

    @Bean
    fun updateVideoUseCase(): UpdateVideoUseCase {
        return DefaultUpdateVideoUseCase(
            categoryGateway,
            castMemberGateway,
            genreGateway,
            mediaResourceGateway,
            videoGateway
        )
    }

    @Bean
    fun getVideoByIdUseCase(): GetVideoByIdUseCase? {
        return DefaultGetVideoByIdUseCase(videoGateway)
    }

    @Bean
    fun deleteVideoUseCase(): DeleteVideoUseCase? {
        return DefaultDeleteVideoUseCase(videoGateway, mediaResourceGateway)
    }

    @Bean
    fun listVideosUseCase(): ListVideosUseCase? {
        return DefaultListVideosUseCase(videoGateway)
    }

    @Bean
    fun getMediaUseCase(): GetMediaUseCase? {
        return DefaultGetMediaUseCase(mediaResourceGateway)
    }

    @Bean
    fun uploadMediaUseCase(): UploadMediaUseCase? {
        return DefaultUploadMediaUseCase(mediaResourceGateway, videoGateway)
    }

    @Bean
    fun updateMediaStatusUseCase(): UpdateMediaStatusUseCase? {
        return DefaultUpdateMediaStatusUseCase(videoGateway)
    }

}