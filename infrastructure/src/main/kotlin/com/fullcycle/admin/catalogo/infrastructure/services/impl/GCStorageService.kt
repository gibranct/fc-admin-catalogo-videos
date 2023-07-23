package com.fullcycle.admin.catalogo.infrastructure.services.impl

import com.fullcycle.admin.catalogo.domain.resource.Resource
import com.fullcycle.admin.catalogo.infrastructure.services.StorageService
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage

class GCStorageService(
    private val bucket: String,
    private val storage: Storage,
) : StorageService {

    override fun store(id: String, resource: Resource) {
        val info = BlobInfo.newBuilder(this.bucket, id)
            .setContentType(resource.contentType)
            .setCrc32cFromHexString(resource.checksum)
            .build()

        this.storage.create(info, resource.content)
    }

    override fun get(id: String): Resource? {
        return storage[bucket, id]
            ?.let { blob ->
                Resource.with(
                    blob.getContent(),
                    blob.crc32cToHexString,
                    blob.contentType,
                    blob.name
                )
            }
    }

    override fun list(prefix: String): List<String> {
        val blobs = this.storage.list(bucket, Storage.BlobListOption.prefix(prefix))
        return blobs.values.map(BlobInfo::getBlobId).map(BlobId::getName)
    }

    override fun deleteAll(ids: List<String>) {
        if (ids.isEmpty()) return

        val blobs = ids.map { id -> BlobId.of(bucket, id) }.toList()

        storage.delete(blobs)
    }
}