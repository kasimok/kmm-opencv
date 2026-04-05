package com.example.kmmopencv

import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfByte
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc

actual class EdgeDetector actual constructor() {

    actual fun detectEdges(imageBytes: ByteArray, lowThreshold: Double, highThreshold: Double): ByteArray {
        // Decode image bytes to Mat
        val mat = Imgcodecs.imdecode(MatOfByte(*imageBytes), Imgcodecs.IMREAD_COLOR)

        // Convert to grayscale
        val gray = Mat()
        Imgproc.cvtColor(mat, gray, Imgproc.COLOR_BGR2GRAY)

        // Apply Canny edge detection
        val edges = Mat()
        Imgproc.Canny(gray, edges, lowThreshold, highThreshold)

        // Encode result to PNG bytes
        val result = MatOfByte()
        Imgcodecs.imencode(".png", edges, result)

        return result.toArray()
    }
}
