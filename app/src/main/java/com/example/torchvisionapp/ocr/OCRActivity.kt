package com.example.torchvisionapp.ocr

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import com.bumptech.glide.Glide
import com.example.torchvisionapp.DialogButtonClickListener
import com.example.torchvisionapp.FileItemClick
import com.example.torchvisionapp.R
import com.example.torchvisionapp.databinding.DialogAddFileBinding
import com.example.torchvisionapp.fragment.PickFolderDialogFragment
import com.example.torchvisionapp.model.DocumentFile
import com.example.torchvisionapp.model.FileItem
import com.example.torchvisionapp.viewmodel.TextConverterViewModel
import com.google.android.material.chip.Chip
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.json.JSONException
import org.json.JSONObject
import org.pytorch.IValue
import org.pytorch.LiteModuleLoader
import org.pytorch.Module
import org.pytorch.Tensor
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.LongBuffer
import java.util.concurrent.Executors

private const val TAG = "MainActivity"

public class OCRActivity : AppCompatActivity() {

    private val tfImageName = "tensorflow.jpg"
    private val androidImageName = "android.jpg"
    private val chromeImageName = "chrome.jpg"
    private lateinit var viewModel: MLExecutionViewModel
    private lateinit var resultImageView: ImageView

    //    private lateinit var tfImageView: ImageView
//    private lateinit var androidImageView: ImageView
//    private lateinit var chromeImageView: ImageView
//    private lateinit var chipsGroup: ChipGroup
    private lateinit var runButton: Button
    private lateinit var textPromptTextView: TextView

    private var useGPU = false
    private var selectedImageName = "tensorflow.jpg"
    private var ocrModel: OCRModelExecutor? = null
    private val inferenceThread = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    private val mainScope = MainScope()
    private val mutex = Mutex()

    //
//    private lateinit var viewModel: MLExecutionViewModel
    private lateinit var selectedImageUri: Uri
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_PICK = 2
    private val REQUEST_CAMERA_PERMISSION = 200

    private lateinit var fransText: TextView
    private lateinit var engText: TextView

    //    fields for translate
    private val HIDDEN_SIZE: Int = 256
    private val EOS_TOKEN = 1
    private val MAX_LENGTH = 50
    private var mModuleEncoder: Module? = null
    private var mModuleDecoder: Module? = null
    private var mInputTensor: Tensor? = null
    private var mInputTensorBuffer: LongBuffer? = null

    //    property for display UI
    private var mEditText: EditText? = null
    private var mTextView: TextView? = null
    private lateinit var actionSave: Button
    private lateinit var actionCancel: Button
    private lateinit var mButton: Button
    private var rootPath: String = ""
    private var listFolders: ArrayList<FileItem> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.tfe_is_activity_main)
        setContentView(R.layout.activity_translate)

        // Check if the app has permission to use the camera
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // If not, request the permission
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION
            )
        }

        fransText = findViewById(R.id.recordText1)
        engText = findViewById(R.id.recordText2)

        mButton = findViewById(R.id.btnTranslate)

        mButton.setOnClickListener {
            val result = translate(fransText!!.text.toString())
            showTranslationResult(result)
            mButton.isEnabled = true
        }

        actionCancel = findViewById(R.id.actionCancel)
        actionCancel.isVisible = false

        actionSave = findViewById(R.id.actionSave)
        actionSave.isVisible = false
        actionSave.setOnClickListener {
            val rootPath = applicationContext.filesDir.path
            val file = File(rootPath)
            val fileItemList: ArrayList<FileItem> = ArrayList()
            val viewModel: TextConverterViewModel = ViewModelProvider(this).get(TextConverterViewModel::class.java)
            listFolders = viewModel.loadFolderList(file)
            val pickFolderDialogFragment:PickFolderDialogFragment = PickFolderDialogFragment(listFolders)
            pickFolderDialogFragment.show(supportFragmentManager, "Pick Folder")
            pickFolderDialogFragment.setClickListener(object: DialogButtonClickListener {
                override fun onNegativeButtonClick() {

                }

                override fun onPositiveButtonClick(path: String?) {
                    if (path!=null) {
                        showAddFileDialog("txt", path)
                    }
                }
            })
        }

        actionCancel.setOnClickListener {
            val rec1: EditText = findViewById(R.id.recordText1);
            rec1.setText("");
            val rec2: EditText = findViewById(R.id.recordText2);
            rec2.setText("")
            actionCancel.isVisible = false
            actionSave.isVisible = false
        }

        val buttonPickImage: Button = findViewById(R.id.button_pick_image)
        buttonPickImage.setOnClickListener {
            dispatchPickPictureIntent()
        }

        val buttonCaptureImage: Button = findViewById(R.id.button_capture_image)
        buttonCaptureImage.setOnClickListener {
            dispatchTakePictureIntent()
        }

