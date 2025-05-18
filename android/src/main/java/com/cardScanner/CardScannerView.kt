package com.cardScanner

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.facebook.react.ReactActivity
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerModule
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.myreactnativepackage1.R
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@SuppressLint("ViewConstructor")
class CardScannerView @JvmOverloads constructor(
  val context: ReactContext, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
  private lateinit var previewView: PreviewView
  private val cameraExecutor: ExecutorService

  init {
    LayoutInflater.from(context).inflate(R.layout.card_scanner, this, true)
    cameraExecutor = Executors.newSingleThreadExecutor()
  }

  private fun startCamera1() {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(getActivity()!!)

    cameraProviderFuture.addListener({
      val cameraProvider = cameraProviderFuture.get()
      val preview = Preview.Builder().build().also {
        it.surfaceProvider = previewView.surfaceProvider
      }


      val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

      try {
        Log.d("cardScannerLogs", "Binding started")

        cameraProvider.unbindAll()
        getActivity()?.let {
          cameraProvider.bindToLifecycle(
            it as LifecycleOwner, cameraSelector, preview
          )
          Log.d("cardScannerLogs", "Binding ended")

        }
      } catch (e: Exception) {
        Log.d("cardScannerLogs", e.message.toString())
        e.printStackTrace()
      }
    }, ContextCompat.getMainExecutor(context))
  }

  private fun startScanAnimation() {
//    val scanLine = findViewById<View>(R.id.scanLine)
//    val container = findViewById<FrameLayout>(R.id.cameraContainer)
//
//    scanLine?.post {
//      val height = container?.height
//      val animator = ObjectAnimator.ofFloat(scanLine, "translationY", 0f, height!!.toFloat())
//      animator.duration = 2000L
//      animator.repeatCount = ObjectAnimator.INFINITE
//      animator.repeatMode = ObjectAnimator.REVERSE
//      animator.interpolator = LinearInterpolator()
//      animator.start()
    //   }
  }

  @SuppressLint("SetTextI18n")
  private fun displayData(cardNumber: String?, name: String?, expiry: String?) {
//    val cardNumberTv = findViewById<TextView>(R.id.cardNumber)
//    val cardExp = findViewById<TextView>(R.id.cardExp)
//    val cardName = findViewById<TextView>(R.id.cardName)
//    cardNumber?.let {
//      cardNumberTv?.text = "Card Number: $it"
//    } ?: {
//      cardNumberTv?.text = "Card Number: Not Found"
//    }
//
//    name?.let {
//      cardName?.text = "Card name: $it"
//    } ?: {
//      cardName?.text = "Card name: Not Found"
//    }
//
//    expiry?.let {
//      cardExp?.text = "Ca`rd expire: $it"
//    } ?: {
//      cardExp?.text = "Card expire: Not Found"
//    }
  }

  private fun getActivity(): Activity? {
    return (context as? ThemedReactContext)?.currentActivity
  }


  private fun installHierarchyFitter(view: ViewGroup) {

    // only react-native setup

    if (view.context is ThemedReactContext) {

      view.setOnHierarchyChangeListener(object : ViewGroup.OnHierarchyChangeListener {

        override fun onChildViewRemoved(parent: View?, child: View?) = Unit

        override fun onChildViewAdded(parent: View?, child: View?) {

          parent?.measure(

            View.MeasureSpec.makeMeasureSpec(parent.measuredWidth, View.MeasureSpec.EXACTLY),

            View.MeasureSpec.makeMeasureSpec(parent.measuredHeight, View.MeasureSpec.EXACTLY)

          )

          parent?.layout(parent.left, parent.top, parent.right, parent.bottom)

        }

      })

    }

  }

  override fun requestLayout() {
    super.requestLayout()
    post(measureAndLayout)
  }

  private val measureAndLayout = Runnable {
    measure(
      MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
      MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
    )
    layout(left, top, right, bottom)
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    var maxWidth = 0
    var maxHeight = 0
    for (i in 0 until childCount) {
      val child = getChildAt(i)
      if (child.visibility != GONE) {
        child.measure(widthMeasureSpec, MeasureSpec.UNSPECIFIED)
        maxWidth = maxWidth.coerceAtLeast(child.measuredWidth)
        maxHeight = maxHeight.coerceAtLeast(child.measuredHeight)
      }
    }
    val finalWidth = maxWidth.coerceAtLeast(suggestedMinimumWidth)
    val finalHeight = maxHeight.coerceAtLeast(suggestedMinimumHeight)
    setMeasuredDimension(finalWidth, finalHeight)
    (context as ThemedReactContext).runOnNativeModulesQueueThread {
      (context as ThemedReactContext).getNativeModule(UIManagerModule::class.java)
        ?.updateNodeSize(id, finalWidth, finalHeight)
    }
  }
}
