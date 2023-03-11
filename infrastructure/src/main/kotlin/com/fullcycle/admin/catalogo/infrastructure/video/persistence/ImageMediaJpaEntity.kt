package com.fullcycle.admin.catalogo.infrastructure.video.persistence

import com.fullcycle.admin.catalogo.domain.video.ImageMedia
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "ImageMedia")
@Table(name = "videos_image_media")
data class ImageMediaJpaEntity(
    @Id val id: String,
    @Column(name = "checksum", nullable = false) val checksum: String,
    @Column(name = "name", nullable = false) val name: String,
    @Column(name = "file_path", nullable = false) val filePath: String,
) {


   companion object {

       fun from(media: ImageMedia): ImageMediaJpaEntity {
           return ImageMediaJpaEntity(
               media.id,
               media.checksum,
               media.name,
               media.location
           )
       }

   }

    fun toDomain(): ImageMedia {
        return ImageMedia.with(
            id,
            checksum,
            name,
            filePath,
        )
    }

}
