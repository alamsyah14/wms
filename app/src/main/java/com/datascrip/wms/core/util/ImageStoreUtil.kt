package com.datascrip.wms.core.util

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import java.io.*

class ImageStoreUtil (val context: Context) {

    companion object {
        private const val MAX_FILE_SIZE = 1024000
    }

    fun saveToInternalStorage(bitmapImage: Bitmap, fileName: String): String? {
        val cw = ContextWrapper(context)
        val directory: File = cw.getDir("imageDir", Context.MODE_PRIVATE)
        // Create imageDir
        val filePath = File(directory, "$fileName.png")
        lateinit var fos: FileOutputStream
        try {
            fos = FileOutputStream(filePath)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return fileName
    }

    fun loadImageFromStorage(fileName: String): Bitmap? {
        val cw = ContextWrapper(context)
        val directory: File = cw.getDir("imageDir", Context.MODE_PRIVATE)
        return try {
            val f = File(directory, "$fileName.png")
            val bitmapResult = BitmapFactory.decodeStream(FileInputStream(f))
            bitmapResult
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    fun compressImage(uri: Uri, rotation: Int = 0): ByteArray? {
        val size = getImageSize(uri)
        var inSampleSize = 1
        while (size / inSampleSize > MAX_FILE_SIZE) {
            inSampleSize *= 2
        }
        val options = BitmapFactory.Options()
        options.inSampleSize = inSampleSize
        val imageStream = context.contentResolver?.openInputStream(uri)
        var bitmap = BitmapFactory.decodeStream(imageStream, null, options)
        if (bitmap != null) {
            val out = ByteArrayOutputStream()
            if (rotation == 90) {
                val matrix = Matrix()
                matrix.postRotate(90f)
                bitmap = Bitmap.createBitmap(
                    bitmap, 0, 0,
                    bitmap.width,
                    bitmap.height,
                    matrix,
                    true
                )
            }
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 80, out)
            bitmap?.recycle()
            return out.toByteArray()
        }
        return null
    }

    private fun getImageSize(chosen: Uri): Long {
        val afd = context.contentResolver?.openAssetFileDescriptor(chosen, "r")
        afd?.run {
            val fileSize = afd.length
            afd.close()
            return fileSize
        }
        return 0
    }

}