//        val toolbar: Toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayShowTitleEnabled(false)

//        tfImageView = findViewById(R.id.tf_imageview)
//        androidImageView = findViewById(R.id.android_imageview)
//        chromeImageView = findViewById(R.id.chrome_imageview)

//        val candidateImageViews = arrayOf<ImageView>(tfImageView, androidImageView, chromeImageView)

        val assetManager = assets
        try {
            val tfInputStream: InputStream = assetManager.open(tfImageName)
            val tfBitmap = BitmapFactory.decodeStream(tfInputStream)
//            tfImageView.setImageBitmap(tfBitmap)

            val androidInputStream: InputStream = assetManager.open(androidImageName)
            val androidBitmap = BitmapFactory.decodeStream(androidInputStream)
//            androidImageView.setImageBitmap(androidBitmap)

            val chromeInputStream: InputStream = assetManager.open(chromeImageName)
            val chromeBitmap = BitmapFactory.decodeStream(chromeInputStream)
//            chromeImageView.setImageBitmap(chromeBitmap)
        } catch (e: IOException) {
            Log.e(TAG, "Failed to open a test image")
        }

//        for (iv in candidateImageViews) {
//            setInputImageViewListener(iv)
//        }

        resultImageView = findViewById(R.id.result_imageview)
//        chipsGroup = findViewById(R.id.chips_group)
//        textPromptTextView = findViewById(R.id.text_prompt)
//        val useGpuSwitch: Switch = findViewById(R.id.switch_use_gpu)

        viewModel = AndroidViewModelFactory(application).create(MLExecutionViewModel::class.java)
        viewModel.resultingBitmap.observe(this, Observer { resultImage ->
            if (resultImage != null) {
                updateUIWithResults(resultImage)
            }
            enableControls(true)
        })

        mainScope.async(inferenceThread) { createModelExecutor(useGPU) }

//        useGpuSwitch.setOnCheckedChangeListener { _, isChecked ->
//            useGPU = isChecked
//            mainScope.async(inferenceThread) { createModelExecutor(useGPU) }
//        }

//        runButton = findViewById(R.id.rerun_button)
//        runButton.setOnClickListener {
//            enableControls(false)
//
//            mainScope.async(inferenceThread) {
//                mutex.withLock {
//                    if (ocrModel != null) {
//                        viewModel.onApplyModel(
//                            baseContext,
//                            selectedImageName,
//                            ocrModel,
//                            inferenceThread
//                        )
//                    } else {
//                        Log.d(
//                            TAG,
//                            "Skipping running OCR since the ocrModel has not been properly initialized ..."
//                        )
//                    }
//                }
//            }
//        }

//
        runButton = findViewById(R.id.rerun_button)
        runButton.setOnClickListener {
            enableControls(false)

            mainScope.async(inferenceThread) {
                mutex.withLock {
                    if (ocrModel != null) {
                        viewModel.onApplyModel(
                            baseContext, selectedImageUri, ocrModel, inferenceThread
                        )
                    } else {
                        Log.d(
                            TAG,
                            "Skipping running OCR since the ocrModel has not been properly initialized ..."
                        )
                    }
                }
            }
        }
