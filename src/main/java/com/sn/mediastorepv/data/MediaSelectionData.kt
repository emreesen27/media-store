package com.sn.mediastorepv.data

import android.provider.MediaStore

data class MediaSelectionData(
    val selection: String? = null,
    val selectionArgs: List<String>? = null,
    val sortOrder: String? = "${MediaStore.MediaColumns.DATE_ADDED} DESC"
)