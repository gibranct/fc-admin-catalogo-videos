package com.fullcycle.admin.catalogo.infrastructure.services.local

import com.fullcycle.admin.catalogo.domain.resource.Resource
import com.fullcycle.admin.catalogo.infrastructure.services.StorageService

class InMemoryStorageService : StorageService {

    private val storage: MutableMap<String, Resource> = HashMap()

    fun clear() {
        storage.clear()
    }

    fun currentStorage(): Map<String, Resource> {
        return storage.toMap()
    }

    override fun store(id: String, resource: Resource) {
        storage[id] = resource
    }

    override fun get(id: String): Resource? {
        return storage[id]
    }

    override fun list(prefix: String): List<String> {
        return storage
            .filterKeys { it.startsWith(prefix) }
            .keys
            .toList()
    }

    override fun deleteAll(ids: List<String>) {
        ids.forEach(storage::remove)
    }
}