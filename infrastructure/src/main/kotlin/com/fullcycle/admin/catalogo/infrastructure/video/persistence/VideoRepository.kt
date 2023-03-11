package com.fullcycle.admin.catalogo.infrastructure.video.persistence

import com.fullcycle.admin.catalogo.domain.video.VideoPreview
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


interface VideoRepository : JpaRepository<VideoJpaEntity, String> {

    @Query(
        """
            select distinct new com.fullcycle.admin.catalogo.domain.video.VideoPreview(
                v.id as id,
                v.title as title,
                v.description as description,
                v.createdAt as createdAt,
                v.updatedAt as updatedAt
            )
            from Video v
                left join v.castMembers members
                left join v.categories categories
                left join v.genres genres
            where
                ( :terms is null or UPPER(v.title) like :terms )
            and
                ( :castMembers is null or members.id.castMemberId in :castMembers )
            and
                ( :categories is null or categories.id.categoryId in :categories )
            and
                ( :genres is null or genres.id.genreId in :genres )
            
            """
    )
    fun findAll(
        @Param("terms") terms: String,
        @Param("castMembers") castMembers: Set<String>,
        @Param("categories") categories: Set<String>,
        @Param("genres") genres: Set<String>,
        page: Pageable
    ): Page<VideoPreview>

}