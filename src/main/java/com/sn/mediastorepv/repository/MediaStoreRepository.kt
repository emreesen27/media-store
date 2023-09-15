package com.sn.mediastorepv.repository

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.util.Log
import com.sn.mediastorepv.data.ConflictStrategy
import com.sn.mediastorepv.data.Media
import com.sn.mediastorepv.data.MediaSelectionData
import com.sn.mediastorepv.data.MediaType
import com.sn.mediastorepv.extension.getFileExtension
import com.sn.mediastorepv.util.MediaScanner
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
            val dataColumn = cursor.getColumnIndexOrThrow(mediaType.projection[5])

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val dateAdded = cursor.getLong(dateAddedColumn)
                val mimeType = cursor.getString(mimeTypeColumn)
                val size = cursor.getLong(sizeColumn)
                val uri = ContentUris.withAppendedId(mediaType.uri, id)
                val ext = name.getFileExtension()
                val data = cursor.getString(dataColumn)

                if (extCheck == null || extCheck.contains(ext)) {
                    val media = Media(
                        id = id, name = name, dateAdded = dateAdded,
                        mimeType = mimeType, size = size, mediaType = mediaType,
                        uri = uri, ext = ext, data = data
                    )
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
        val mediaData: MutableList<Pair<String, String>> = mutableListOf()
        try {
            for (media in mediaList) {
                if (media.uri == null)
                    return false

                var destinationFile = File(destinationPath, media.name)

                if (destinationFile.exists()) {
                    if (media.conflict == ConflictStrategy.SKIP) {
                        continue
                    } else if (media.conflict == ConflictStrategy.KEEP_BOTH) {
                        var copyNumber = 1
                        val baseFileName = media.name
                        while (File(destinationPath, "${copyNumber}${baseFileName}").exists()) {
                            copyNumber++
                        }
                        media.name = "${copyNumber}${baseFileName}"
                        destinationFile = File(destinationPath, media.name)
                    }
                }

                val inputStream = context.contentResolver.openInputStream(media.uri)
                val destinationUri = Uri.fromFile(destinationFile)
                val outputStream = context.contentResolver.openOutputStream(destinationUri)

                inputStream?.use { input ->
                    outputStream?.use { output -> input.copyTo(output) }
                }

                val deleteResult = context.contentResolver.delete(media.uri, null, null)
                if (deleteResult != 0) {
                    mediaData.add(Pair(destinationFile.absolutePath, media.mimeType))
                }

                if (mediaData.size == mediaList.size)
                    MediaScanner.scanMediaFiles(context, mediaData)

            }
        } catch (e: Exception) {
            Log.e("error", e.message.toString())
        }

        return mediaData.size == mediaList.size
    }

}