//

        setChipsToLogView(HashMap<String, Int>())
        enableControls(true)
    }

    private fun showAddFileDialog(format: String, path: String) {
        val dialogAddFileBinding = DialogAddFileBinding.inflate(layoutInflater)
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setView(dialogAddFileBinding.root)

        val editTextFileName = dialogAddFileBinding.editTextFileName
        val btnSave = dialogAddFileBinding.btnSave
        val btnCancel = dialogAddFileBinding.btnCancel

        val alertDialog = dialogBuilder.create()

        btnSave.setOnClickListener {
            val fileName = editTextFileName.text.toString().trim()
            if (fileName.isNotEmpty()) {
                saveAsFile(fileName, format, path)
                alertDialog.dismiss()
            } else {
                Toast.makeText(this, "Please enter file name", Toast.LENGTH_SHORT).show()
            }
        }

        alertDialog.show()
    }

    private fun saveAsFile(fileName: String, format: String, path: String) {
        val docs = DocumentFile(applicationContext)
        val recgText: EditText = findViewById(R.id.recordText2)
        docs.saveAsFile(recgText.text.toString(), fileName, format, path)
        finish()
    }



    //
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted, you can now use the camera
                } else {
                    // Permission was denied, show a message to the user
                    Toast.makeText(
                        this,
                        "Camera permission is required to use this feature",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    fun dispatchPickPictureIntent() {
        Intent(
            Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        ).also { pickPictureIntent ->
            pickPictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(pickPictureIntent, REQUEST_IMAGE_PICK)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    // Convert bitmap to Uri
                    selectedImageUri = getImageUriFromBitmap(this, imageBitmap)
                    setImageView(resultImageView, imageBitmap)
                }

                REQUEST_IMAGE_PICK -> {
                    selectedImageUri = data?.data!!

                    val imageBitmap = getBitmapFromUri(this, selectedImageUri)
                    setImageView(resultImageView, imageBitmap)

                }
            }
        }
    }

    fun getBitmapFromUri(context: Context, uri: Uri): Bitmap {
        return context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        } ?: throw IllegalArgumentException("Cannot decode bitmap from uri: $uri")
    }

    fun getImageUriFromBitmap(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }


