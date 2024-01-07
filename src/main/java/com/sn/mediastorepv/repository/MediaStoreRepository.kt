package com.sn.mediastorepv.repository

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import com.sn.mediastorepv.data.ConflictStrategy
import com.sn.mediastorepv.data.Media
import com.sn.mediastorepv.data.MediaSelectionData
import com.sn.mediastorepv.data.MediaType
import com.sn.mediastorepv.extension.copyToWithProgress
import com.sn.mediastorepv.extension.generateUniqueFileName
import com.sn.mediastorepv.extension.getFileExtension
import com.sn.mediastorepv.util.MediaOperationCallback
import java.io.File

class MediaStoreRepository(
    private val context: Context,
    private val mediaSelectionData: MediaSelectionData,
    private val extCheck: List<String>?
) {

    private var strategy: Pair<ConflictStrategy, Boolean> = Pair(ConflictStrategy.OVERWRITE, false)

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
            val directoryPathColumn = cursor.getColumnIndexOrThrow(mediaType.projection[7])

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val dateAdded = cursor.getLong(dateAddedColumn)
                val mimeType = cursor.getString(mimeTypeColumn)
                val size = cursor.getLong(sizeColumn)
                val uri = ContentUris.withAppendedId(mediaType.uri, id)
                val ext = name.getFileExtension()
                val data = cursor.getString(dataColumn)
                val dateModified = cursor.getLong(dateModifiedColumn)
                val directoryPath = cursor.getString(directoryPathColumn)

                if (extCheck == null || extCheck.contains(ext)) {
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
                        directoryPath = directoryPath
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

    suspend fun moveMedia(
        mediaList: List<Media>,
        destinationPath: String,
        callback: MediaOperationCallback,
        isCopy: Boolean
    ): MutableList<Pair<String, String>>? {
        val mediaData: MutableList<Pair<String, String>> = mutableListOf()
        try {
            val totalSize: Long = mediaList.sumOf { it.size }

            for (media in mediaList) {
                if (media.uri == null)
                    return null

                var destinationFile = File(destinationPath, media.name)

                if (destinationFile.exists()) {
                    if (!strategy.second) {
                        strategy = callback.fileConflict(destinationFile)
                    }

                    if (strategy.first == ConflictStrategy.SKIP) {
                        continue
                    } else if (strategy.first == ConflictStrategy.KEEP_BOTH) {
                        destinationFile = media.generateUniqueFileName(destinationPath)
                    }
                }

                val inputStream = context.contentResolver.openInputStream(media.uri)
                val destinationUri = Uri.fromFile(destinationFile)
                val outputStream = context.contentResolver.openOutputStream(destinationUri)

                inputStream?.use { input ->
                    outputStream?.use { output ->
                        input.copyToWithProgress(output, totalSize) { progress ->
                            callback.onProgress(progress)
                        }
                    }
                }

                mediaData.add(Pair(destinationFile.absolutePath, media.mimeType))
                if (!isCopy) {
                    context.contentResolver.delete(media.uri, null, null)
                }

            }
        } catch (e: Exception) {
            return null
        }
        return mediaData
    }

}