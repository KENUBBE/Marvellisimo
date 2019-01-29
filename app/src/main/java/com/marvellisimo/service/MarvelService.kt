package com.marvellisimo.service

import java.security.MessageDigest

class MarvelService {

    fun generateHashKey(): String {
        var hexString = StringBuilder("")
        val md5 = MessageDigest.getInstance("MD5")
        md5.digest("143e8ed4d3bfea5e29acde9d8f587c9c63b13719dca119f99531365ccb328f771ec231aa2".toByteArray()).forEach {
            hexString.append(String.format("%02x", it))
        }
        return hexString.toString()
    }
}