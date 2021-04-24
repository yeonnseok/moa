package com.moa.common

import java.security.MessageDigest

fun hash(password: String): String {
    return try {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash: ByteArray = digest.digest(password.toByteArray(Charsets.UTF_8))
        val hexString = StringBuffer()
        for (i in hash.indices) {
            val hex = Integer.toHexString(0xff and hash[i].toInt())
            if (hex.length == 1) hexString.append('0')
            hexString.append(hex)
        }

        hexString.toString()
    } catch (ex: Exception) {
        throw RuntimeException(ex)
    }
}
