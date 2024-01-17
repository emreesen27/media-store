package com.sn.mediastorepv.extension

import android.webkit.MimeTypeMap
import kotlin.String

fun String.getFileExtension(): String? {
    val lastDotIndex = this.lastIndexOf(".")
    if (lastDotIndex >= 0) {
        return this.substring(lastDotIndex + 1)
    }
    return null
}

fun String.getExtension(): String? {
    val extension = MimeTypeMap.getFileExtensionFromUrl(this)
    return if (extension.isNullOrEmpty()) this.getFileExtension() else extension
}