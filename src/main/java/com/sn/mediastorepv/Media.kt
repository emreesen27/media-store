package com.sn.mediastorepv

import android.net.Uri

data class Media(
    var id: Long,
    val name: String,
    val dateAdded: Long,
    val mimeType: String,
    val size: Long,
    val mediaType: MediaType,
    val uri: Uri?,
    val ext: String?
)
