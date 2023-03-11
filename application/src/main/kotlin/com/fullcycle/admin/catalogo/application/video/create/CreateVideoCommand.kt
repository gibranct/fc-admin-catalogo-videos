package com.fullcycle.admin.catalogo.application.video.create

import com.fullcycle.admin.catalogo.domain.video.Resource

data class CreateVideoCommand(
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
        ): CreateVideoCommand {
            return CreateVideoCommand(
                title,
                description,
                launchedAt,
                duration,
                opened,
                published,
                rating,
                categories,
                genres,
                members,
                video,
                trailer,
                banner,
                thumbnail,
                thumbnailHalf,
            )
        }

        fun with(
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
        ): CreateVideoCommand {
            return CreateVideoCommand(
                title,
                description,
                launchedAt,
                duration,
                opened,
                published,
                rating,
                categories,
                genres,
                members,
                null,
                null,
                null,
                null,
                null
            )
        }

    }

}
