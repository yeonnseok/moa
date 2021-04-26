package com.moa.common

import java.security.MessageDigest
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

fun String.hashed(): String {
    return try {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash: ByteArray = digest.digest(this.toByteArray(Charsets.UTF_8))
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

fun <R> Throwable.multiCatch(vararg classes: KClass<*>, block: () -> R): R {
    if (classes.any { this::class.isSubclassOf(it) }) {
        return block()
    } else throw this
}
