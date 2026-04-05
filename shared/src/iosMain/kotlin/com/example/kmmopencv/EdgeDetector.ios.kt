package com.example.kmmopencv

import OpenCVWrapper.OpenCVWrapper as Wrapper
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.create
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
actual class EdgeDetector actual constructor() {

    actual fun detectEdges(imageBytes: ByteArray, lowThreshold: Double, highThreshold: Double): ByteArray {
        val inputData = imageBytes.usePinned { pinned ->
            NSData.create(bytes = pinned.addressOf(0), length = imageBytes.size.toULong())
        }

        val resultData = Wrapper.detectEdgesFromData(inputData, lowThreshold, highThreshold)
            ?: return byteArrayOf()

        val length = resultData.length.toInt()
        val result = ByteArray(length)
        result.usePinned { pinned ->
            memcpy(pinned.addressOf(0), resultData.bytes, resultData.length)
        }
        return result
    }
}
