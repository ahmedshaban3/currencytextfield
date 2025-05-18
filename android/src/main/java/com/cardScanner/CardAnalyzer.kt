package com.cardScanner

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class CardAnalyzer(
  private val onDataReady: (
    cardNumber: String?, name: String?, expiry: String?
  ) -> Unit
) : ImageAnalysis.Analyzer {
  private val recognizer = TextRecognition.getClient(TextRecognizerOptions.Builder().build())

  @OptIn(ExperimentalGetImage::class)
  override fun analyze(imageProxy: ImageProxy) {
    Log.d("CardAnalyzer", "Analyzer triggered") // <- Add this
    val mediaImage = imageProxy.image ?: return
    val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

    recognizer.process(image).addOnSuccessListener { visionText ->
      val rawText = visionText.text

      val cardNumber = Regex("\\b\\d{4} ?\\d{4} ?\\d{4} ?\\d{4}\\b").find(rawText)?.value
      val expiry = Regex("\\b(0[1-9]|1[0-2])/(\\d{2,4})\\b").find(rawText)?.value

      val name = rawText.lines().filter { it.contains(" ") }
        .filter { line -> line.asIterable().none { char -> char.isDigit() } }
        .maxByOrNull { it.length }
      onDataReady(cardNumber, name, expiry)
    }.addOnCompleteListener {
      imageProxy.close()
    }
  }
}
