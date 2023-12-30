package com.sn.mediastorepv.util

import com.sn.mediastorepv.data.ConflictStrategy
import java.io.File

interface MediaOperationCallback {

    /** Triggered as the copy or move operation continues. Returns the result as a percentage */
    fun onProgress(progress: Int)

    /** Triggered if an conflict occurs while moving or copying files*/
    suspend fun fileConflict(file: File): Pair<ConflictStrategy, Boolean>
}