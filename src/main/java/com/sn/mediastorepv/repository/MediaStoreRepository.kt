package com.sn.mediastorepv.repository

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import com.sn.mediastorepv.data.Media
import com.sn.mediastorepv.data.MediaSelectionData
import com.sn.mediastorepv.data.MediaType
import com.sn.mediastorepv.extension.getFileExtension
import java.io.File

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

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val dateAdded = cursor.getLong(dateAddedColumn)
                val mimeType = cursor.getString(mimeTypeColumn)
                val size = cursor.getLong(sizeColumn)
                val uri = ContentUris.withAppendedId(mediaType.uri, id)
                val ext = name.getFileExtension()

                if (extCheck == null || extCheck.contains(ext)) {
                    val media = Media(id, name, dateAdded, mimeType, size, mediaType, uri, ext)
                    mediaList.add(media)
                }
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

    fun moveMedia(mediaList: List<Media>, destinationPath: String): Boolean {
        var movedCount = 0
        for (media in mediaList) {
            if (media.uri == null)
                return false

            val inputStream = context.contentResolver.openInputStream(media.uri)
            val destinationFile = File(destinationPath, media.name)
            val destinationUri = Uri.fromFile(destinationFile)

            val outputStream = context.contentResolver.openOutputStream(destinationUri)
            inputStream?.use { input ->
                outputStream?.use { output ->
                    input.copyTo(output)
                }
            }

            val deleteResult = context.contentResolver.delete(media.uri, null, null)
            if (deleteResult != 0) {
                movedCount++
            }

        }
        return movedCount == mediaList.size
    }

}