//

    @SuppressLint("ClickableViewAccessibility")
    private fun setInputImageViewListener(iv: ImageView) {
        iv.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent?): Boolean {
//                    if (v.equals(tfImageView)) {
//                        selectedImageName = tfImageName
////                        textPromptTextView.setText(getResources().getString(R.string.tfe_using_first_image))
//                    } else if (v.equals(androidImageView)) {
//                        selectedImageName = androidImageName
////                        textPromptTextView.setText(getResources().getString(R.string.tfe_using_second_image))
//                    } else if (v.equals(chromeImageView)) {
//                        selectedImageName = chromeImageName
////                        textPromptTextView.setText(getResources().getString(R.string.tfe_using_third_image))
//                    }
                return false
            }
        })
    }

    private suspend fun createModelExecutor(useGPU: Boolean) {
        mutex.withLock {
            if (ocrModel != null) {
                ocrModel!!.close()
                ocrModel = null
            }
            try {
                ocrModel = OCRModelExecutor(this, useGPU)
            } catch (e: Exception) {
                Log.e(TAG, "Fail to create OCRModelExecutor: ${e.message}")
//                val logText: TextView = findViewById(R.id.log_view)
//                logText.text = e.message
            }
        }
    }

    private fun setChipsToLogView(itemsFound: Map<String, Int>) {
//        chipsGroup.removeAllViews()

        for ((word, color) in itemsFound) {
            val chip = Chip(this)
            chip.text = word
            chip.chipBackgroundColor = getColorStateListForChip(color)
            chip.isClickable = false
//            chipsGroup.addView(chip)
        }
//        val labelsFoundTextView: TextView = findViewById(R.id.tfe_is_labels_found)
//        if (chipsGroup.childCount == 0) {
//            labelsFoundTextView.text = getString(R.string.tfe_ocr_no_text_found)
//        } else {
//            labelsFoundTextView.text = getString(R.string.tfe_ocr_texts_found)
//        }
//        chipsGroup.parent.requestLayout()
    }

    private fun getColorStateListForChip(color: Int): ColorStateList {
        val states = arrayOf(
            intArrayOf(android.R.attr.state_enabled), // enabled
            intArrayOf(android.R.attr.state_pressed) // pressed
        )

        val colors = intArrayOf(color, color)
        return ColorStateList(states, colors)
    }

    private fun setImageView(imageView: ImageView, image: Bitmap) {
        Glide.with(baseContext).load(image).override(250, 250).fitCenter().into(imageView)
    }

    private fun updateUIWithResults(modelExecutionResult: ModelExecutionResult) {
        setImageView(resultImageView, modelExecutionResult.bitmapResult)
//        val logText: TextView = findViewById(R.id.log_view)
//        logText.text = modelExecutionResult.executionLog

        setChipsToLogView(modelExecutionResult.itemsFound)

        for ((word) in modelExecutionResult.itemsFound) {
            Log.d("Recognition result:", word)
        }

        var resultString = ""
        val itemsList = modelExecutionResult.itemsFound.toList()
        val reversedItems = itemsList.reversed()

        for ((word) in reversedItems) {
            Log.d("Recognition result:", word)
            resultString += "$word "
        }

        fransText.text = resultString
        Log.d("OCR", modelExecutionResult.itemsFound.toString())

        enableControls(true)
    }

    private fun enableControls(enable: Boolean) {
        runButton.isEnabled = enable
    }

    //    handle machine translate
//    func to get model file
//    @Throws(IOException::class)
    //    func to get model file
    @Throws(IOException::class)
    private fun assetFilePath(context: Context, assetName: String): String? {
        val file = File(context.filesDir, assetName)
        if (file.exists() && file.length() > 0) {
            return file.absolutePath
        }
        context.assets.open(assetName).use { `is` ->
            FileOutputStream(file).use { os ->
                val buffer = ByteArray(4 * 1024)
                var read: Int
                while (`is`.read(buffer).also { read = it } != -1) {
                    os.write(buffer, 0, read)
                }
                os.flush()
            }
            return file.absolutePath
        }
    }


    //    func to display result
    private fun showTranslationResult(result: String?) {
        engText.text = result
    }

    fun run() {
        val result = translate(fransText.getText().toString())
        runOnUiThread {
            showTranslationResult(result)
            mButton?.setEnabled(true)
        }
    }

    //    func handle logic translate, including load model and translate by use model to predict
    private fun translate(text: String): String? {
        if (mModuleEncoder == null) {
            try {
                mModuleEncoder = LiteModuleLoader.load(assetFilePath(applicationContext, "optimized_encoder_150k.ptl"));
            } catch (e: IOException) {
                Log.e("TAG", "Error reading assets", e)
                finish()
            }
        }
        var json: String
        val wrd2idx: JSONObject
        val idx2wrd: JSONObject
        try {
            var `is` = assets.open("target_idx2wrd.json");
            var size = `is`.available()
            var buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer, charset("UTF-8"))
            idx2wrd = JSONObject(json)
            `is` = assets.open("source_wrd2idx.json");
            size = `is`.available()
            buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer, charset("UTF-8"))
            wrd2idx = JSONObject(json)
        } catch (e: JSONException) {
            Log.e(
                "TAG", "JSONException | IOException ", e
            )
            return null
        } catch (e: IOException) {
            Log.e(
                "TAG", "JSONException | IOException ", e
            )
            return null
        }
        val inputs =
            LongArray(text.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size)
        try {
            for (i in text.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray().indices) {
                inputs[i] = wrd2idx.getLong(text.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[i])
            }
        } catch (e: JSONException) {
            Log.e("machine_translation", "JSONException ", e)
            return null
        }
        val inputShape = longArrayOf(1)
        val hiddenShape = longArrayOf(1, 1, 256)
        val hiddenTensorBuffer = Tensor.allocateFloatBuffer(1 * 1 * 256)
        var hiddenTensor = Tensor.fromBlob(hiddenTensorBuffer, hiddenShape)
        val outputsShape = longArrayOf(
            50,
            256
        )
        val outputsTensorBuffer =
            Tensor.allocateFloatBuffer(50 * 256)
        for (i in inputs.indices) {
            val inputTensorBuffer = Tensor.allocateLongBuffer(1)
            inputTensorBuffer.put(inputs[i])
            val inputTensor = Tensor.fromBlob(inputTensorBuffer, inputShape)
            val outputTuple: Array<IValue> =
                mModuleEncoder!!.forward(IValue.from(inputTensor), IValue.from(hiddenTensor))
                    .toTuple()
            val outputTensor = outputTuple[0].toTensor()
            outputsTensorBuffer.put(outputTensor.dataAsFloatArray)
            hiddenTensor = outputTuple[1].toTensor()
        }
        val outputsTensor = Tensor.fromBlob(outputsTensorBuffer, outputsShape)
        val decoderInputShape = longArrayOf(1, 1)
        if (mModuleDecoder == null) {
            try {
                mModuleDecoder = LiteModuleLoader.load(assetFilePath(applicationContext, "optimized_decoder_150k.ptl"));
            } catch (e: IOException) {
                Log.e("machine_translation", "Error reading assets", e)
                finish()
            }
        }
