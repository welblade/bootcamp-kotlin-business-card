package com.github.welblade.businesscard.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider.getUriForFile
import com.github.welblade.businesscard.R
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.security.AccessController.getContext

class Image {
    companion object{
        fun share (context: Context, card: View) {
            val bitmap = getScreenShotFromView(card)
            bitmap?.let {
                saveMediaToStorage(context, it)
            }
        }

        private fun saveMediaToStorage(context: Context, bitmap: Bitmap) {
            val filename = "${System.currentTimeMillis()}.jpg"
            var fileOS: OutputStream? = null

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                context.contentResolver?.also { resolver ->
                    val contentValues = ContentValues().apply{
                        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                    }

                    val imageUri: Uri? = resolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
                    )

                    fileOS = imageUri?.let {
                        shareIntent(context, it)
                        resolver.openOutputStream(it)
                    }
                }
            } else {
                val imageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                val image = File(imageDir, filename)
                val contentUri: Uri =
                    getUriForFile(context, "com.github.welblade.businesscard.fileprovider", image)
                shareIntent(context, contentUri)
                fileOS = FileOutputStream(image)
            }
            fileOS?.use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                Toast.makeText(context, R.string.capture_image_successful, Toast.LENGTH_SHORT).show()
            }
        }
        private fun shareIntent(context: Context, imageUri: Uri) {
            val shareIntent: Intent = Intent().apply{
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, imageUri)
                type = "image/jpeg"
            }

            context.startActivity(
                Intent.createChooser(
                    shareIntent,
                    context.resources.getText(R.string.share)
                )
            )
        }

        private fun getScreenShotFromView(card: View): Bitmap? {
            var screenshot: Bitmap? = null
            try {
                screenshot = Bitmap.createBitmap(
                    card.measuredWidth,
                    card.measuredHeight,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(screenshot)
                card.draw(canvas)
            }catch (err: Exception){
                Log.e("Screenshot", "Erro ao tentar capturar o screenshot" + err.message)
            }
            return screenshot
        }
    }
}