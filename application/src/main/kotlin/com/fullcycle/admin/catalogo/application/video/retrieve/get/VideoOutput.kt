package com.fullcycle.admin.catalogo.application.video.retrieve.get

import com.fullcycle.admin.catalogo.domain.video.AudioVideoMedia
import com.fullcycle.admin.catalogo.domain.video.ImageMedia
import com.fullcycle.admin.catalogo.domain.video.Rating
import com.fullcycle.admin.catalogo.domain.video.Video
import java.time.Instant

data class VideoOutput(
    val id: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val title: String,
    val description: String,
    val launchedAt: Int,
    val duration: Double,
    val opened: Boolean,
    val published: Boolean,
    val rating: Rating,
    val categories: Set<String>,
    val genres: Set<String>,
    val members: Set<String>,
    val banner: ImageMedia?,
    val thumbnail: ImageMedia?,
    val thumbnailHalf: ImageMedia?,
    val video: AudioVideoMedia?,
    val trailer: AudioVideoMedia?,
) {

    companion object {

        fun from(video: Video) = VideoOutput(
            id = video.id.value,
            createdAt = video.createdAt,
            updatedAt = video.updatedAt,
            title = video.title,
            description = video.description,
            launchedAt = video.launchedAt,
            duration = video.duration,
            opened = video.opened,
            published = video.published,
            rating = video.rating,
            categories = video.categories.map{ it.value }.toSet(),
            genres = video.genres.map{ it.value }.toSet(),
            members = video.castMembers.map{ it.value }.toSet(),
            banner = video.banner,
            thumbnail = video.thumbnail,
            thumbnailHalf = video.thumbnailHalf,
            video = video.video,
            trailer = video.trailer,
        )

    }

}
