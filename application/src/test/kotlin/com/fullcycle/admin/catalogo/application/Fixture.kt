package com.fullcycle.admin.catalogo.application

import com.fullcycle.admin.catalogo.domain.castmember.CastMember
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType
import com.fullcycle.admin.catalogo.domain.category.Category
import com.github.javafaker.Faker

class Fixture {

    companion object {

        private val FAKER: Faker = Faker()

        fun name(): String? {
            return FAKER.name().fullName()
        }

        fun year(): Int? {
            return FAKER.random().nextInt(2020, 2030)
        }

        fun duration(): Double? {
            return FAKER.options().option(120.0, 15.5, 35.5, 10.0, 2.0)
        }

        fun bool(): Boolean {
            return FAKER.bool().bool()
        }

        fun title(): String {
            return FAKER.options().option(
                "System Design no Mercado Livre na prática",
                "Não cometa esses erros ao trabalhar com Microsserviços",
                "Testes de Mutação. Você não testa seu software corretamente"
            )
        }

        class Categories {

            companion object {

                private val AULAS: Category = Category.newCategory("Aulas", "Some description", true)

                fun aulas(): Category {
                    return AULAS.copy()
                }

            }

        }

        class CastMembers {
            companion object {

                private val WESLEY = CastMember.newMember("Wesley FullCycle", CastMemberType.ACTOR)
                private val GABRIEL = CastMember.newMember("Gabriel FullCycle", CastMemberType.ACTOR)
                fun type(): CastMemberType {
                    return FAKER.options().option(CastMemberType.ACTOR)
                }

                fun wesley(): CastMember {
                    return CastMember.with(WESLEY)
                }

                fun gabriel(): CastMember {
                    return CastMember.with(GABRIEL)
                }

            }
        }

    }
}