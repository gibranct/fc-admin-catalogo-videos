package com.fullcycle.admin.catalogo.infrastructure.video.persistence

import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.genre.GenreID
import com.fullcycle.admin.catalogo.domain.video.Rating
import com.fullcycle.admin.catalogo.domain.video.Video
import com.fullcycle.admin.catalogo.domain.video.VideoID
import java.time.Instant
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table


@Table(name = "videos")
@Entity(name = "Video")
class VideoJpaEntity(
    @Id @Column(name = "id", nullable = false) val id: String,
    @Column(name = "title", nullable = false) val title: String,
    @Column(name = "description", length = 4000) val description: String,
    @Column(name = "year_launched", nullable = false) val yearLaunched: Int,
    @Column(name = "opened", nullable = false) val opened: Boolean,
    @Column(name = "published", nullable = false) val published: Boolean,
    @Column(name = "rating") val rating: Rating,
    @Column(name = "duration", precision = 2) val duration: Double,
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)") val createdAt: Instant,
    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)") val updatedAt: Instant,
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true) @JoinColumn(name = "video_id") val video: AudioVideoMediaJpaEntity?,
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)  @JoinColumn(name = "trailer_id") val trailer: AudioVideoMediaJpaEntity?,
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true) @JoinColumn(name = "banner_id") val banner: ImageMediaJpaEntity?,
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true) @JoinColumn(name = "thumbnail_id") val thumbnail: ImageMediaJpaEntity?,
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)  @JoinColumn(name = "thumbnail_half_id") val thumbnailHalf: ImageMediaJpaEntity?,
    @OneToMany(mappedBy = "video", cascade = [CascadeType.ALL], orphanRemoval = true) val categories: Set<VideoCategoryJpaEntity>? = emptySet(),
    @OneToMany(mappedBy = "video", cascade = [CascadeType.ALL], orphanRemoval = true) val genres: Set<VideoGenreJpaEntity>? = emptySet(),
    @OneToMany(mappedBy = "video", cascade = [CascadeType.ALL], orphanRemoval = true) val castMembers: Set<VideoCastMemberJpaEntity>? = emptySet(),
) {

    companion object {

        fun from(aVideo: Video): VideoJpaEntity {
            val entity = VideoJpaEntity(
                aVideo.id.value,
                aVideo.title,
                aVideo.description,
                aVideo.launchedAt,
                aVideo.opened,
                aVideo.published,
                aVideo.rating,
                aVideo.duration,
                aVideo.createdAt,
                aVideo.updatedAt,
                aVideo.video?.let { AudioVideoMediaJpaEntity.from(it) },
                aVideo.trailer?.let { AudioVideoMediaJpaEntity.from(it) },
                aVideo.banner?.let { ImageMediaJpaEntity.from(it) },
                aVideo.thumbnail?.let { ImageMediaJpaEntity.from(it) },
                aVideo.thumbnailHalf?.let { ImageMediaJpaEntity.from(it) },
            )
            aVideo.categories.forEach(entity::addCategory)
            aVideo.genres.forEach(entity::addGenre)
            aVideo.castMembers.forEach(entity::addCastMember)
            return entity
        }

    }

    fun toAggregate(): Video {
        return Video.with(
            anId = VideoID.from(id),
            aTitle = title,
            aDescription = description,
            aLaunchYear = yearLaunched,
            aDuration = duration ,
            wasOpened = opened,
            wasPublished = published,
            aRating = rating,
            aCreationDate = createdAt,
            aUpdateDate = updatedAt,
            aBanner = banner?.toDomain(),
            aThumb = thumbnail?.toDomain(),
            aThumbHalf = thumbnailHalf?.toDomain(),
            aTrailer = trailer?.toDomain(),
            aVideo = video?.toDomain(),
            categories = categories.orEmpty().map { CategoryID.from(it.id.categoryId) }.toSet(),
            genres = genres.orEmpty().map { GenreID.from(it.id.genreId) }.toSet(),
            members = castMembers.orEmpty().map { CastMemberID.from(it.id.castMemberId) }.toSet(),
        )
    }


    fun addCategory(anId: CategoryID) {
        categories?.plus(VideoCategoryJpaEntity.from(this, anId))
    }

    fun addGenre(anId: GenreID) {
        genres?.plus(VideoGenreJpaEntity.from(this, anId))
    }

    fun addCastMember(anId: CastMemberID) {
        castMembers?.plus(VideoCastMemberJpaEntity.from(this, anId))
    }

}
