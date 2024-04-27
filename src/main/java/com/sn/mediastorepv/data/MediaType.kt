package com.sn.mediastorepv.data

import android.net.Uri
import android.provider.MediaStore

enum class MediaType(val uri: Uri, val projection: Array<String>) {
    IMAGES(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_MODIFIED,
        )
    ),
    VIDEOS(
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
        arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DATE_MODIFIED,
        )
    ),
    AUDIOS(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DATE_MODIFIED,
        )
    ),
    FILES(
        MediaStore.Files.getContentUri("external"),
        arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DATE_MODIFIED,
        )
    );
}