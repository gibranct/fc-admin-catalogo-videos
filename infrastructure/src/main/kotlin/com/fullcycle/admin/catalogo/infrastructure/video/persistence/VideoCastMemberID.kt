package com.fullcycle.admin.catalogo.infrastructure.video.persistence

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class VideoCastMemberID(
    @Column(name = "video_id", nullable = false)  val videoId: String,
    @Column(name = "cast_member_id", nullable = false) val castMemberId: String,
) : Serializable {

    companion object {

        fun from(videoId: String, castMemberId: String): VideoCastMemberID {
            return VideoCastMemberID(videoId, castMemberId)
        }

    }

}
