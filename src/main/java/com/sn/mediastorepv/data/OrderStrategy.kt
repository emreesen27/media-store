package com.sn.mediastorepv.data

import android.provider.MediaStore

object OrderStrategy {
    const val DESC = "DESC"
    const val ASC = "ASC"

    fun dateModified(sortBy: String) = "${MediaStore.MediaColumns.DATE_MODIFIED} $sortBy"

    fun name(sortBy: String) = "${MediaStore.MediaColumns.DISPLAY_NAME} $sortBy"

    fun extension(sortBy: String) = "${MediaStore.MediaColumns.MIME_TYPE} $sortBy"

}