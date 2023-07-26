package com.fullcycle.admin.catalogo.infrastructure.configuration

import com.nimbusds.jose.shaded.json.JSONObject
import org.springframework.context.annotation.Bean
import org.springframework.core.convert.converter.Converter
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtClaimNames
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
class SecurityConfig {

    companion object {
        private const val ROLE_ADMIN = "CATALOGO_ADMIN"
        private const val ROLE_CAST_MEMBERS = "CATALOGO_CAST_MEMBERS"
        private const val ROLE_CATEGORIES = "CATALOGO_CATEGORIES"
        private const val ROLE_GENRES = "CATALOGO_GENRES"
        private const val ROLE_VIDEOS = "CATALOGO_VIDEOS"
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { csrf -> csrf.disable() }
            .authorizeHttpRequests { authorize ->
                authorize
                    .antMatchers("/cast_members*").hasAnyRole(ROLE_ADMIN, ROLE_CAST_MEMBERS)
                    .antMatchers("/categories*").hasAnyRole(ROLE_ADMIN, ROLE_CATEGORIES)
                    .antMatchers("/genres*").hasAnyRole(ROLE_ADMIN, ROLE_GENRES)
                    .antMatchers("/videos*").hasAnyRole(ROLE_ADMIN, ROLE_VIDEOS)
                    .anyRequest().hasRole(ROLE_ADMIN)
            }
            .oauth2ResourceServer { oauth ->
                oauth.jwt()
                    .jwtAuthenticationConverter(KeycloakJwtConverter())
            }
            .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .headers { headers -> headers.frameOptions().sameOrigin() }
            .build()
    }


    internal class KeycloakJwtConverter : Converter<Jwt?, AbstractAuthenticationToken?> {
        private val authoritiesConverter: KeycloakAuthoritiesConverter = KeycloakAuthoritiesConverter()

        override fun convert(jwt: Jwt): AbstractAuthenticationToken {
            return JwtAuthenticationToken(jwt, extractAuthorities(jwt), extractPrincipal(jwt))
        }

        private fun extractPrincipal(jwt: Jwt): String {
            return jwt.getClaimAsString(JwtClaimNames.SUB)
        }

        private fun extractAuthorities(jwt: Jwt): Collection<GrantedAuthority?> {
            return authoritiesConverter.convert(jwt)
        }
    }


    @Suppress("UNCHECKED_CAST")
    internal class KeycloakAuthoritiesConverter : Converter<Jwt?, Collection<GrantedAuthority?>?> {
        override fun convert(jwt: Jwt): Collection<GrantedAuthority> {
            val realmRoles: List<String> = extractRealmRoles(jwt)
            val resourceRoles: List<String> = extractResourceRoles(jwt)
            return (realmRoles + resourceRoles)
                .map { role -> SimpleGrantedAuthority(ROLE_PREFIX + role.uppercase()) }
                .toSet()
        }

        private fun extractResourceRoles(jwt: Jwt): List<String> {
            return jwt.getClaimAsMap(RESOURCE_ACCESS)
                .map {
                    val key = it.key
                    val value = it.value as JSONObject
                    val roles = value[ROLES] as List<String>
                    roles.map { role ->
                        key.plus(SEPARATOR).plus(role)
                    }
                }.flatten()

        }

        private fun extractRealmRoles(jwt: Jwt): List<String> {
            return jwt.getClaimAsMap(REALM_ACCESS)
                .getOrDefault(ROLES, emptyList<String>())
                as List<String>
        }

        companion object {
            private const val REALM_ACCESS = "realm_access"
            private const val ROLES = "roles"
            private const val RESOURCE_ACCESS = "resource_access"
            private const val SEPARATOR = "_"
            private const val ROLE_PREFIX = "ROLE_"
        }
    }


}