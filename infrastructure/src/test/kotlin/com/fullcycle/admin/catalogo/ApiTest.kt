package com.fullcycle.admin.catalogo

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt

interface ApiTest {

   companion object {

       val ADMIN_JWT: JwtRequestPostProcessor = jwt().authorities(SimpleGrantedAuthority("ROLE_CATALOGO_ADMIN"))
       val CATEGORIES_JWT: JwtRequestPostProcessor = jwt().authorities(SimpleGrantedAuthority("ROLE_CATALOGO_CATEGORIES"))
       val GENRES_JWT: JwtRequestPostProcessor = jwt().authorities(SimpleGrantedAuthority("ROLE_CATALOGO_GENRES"))
       val CAST_MEMBERS_JWT: JwtRequestPostProcessor = jwt().authorities(SimpleGrantedAuthority("ROLE_CATALOGO_CAST_MEMBERS"))
       val VIDEO_JWT: JwtRequestPostProcessor = jwt().authorities(SimpleGrantedAuthority("ROLE_CATALOGO_VIDEOS"))
       
   }


}