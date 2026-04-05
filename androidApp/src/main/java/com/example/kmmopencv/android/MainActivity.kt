package com.example.kmmopencv.android

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.kmmopencv.EdgeDetector
import org.opencv.android.OpenCVLoader

class MainActivity : AppCompatActivity() {

    private val edgeDetector = EdgeDetector()
    private var originalImageBytes: ByteArray? = null

    private lateinit var originalImageView: ImageView
    private lateinit var edgesImageView: ImageView
    private lateinit var thresholdSeekBar: SeekBar

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri ?: return@registerForActivityResult
        val bytes = contentResolver.openInputStream(uri)?.readBytes() ?: return@registerForActivityResult
        originalImageBytes = bytes

        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        originalImageView.setImageBitmap(bitmap)

        processEdges()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        OpenCVLoader.initLocal()
        setContentView(R.layout.activity_main)

        originalImageView = findViewById(R.id.originalImage)
        edgesImageView = findViewById(R.id.edgesImage)
        thresholdSeekBar = findViewById(R.id.thresholdSeekBar)
        val pickButton: Button = findViewById(R.id.pickButton)

        pickButton.setOnClickListener {
            pickImage.launch("image/*")
        }

        thresholdSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) processEdges()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun processEdges() {
        val bytes = originalImageBytes ?: return
        val threshold = thresholdSeekBar.progress.toDouble().coerceAtLeast(1.0)
        val resultBytes = edgeDetector.detectEdges(bytes, threshold, threshold * 3)
        val bitmap = BitmapFactory.decodeByteArray(resultBytes, 0, resultBytes.size)
        edgesImageView.setImageBitmap(bitmap)
    }
}