//        mInputTensorBuffer = Tensor.allocateLongBuffer(1)
//        mInputTensorBuffer.put(0)
        var mInputTensorBuffer = Tensor.allocateLongBuffer(1)
        val localBuffer = mInputTensorBuffer
        localBuffer.put(0)
        mInputTensorBuffer = localBuffer

        mInputTensor = Tensor.fromBlob(mInputTensorBuffer, decoderInputShape)
        val result: ArrayList<Int> =
            ArrayList<Int>(50)
        for (i in 0 until 50) {
            val outputTuple: Array<IValue> = mModuleDecoder!!.forward(
                IValue.from(mInputTensor), IValue.from(hiddenTensor), IValue.from(outputsTensor)
            ).toTuple()
            val decoderOutputTensor = outputTuple[0].toTensor()
            hiddenTensor = outputTuple[1].toTensor()
            val outputs = decoderOutputTensor.dataAsFloatArray
            var topIdx = 0
            var topVal = -Double.MAX_VALUE
            for (j in outputs.indices) {
                if (outputs[j] > topVal) {
                    topVal = outputs[j].toDouble()
                    topIdx = j
                }
            }
            if (topIdx == 1) break
            result.add(topIdx)
//            mInputTensorBuffer = Tensor.allocateLongBuffer(1)
//            mInputTensorBuffer.put(topIdx.toLong())
            var mInputTensorBuffer = Tensor.allocateLongBuffer(1)
            val localBuffer = mInputTensorBuffer
            localBuffer.put(topIdx.toLong())
            mInputTensorBuffer = localBuffer

            mInputTensor = Tensor.fromBlob(mInputTensorBuffer, decoderInputShape)
        }
        var english = ""
        try {
            for (i in result.indices) english += " " + idx2wrd.getString("" + result[i])
        } catch (e: JSONException) {
            Log.e("machine_translation", "JSONException ", e)
        }
        Log.d("Trans", english)
        actionCancel.isVisible = true
        actionSave.isVisible = true
        resultImageView.setImageDrawable(null)
        return english
    }

}

