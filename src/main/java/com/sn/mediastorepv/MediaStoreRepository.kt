package com.sn.mediastorepv

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore

class MediaStoreRepository(
    private val context: Context,
    private val mediaSelectionData: MediaSelectionData?
) {

    fun getMedia(mediaType: MediaType): MutableList<Media> {
        val selection = mediaSelectionData?.selection
        val selectionArgs = mediaSelectionData?.selectionArgs?.toTypedArray()
        val sortOrder = "${MediaStore.MediaColumns.DATE_ADDED} DESC"

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

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val dateAdded = cursor.getLong(dateAddedColumn)
                val mimeType = cursor.getString(mimeTypeColumn).substringAfterLast("/")
                val size = cursor.getLong(sizeColumn)
                val uri = ContentUris.withAppendedId(mediaType.uri, id)
                val ext = name

                val media = Media(id, name, dateAdded, mimeType, size, mediaType, uri, ext)
                mediaList.add(media)
            }
        }
        return mediaList
    }

    fun deleteMedia(medias: List<Media>): Boolean {
        var deletedCount = 0
        for (media in medias) {
            val deleteResult = media.uri?.let { context.contentResolver.delete(it, null, null) }
            if (deleteResult != 0) {
                deletedCount++
            }
        }
        return deletedCount == medias.size
    }
}