package com.cardScanner

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.PixelUtil.dpToPx
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerModule
import com.myreactnativepackage1.R
import com.facebook.react.bridge.Callback
import com.facebook.react.uimanager.events.RCTEventEmitter


@SuppressLint("ViewConstructor")
class CardScannerView @JvmOverloads constructor(
  val context: ReactContext, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
  private var cardPreview: CameraPreviewView? = null
  var onCardScanned: Callback? = null

  init {
    LayoutInflater.from(context).inflate(R.layout.card_scanner, this, true)
    cardPreview = findViewById(R.id.previewView)
    cardPreview?.setOnCardScannedListener(object : CameraPreviewView.OnCardScanned {
      override fun onScanned(cardNumber: String?, name: String?, expiry: String?) {
        displayData(cardNumber, name, expiry)
      }
    })
    startScanAnimation()
  }

  private fun startScanAnimation() {
    val scanLine = findViewById<View>(R.id.scanLine)
    val container = findViewById<RelativeLayout>(R.id.container)

    scanLine?.post {
      val height = container?.height
      val animator = ObjectAnimator.ofFloat(scanLine, "translationY", 0f, height!!.toFloat())
      animator.duration = 2000L
      animator.repeatCount = ObjectAnimator.INFINITE
      animator.repeatMode = ObjectAnimator.REVERSE
      animator.interpolator = LinearInterpolator()
      animator.start()
    }
  }

  @SuppressLint("SetTextI18n")
  private fun displayData(cardNumber: String?, name: String?, expiry: String?) {
    val cardNumberTv = findViewById<TextView>(R.id.cardNumber)
    val cardExp = findViewById<TextView>(R.id.cardExpire)
    val cardName = findViewById<TextView>(R.id.cardName)
    cardNumber?.let {
      cardNumberTv?.apply {
        text = it
        visibility = VISIBLE
      }
    }
    name?.let {
      cardName?.apply {
        text = it
        visibility = VISIBLE
      }
    }
    expiry?.let {
      cardExp?.apply {
        text = it
        visibility = VISIBLE
      }
    }

    if (cardNumber != null && name != null && expiry != null) {
      val result = Arguments.createMap().apply {
        putString("cardNumber", cardNumber)
        putString("name", name)
        putString("expiry", expiry)
      }
      context.getJSModule(RCTEventEmitter::class.java)?.receiveEvent(
        id, "onCardScanned", result
      )
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
      context.getNativeModule(UIManagerModule::class.java)
        ?.updateNodeSize(id, finalWidth, finalHeight)
    }
  }

  fun setBorderColor(color: Int) {
    val drawable = findViewById<CardView>(R.id.cardContainer).foreground as? GradientDrawable
    val scanLine = findViewById<View>(R.id.scanLine)
    scanLine.setBackgroundColor(color)
    drawable?.setStroke(4.dpToPx().toInt(), color)
  }

  fun setCardHeight(cardHeight: Int) {
    setViewHeight(cardPreview as ViewGroup, cardHeight)
  }

  fun setViewHeight(view: ViewGroup, dp: Int) {
    val heightInPx = (dp * view.context.resources.displayMetrics.density).toInt()
    val params = view.layoutParams
    params.height = heightInPx
    view.layoutParams = params
  }

}
