package com.dracula.socialnetworktwitch.core.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

fun ContentResolver.getFileName(uri: Uri): String {
    val cursor = query(uri, null, null, null, null) ?: return ""
    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    cursor.moveToFirst()
    val name = cursor.getString(nameIndex)
    cursor.close()

    return name
}

suspend fun Uri.asFile(context: Context): File? {
    var file: File? = null
    val uri = this
    withContext(Dispatchers.IO) {
        context.contentResolver.openFileDescriptor(uri, "r")?.let { fd ->
            val inputStream = FileInputStream(fd.fileDescriptor)
            val f = File(context.cacheDir, context.contentResolver.getFileName(uri))
            val outputStream = FileOutputStream(f)
            inputStream.copyTo(outputStream)
            file = f
            fd.close()
        }
    }
    return file

}