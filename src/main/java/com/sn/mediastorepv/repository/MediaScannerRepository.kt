package com.sn.mediastorepv.repository

import android.content.Context
import android.media.MediaScannerConnection
import com.sn.mediastorepv.util.MediaScanCallback

class MediaScannerRepository(
    private val context: Context,
    private val mediaList: List<Pair<String, String?>>,
    private val mediaScanCallback: MediaScanCallback
) {

    fun scanMediaFiles() {
        val filePaths = mediaList.map { it.first }.toTypedArray()
        val fileTypes = mediaList.map { it.second }.toTypedArray()
        MediaScannerConnection.scanFile(
            context,
            filePaths,
            fileTypes
        ) { path, _ ->
            mediaScanCallback.onMediaScanned(path)
        }
    }

}