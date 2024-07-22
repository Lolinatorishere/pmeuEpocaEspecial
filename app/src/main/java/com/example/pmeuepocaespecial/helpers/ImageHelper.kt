package com.example.pmeuepocaespecial.helpers

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream

class ImageHelper(context: Context) {
    fun encodeImageToByteArray(imageUri: Uri, context: Context): ByteArray {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        return outputStream.toByteArray();
    }

    fun decodeByteArrayToImage(encodedImage: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(encodedImage, 0,encodedImage.size)
    }
}