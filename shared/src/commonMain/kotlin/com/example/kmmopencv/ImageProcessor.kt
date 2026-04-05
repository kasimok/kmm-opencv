package com.example.kmmopencv

/**
 * Shared interface for OpenCV image processing.
 * Each platform provides its own implementation using OpenCV.
 */
expect class EdgeDetector() {
    /**
     * Applies Canny edge detection to the input image bytes (PNG/JPEG encoded).
     * Returns the result as PNG-encoded bytes.
     */
    fun detectEdges(imageBytes: ByteArray, lowThreshold: Double, highThreshold: Double): ByteArray
}
