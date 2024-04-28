package com.example.torchvisionapp.ocr

import android.graphics.Bitmap

data class ModelExecutionResult(
    val bitmapResult: Bitmap,   // a image for result
    val executionLog: String,
    val itemsFound: Map<String, Int>,   // a map between words and colors of the items found.
)
