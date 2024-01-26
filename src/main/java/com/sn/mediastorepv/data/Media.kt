package com.sn.mediastorepv.data

import android.net.Uri

data class Media(
    var id: Long,
    var name: String,
    val dateAdded: Long,
    val mimeType: String?,
    val size: Long,
    val mediaType: MediaType,
    val uri: Uri,
    val ext: String?,
    val data: String,
    val dateModified: Long,
    var isSelected: Boolean,
    var conflictStrategy: ConflictStrategy
)