package com.cardScanner


import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewTreeObserver
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.facebook.react.bridge.ReactContext


@SuppressLint("ViewConstructor")
class CameraPreviewView constructor(context: Context, attrs: AttributeSet?) :
  ViewGroup(context, attrs) {
  private val previewView = PreviewView(context).apply {
    layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
    scaleType = PreviewView.ScaleType.FILL_CENTER  // or FIT_CENTER, FILL_START

  }
  private var listener: OnCardScanned? = null
  private val cameraExecutor = ContextCompat.getMainExecutor(context)
  private val TAG = "CameraPreviewView"

  init {
    previewView.apply {
      layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
    }
    addView(previewView)
    setupCamera()
  }

  private fun setupCamera() {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

    cameraProviderFuture.addListener({
      try {
        val cameraProvider = cameraProviderFuture.get()

        val preview = Preview.Builder().build().apply {

          setSurfaceProvider(previewView.surfaceProvider)
        }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        val imageAnalyzer =
          ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build().also {
              it.setAnalyzer(
                cameraExecutor, CardAnalyzer(onDataReady = { cardNumber, name, expiry ->
                  listener?.onScanned(cardNumber, name, expiry)
                })
              )
            }

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
          (context as ReactContext).currentActivity as LifecycleOwner,
          cameraSelector,
          preview,
          imageAnalyzer
        )

        // 👇 Fix: Trigger re-layout to avoid black screen
        previewView.viewTreeObserver.addOnGlobalLayoutListener(object :
          ViewTreeObserver.OnGlobalLayoutListener {
          override fun onGlobalLayout() {
            previewView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            previewView.requestLayout()
            previewView.invalidate()
            previewView.forceLayout()

            val parent = previewView.parent as? ViewGroup
            parent?.requestLayout()
            parent?.invalidate()
          }
        })

        // 👇 Optional delayed fallback
        Handler(Looper.getMainLooper()).postDelayed({
          previewView.requestLayout()
          previewView.invalidate()
        }, 200)

      } catch (e: Exception) {
        Log.e(TAG, "Failed to bind camera use cases", e)
      }
    }, cameraExecutor)
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val width = MeasureSpec.getSize(widthMeasureSpec)
    val height = MeasureSpec.getSize(heightMeasureSpec)

    setMeasuredDimension(width, height)
    Log.d(TAG, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY).toString())

    previewView.measure(
      MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
      MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
    )
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    Log.d(TAG, height.toString())
    previewView.layout(0, 0, r - l, b - t)
  }

  fun setOnCardScannedListener(listener: OnCardScanned) {
    this.listener = listener
  }

  interface OnCardScanned {
    fun onScanned(cardNumber: String?, name: String?, expiry: String?)
  }
}



