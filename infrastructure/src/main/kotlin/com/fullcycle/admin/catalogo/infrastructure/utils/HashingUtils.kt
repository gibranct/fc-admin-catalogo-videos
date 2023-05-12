package com.fullcycle.admin.catalogo.infrastructure.utils

import com.google.common.hash.HashFunction
import com.google.common.hash.Hashing

class HashingUtils private constructor() {

    companion object {
        private val CHECKSUM: HashFunction = Hashing.crc32c()

        fun checksum(content: ByteArray): String {
            return CHECKSUM.hashBytes(content).toString()
        }
    }
}