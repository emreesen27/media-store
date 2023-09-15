package com.sn.mediastorepv.util

import android.content.Context
import android.media.MediaScannerConnection
import android.util.Log

object MediaScanner {

    fun scanMediaFiles(context: Context, mediaData: List<Pair<String, String>>) {
        val filePaths = mediaData.map { it.first }.toTypedArray()
        val fileTypes = mediaData.map { it.second }.toTypedArray()
        MediaScannerConnection.scanFile(
            context,
            filePaths,
            fileTypes
        ) { path, _ ->
            Log.d("MediaScanner", "File path: $path")
        }
    }

}