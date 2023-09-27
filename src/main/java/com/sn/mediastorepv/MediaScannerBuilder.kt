package com.sn.mediastorepv

import android.content.Context
import com.sn.mediastorepv.repository.MediaScannerRepository
import com.sn.mediastorepv.util.MediaScanCallback

class MediaScannerBuilder {

    private var context: Context? = null
    private var mediaScanCallback: MediaScanCallback? = null
    private var mediaList: List<Pair<String, String>>? = null

    fun addContext(context: Context): MediaScannerBuilder {
        this.context = context
        return this
    }

    fun addMediaList(mediaList: List<Pair<String, String>>): MediaScannerBuilder {
        this.mediaList = mediaList
        return this
    }

    fun addCallback(mediaScanCallback: MediaScanCallback): MediaScannerBuilder {
        this.mediaScanCallback = mediaScanCallback
        return this
    }

    fun build(): MediaScannerRepository {
        val callback = requireNotNull(mediaScanCallback) { "MediaScanCallback cannot be null" }
        val ctx = requireNotNull(context) { "Context cannot be null" }
        val list = requireNotNull(mediaList) { "Media list cannot be null" }
        return MediaScannerRepository(ctx, list, callback)
    }

}