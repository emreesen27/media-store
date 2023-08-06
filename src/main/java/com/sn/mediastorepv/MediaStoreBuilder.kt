package com.sn.mediastorepv

import android.content.Context
import com.sn.mediastorepv.data.MediaSelectionData
import com.sn.mediastorepv.repository.MediaStoreRepository

class MediaStoreBuilder(private val context: Context) {

    private var mediaSelectionData: MediaSelectionData = MediaSelectionData()
    private var extCheck: List<String>? = null

    fun setMediaSelectionData(mediaSelectionData: MediaSelectionData): MediaStoreBuilder {
        this.mediaSelectionData = mediaSelectionData
        return this
    }

    fun setExtCheck(extCheck: List<String>?): MediaStoreBuilder {
        this.extCheck = extCheck
        return this
    }

    fun build(): MediaStoreRepository {
        return MediaStoreRepository(context, mediaSelectionData, extCheck)
    }
}