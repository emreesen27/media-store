package com.sn.mediastorepv.extension

import com.sn.mediastorepv.data.Media
import java.io.File


fun Media.generateUniqueFileName(destinationPath: String): File {
    val baseFileName = name
    var uniqueFileName = name
    var copyNumber = 1

    while (File(destinationPath, uniqueFileName).exists()) {
        uniqueFileName = "${copyNumber++}$baseFileName"
    }

    return File(destinationPath, uniqueFileName)
}