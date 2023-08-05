package com.sn.mediastorepv

import android.content.Context
import com.sn.mediastorepv.data.MediaSelectionData
import com.sn.mediastorepv.repository.MediaStoreRepository

class MediaStoreBuilder(private val context: Context) {

    private var mediaSelectionData: MediaSelectionData? = null

    fun setMediaSelectionData(mediaSelectionData: MediaSelectionData): MediaStoreBuilder {
        this.mediaSelectionData = mediaSelectionData
        return this
    }

    fun build(): MediaStoreRepository {
        return MediaStoreRepository(context, mediaSelectionData)
    }
}