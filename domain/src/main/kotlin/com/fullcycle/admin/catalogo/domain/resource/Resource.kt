package com.fullcycle.admin.catalogo.domain.resource

import com.fullcycle.admin.catalogo.domain.ValueObject

data class Resource constructor(
    val content: ByteArray,
    val checksum: String,
    val contentType: String,
    val name: String
) : ValueObject() {

    companion object {

        fun with(
            content: ByteArray,
            checksum: String,
            contentType: String,
            name: String
        ) = Resource(content, checksum, contentType, name)

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Resource

        if (!content.contentEquals(other.content)) return false
        if (checksum != other.checksum) return false
        if (contentType != other.contentType) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = content.contentHashCode()
        result = 31 * result + checksum.hashCode()
        result = 31 * result + contentType.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }

}