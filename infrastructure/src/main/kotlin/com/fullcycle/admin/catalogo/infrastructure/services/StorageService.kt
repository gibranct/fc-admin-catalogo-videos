package com.fullcycle.admin.catalogo.infrastructure.services

import com.fullcycle.admin.catalogo.domain.resource.Resource

interface StorageService {

    fun store(id: String, resource: Resource)

    fun get(id: String): Resource?

    fun list(prefix: String): List<String>

    fun deleteAll(ids: List<String>)

}