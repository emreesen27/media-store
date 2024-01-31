package com.sn.mediastorepv.repository

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.sn.mediastorepv.data.ConflictStrategy
import com.sn.mediastorepv.data.Media
import com.sn.mediastorepv.data.MediaSelectionData
import com.sn.mediastorepv.data.MediaType
import com.sn.mediastorepv.extension.getExtension

class MediaStoreRepository(
    private val context: Context,
    private val mediaSelectionData: MediaSelectionData,
    private val extCheck: List<String>?
) {

    fun getMedia(mediaType: MediaType): MutableList<Media> {
        val selection = mediaSelectionData.selection
        val selectionArgs = mediaSelectionData.selectionArgs?.toTypedArray()
        val sortOrder = mediaSelectionData.sortOrder

        val mediaList = mutableListOf<Media>()

        context.contentResolver.query(
            mediaType.uri,
            mediaType.projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(mediaType.projection[0])
            val nameColumn = cursor.getColumnIndexOrThrow(mediaType.projection[1])
            val dateAddedColumn = cursor.getColumnIndexOrThrow(mediaType.projection[2])
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(mediaType.projection[3])
            val sizeColumn = cursor.getColumnIndexOrThrow(mediaType.projection[4])
            val dataColumn = cursor.getColumnIndexOrThrow(mediaType.projection[5])
            val dateModifiedColumn = cursor.getColumnIndexOrThrow(mediaType.projection[6])

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val dateAdded = cursor.getLong(dateAddedColumn)
                val mimeType = cursor.getString(mimeTypeColumn)
                val size = cursor.getLong(sizeColumn)
                val uri = ContentUris.withAppendedId(mediaType.uri, id)
                val ext = name.getExtension()
                val data = cursor.getString(dataColumn)
                val dateModified = cursor.getLong(dateModifiedColumn)

                if (extCheck == null || extCheck.contains(ext)) {
                    if (!data.isNullOrEmpty() && !uri.path.isNullOrEmpty()) {
                        val media = Media(
                            id = id,
                            name = name,
                            dateAdded = dateAdded,
                            mimeType = mimeType,
                            size = size,
                            mediaType = mediaType,
                            uri = uri,
                            ext = ext,
                            data = data,
                            dateModified = dateModified,
                            isSelected = false,
                            conflictStrategy = ConflictStrategy.OVERWRITE
                        )
                        mediaList.add(media)
                    }
                }
            }
        }
        return mediaList
    }

    fun searchInPath(
        fileName: String,
        rootPath: String,
        vararg mediaTypes: MediaType
    ): MutableList<String> {
        val pathList = mutableListOf<String>()
        for (mediaType in mediaTypes) {
            val selection =
                "${mediaType.projection[1]} LIKE ? AND ${MediaStore.MediaColumns.DATA} LIKE ?"
            val selectionArgs = arrayOf("%$fileName%", "%$rootPath%")

            context.contentResolver.query(
                mediaType.uri,
                mediaType.projection,
                selection,
                selectionArgs,
                null
            )?.use { cursor ->
                while (cursor.moveToNext()) {
                    val path =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))
                    pathList.add(path)
                }
            }
        }

        return pathList
    }

}