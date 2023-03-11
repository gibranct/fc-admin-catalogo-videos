package com.fullcycle.admin.catalogo.application.video.update

import com.fullcycle.admin.catalogo.domain.video.Resource

data class UpdateVideoCommand(
    val id: String,
    val title: String,
    val description: String,
    val launchedAt: Int,
    val duration: Double,
    val opened: Boolean,
    val published: Boolean,
    val rating: String,
    val categories: Set<String>,
    val genres: Set<String>,
    val members: Set<String>,
    val video: Resource?,
    val trailer: Resource?,
    val banner: Resource?,
    val thumbnail: Resource?,
    val thumbnailHalf: Resource?,
) {

    companion object {

        fun with(
            id: String,
            title: String,
            description: String,
            launchedAt: Int,
            duration: Double,
            opened: Boolean,
            published: Boolean,
            rating: String,
            categories: Set<String>,
            genres: Set<String>,
            members: Set<String>,
        ): UpdateVideoCommand {
            return UpdateVideoCommand(
                id = id,
                title = title,
                description = description,
                launchedAt = launchedAt,
                duration = duration,
                opened = opened,
                published = published,
                rating = rating,
                categories = categories,
                genres = genres,
                members = members,
                null,
                null,
                null,
                null,
                null
            )
        }

        fun with(
            id: String,
            title: String,
            description: String,
            launchedAt: Int,
            duration: Double,
            opened: Boolean,
            published: Boolean,
            rating: String,
            categories: Set<String>,
            genres: Set<String>,
            members: Set<String>,
            video: Resource,
            trailer: Resource,
            banner: Resource,
            thumbnail: Resource,
            thumbnailHalf: Resource,
        ): UpdateVideoCommand {
            return UpdateVideoCommand(
                id = id,
                title = title,
                description = description,
                launchedAt = launchedAt,
                duration = duration,
                opened = opened,
                published = published,
                rating = rating,
                categories = categories,
                genres = genres,
                members = members,
                video = video,
                trailer = trailer,
                banner = banner,
                thumbnail = thumbnail,
                thumbnailHalf = thumbnailHalf,
            )
        }

    }

}
