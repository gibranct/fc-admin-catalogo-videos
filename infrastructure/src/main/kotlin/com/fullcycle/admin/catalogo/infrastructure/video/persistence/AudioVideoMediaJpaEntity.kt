package com.fullcycle.admin.catalogo.infrastructure.video.persistence

import com.fullcycle.admin.catalogo.domain.video.AudioVideoMedia
import com.fullcycle.admin.catalogo.domain.video.MediaStatus
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "AudioVideoMedia")
@Table(name = "videos_video_media")
data class AudioVideoMediaJpaEntity (
    @Id val id: String,
    @Column(name = "checksum", nullable = false) val checksum: String,
    @Column(name = "name", nullable = false) val name: String,
    @Column(name = "file_path", nullable = false) val filePath: String,
    @Column(name = "encoded_path", nullable = false) val encodedPath: String,
    @Column(name = "media_status", nullable = false) @Enumerated(EnumType.STRING) val status: MediaStatus,
) {

    companion object {

        fun from(media: AudioVideoMedia): AudioVideoMediaJpaEntity {
            return AudioVideoMediaJpaEntity(
                media.id,
                media.checksum,
                media.name,
                media.rawLocation,
                media.encodedLocation,
                media.status
            )
        }

    }

    fun toDomain(): AudioVideoMedia {
        return AudioVideoMedia.with(
            id,
            checksum,
            name,
            filePath,
            encodedPath,
            status
        )
    }

}