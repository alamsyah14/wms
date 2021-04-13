package com.datascrip.wms.core.util

import android.content.Context
import java.io.FileInputStream

class LocalFileHelper (private val context: Context)  {

    fun saveFile(fileName: String, fileContents: String) {
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(fileContents.toByteArray())
        }
    }

    private fun fileExist(fileName: String) = context.getFileStreamPath(fileName).exists()

    fun loadFile(fileName: String): FileInputStream {
        // create new file when not exist and set initial content with empty string
        if (!fileExist(fileName))
            saveFile(fileName, "")

        return context.openFileInput(fileName)

    }

}

fun FileInputStream.readContentFile(): String {
    return bufferedReader().useLines {
        it.fold("") { _, text ->
            text
        }
    }
}