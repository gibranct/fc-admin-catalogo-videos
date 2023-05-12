package com.fullcycle.admin.catalogo

import com.fullcycle.admin.catalogo.Fixture.Companion.CastMembers.gabriel
import com.fullcycle.admin.catalogo.Fixture.Companion.CastMembers.wesley
import com.fullcycle.admin.catalogo.Fixture.Companion.Categories.aulas
import com.fullcycle.admin.catalogo.Fixture.Companion.Genres.tech
import com.fullcycle.admin.catalogo.domain.castmember.CastMember
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType
import com.fullcycle.admin.catalogo.domain.category.Category
import com.fullcycle.admin.catalogo.domain.genre.Genre
import com.fullcycle.admin.catalogo.domain.resource.Resource
import com.fullcycle.admin.catalogo.domain.utils.IdUtils
import com.fullcycle.admin.catalogo.domain.video.*
import com.github.javafaker.Faker
import java.util.*

class Fixture {

    companion object {

        private val FAKER = Faker()

        private fun bool(): Boolean {
            return FAKER.bool().bool()
        }

        fun checksum(): String {
            return "03fe62de"
        }

        private fun title(): String {
            return FAKER.options().option(
                "System Design no Mercado Livre na prática",
                "Não cometa esses erros ao trabalhar com Microsserviços",
                "Testes de Mutação. Você não testa seu software corretamente"
            )
        }

        fun duration(): Double {
            return FAKER.options().option(120.0, 15.5, 35.5, 10.0, 2.0)
        }

        private fun year(): Int {
            return FAKER.random().nextInt(2020, 2030)
        }

        fun video(): Video {
            return Video.newVideo(
                title(),
                Videos.description(),
                year(),
                duration(),
                bool(),
                bool(),
                Videos.rating(),
                setOf(aulas().id),
                setOf(tech().id),
                setOf(wesley().id, gabriel().id)
            )
        }


        object Categories {
            private val AULAS: Category = Category.newCategory("Aulas", "Some description", true)
            private val LIVES: Category = Category.newCategory("Lives", "Some description", true)
            fun aulas(): Category {
                return AULAS.copy()
            }

            fun lives(): Category {
                return LIVES.copy()
            }
        }


        object CastMembers {
            private val WESLEY = CastMember.newMember("Wesley FullCycle", CastMemberType.ACTOR)
            private val GABRIEL = CastMember.newMember("Gabriel FullCycle", CastMemberType.ACTOR)
            fun type(): CastMemberType {
                return FAKER.options().option(*CastMemberType.values())
            }

            fun wesley(): CastMember {
                return CastMember.with(WESLEY)
            }

            fun gabriel(): CastMember {
                return CastMember.with(GABRIEL)
            }
        }


        object Genres {
            private val TECH = Genre.newGenre("Technology", true)
            private val BUSINESS = Genre.newGenre("Business", true)
            fun tech(): Genre {
                return Genre.with(TECH)
            }

            fun business(): Genre {
                return Genre.with(BUSINESS)
            }
        }


        object Videos {
            private val SYSTEM_DESIGN = Video.newVideo(
                "System Design no Mercado Livre na prática",
                description(),
                2022,
                duration(),
                bool(),
                bool(),
                rating(),
                setOf(aulas().id),
                setOf(tech().id),
                setOf(wesley().id, gabriel().id)
            )

            fun systemDesign(): Video {
                return Video.with(SYSTEM_DESIGN)
            }

            fun rating(): Rating {
                return FAKER.options().option(*Rating.values())
            }

            fun mediaType(): VideoMediaType {
                return FAKER.options().option(*VideoMediaType.values())
            }

            fun resource(type: VideoMediaType): Resource {
                val contentType: String = when(type) {
                    VideoMediaType.VIDEO -> "video/mp4"
                    VideoMediaType.TRAILER -> "video/mp4"
                    else -> "image/jpg"
                }
                val checksum = IdUtils.uuid()
                val content = "Conteudo".toByteArray()
                return Resource.with(content, checksum, contentType, type.name.lowercase(Locale.getDefault()))
            }

            fun description(): String {
                return FAKER.options().option(
                    """
                            Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                            Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                            Para acessar todas as aulas, lives e desafios, acesse:
                            https://imersao.fullcycle.com.br/
                            
                            """.trimIndent(),
                    """
                            Nesse vídeo você entenderá o que é DTO (Data Transfer Object), quando e como utilizar no dia a dia, 
                            bem como sua importância para criar aplicações com alta qualidade.
                            
                            """
                        .trimIndent()
                )
            }

            fun audioVideo(type: VideoMediaType): AudioVideoMedia {
                val checksum: String = checksum()
                return AudioVideoMedia.with(
                    checksum,
                    type.name.lowercase(Locale.getDefault()),
                    "/videos/$checksum"
                )
            }

            fun image(type: VideoMediaType): ImageMedia {
                val checksum = checksum()
                return ImageMedia.with(
                    checksum,
                    type.name.lowercase(Locale.getDefault()),
                    "/images/$checksum"
                )
            }
        }


    }

}