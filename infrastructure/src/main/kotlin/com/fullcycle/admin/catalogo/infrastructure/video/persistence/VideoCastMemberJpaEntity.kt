package com.fullcycle.admin.catalogo.infrastructure.video.persistence

import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.ManyToOne
import javax.persistence.MapsId
import javax.persistence.Table

@Entity(name = "VideoCastMember")
@Table(name = "videos_cast_members")
data class VideoCastMemberJpaEntity(
    @EmbeddedId  val id: VideoCastMemberID,
    @ManyToOne(fetch = FetchType.LAZY) @MapsId("videoId") val video: VideoJpaEntity,
) {

    companion object {

        fun from(video: VideoJpaEntity, castMemberID: CastMemberID) = VideoCastMemberJpaEntity(
            VideoCastMemberID.from(video.id, castMemberID.value), video
        )

    }

}
