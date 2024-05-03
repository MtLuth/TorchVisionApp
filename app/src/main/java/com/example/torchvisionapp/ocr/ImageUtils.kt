package com.example.torchvisionapp.ocr

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import java.io.File
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.TransformToGrayscaleOp

abstract class ImageUtils {
    companion object {
        private fun decodeExifOrientation(orientation: Int): Matrix {
            val matrix = Matrix()

            when (orientation) {
                ExifInterface.ORIENTATION_NORMAL, ExifInterface.ORIENTATION_UNDEFINED -> Unit
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90F)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180F)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270F)
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.postScale(-1F, 1F)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.postScale(1F, -1F)
                ExifInterface.ORIENTATION_TRANSPOSE -> {
                    matrix.postScale(-1F, 1F)
                    matrix.postRotate(270F)
                }

                ExifInterface.ORIENTATION_TRANSVERSE -> {
                    matrix.postScale(-1F, 1F)
                    matrix.postRotate(90F)
                }


                else -> throw IllegalArgumentException("Invalid orientation: $orientation")
            }

            return matrix
        }

        fun bitmapToTensorImageForRecognition(
            bitmapIn: Bitmap, width: Int, height: Int, mean: Float, std: Float
        ): TensorImage {
            val imageProcessor = ImageProcessor.Builder()
                .add(ResizeOp(height, width, ResizeOp.ResizeMethod.BILINEAR))
                .add(TransformToGrayscaleOp()).add(NormalizeOp(mean, std)).build()
            var tensorImage = TensorImage(DataType.FLOAT32)

            tensorImage.load(bitmapIn)
            tensorImage = imageProcessor.process(tensorImage)

            return tensorImage
        }

        fun bitmapToTensorImageForDetection(
            bitmapIn: Bitmap, width: Int, height: Int, means: FloatArray, stds: FloatArray
        ): TensorImage {
            val imageProcessor = ImageProcessor.Builder()
                .add(ResizeOp(height, width, ResizeOp.ResizeMethod.BILINEAR))
                .add(NormalizeOp(means, stds)).build()
            var tensorImage = TensorImage(DataType.FLOAT32)

            tensorImage.load(bitmapIn)
            tensorImage = imageProcessor.process(tensorImage)

            return tensorImage
        }

        fun createEmptyBitmap(
            imageWidth: Int,
            imageHeigth: Int,
            color: Int = 0,
            imageConfig: Bitmap.Config = Bitmap.Config.RGB_565
        ): Bitmap {
            val ret = Bitmap.createBitmap(imageWidth, imageHeigth, imageConfig)
            if (color != 0) {
                ret.eraseColor(color)
            }
            return ret
        }
    }
}
