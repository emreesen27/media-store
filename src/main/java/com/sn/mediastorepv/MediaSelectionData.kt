package com.sn.mediastorepv

data class MediaSelectionData(
    val selection: String,
    val selectionArgs: List<String>
) {
    constructor(
        columnName: String,
        comparison: String,
        value: String
    ) : this("$columnName $comparison ?", listOf(value))
}

/*
constructor(columnName: String, comparison: String, value: String) : this(
        String.format("%s %s ?", columnName, comparison),
        listOf(value)
    )
*
 */