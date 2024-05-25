package com.sn.mediastorepv.data

data class MediaSelectionData(
    val selection: String? = null,
    val selectionArgs: List<String>? = null,
    val sortOrder: String? = OrderStrategy.dateModified(OrderStrategy.DESC)
)