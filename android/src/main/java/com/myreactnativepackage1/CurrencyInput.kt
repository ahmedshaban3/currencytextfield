@file:Suppress("DEPRECATION")

package com.myreactnativepackage1

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.core.widget.ImageViewCompat
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.events.RCTEventEmitter

@Suppress("SameParameterValue")
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("AppCompatCustomView")
class CurrencyInput @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

  private val currencySymbol: ImageView
  private val editText: EditText
  private var hintColor: String = "#9A9A9A"
  private var textColor: String = "#000000"


  init {
    LayoutInflater.from(context).inflate(R.layout.currency_input, this, true)
    currencySymbol = findViewById(R.id.ksa_symbol)
    editText = findViewById(R.id.ksa_input)

    editText.addTextChangedListener(object : android.text.TextWatcher {
      var currentText = ""
      override fun afterTextChanged(s: android.text.Editable?) {
        val reactContext = context as? ReactContext
        reactContext?.getJSModule(RCTEventEmitter::class.java)?.receiveEvent(
          id, "onChangeText", createEventMap(s.toString())
        )
        val color =
          if (s.toString().isEmpty()) hintColor else textColor
        setTintColor(color)

        if (s.isNullOrEmpty()) return
        val textPaint = editText.paint
        val textWidth = textPaint.measureText(s.toString())
        val minWidth = dpToPx(50) // Minimum width (adjustable)
        val newWidth = maxOf(
          textWidth.toInt() + editText.paddingLeft + editText.paddingRight, minWidth
        )
        editText.updateLayoutParams<ViewGroup.LayoutParams> {
          width = newWidth + 20
        }
        editText.setSelection(s.toString().length)
        editText.requestFocus()

        if (editText.text.isNullOrEmpty()) return

        val rawText = editText.text.toString().replace(",", "")

        if (rawText == currentText) return // Prevent infinite loop

        try {
          val formatted = formatWithCustomGrouping(rawText)
          currentText = formatted
          editText.removeTextChangedListener(this)
          setText(formatted)
          editText.setSelection(formatted.length) // Move cursor to end
          editText.addTextChangedListener(this)
        } catch (e: Exception) {
          e.printStackTrace()
        }
      }

      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

      }
    })
  }

  @SuppressLint("DefaultLocale")
  private fun formatWithCustomGrouping(input: String): String {
    return if (input.contains(".")) {
      val parts = input.split(".")
      val integerPart = parts[0].toLongOrNull()?.let { String.format("%,d", it) } ?: parts[0]
      "${integerPart}.${parts[1].take(2)}"
    } else {
      input.toLongOrNull()?.let { String.format("%,d", it) } ?: input
    }
  }

  private fun dpToPx(dp: Int): Int {
    val density = resources.displayMetrics.density
    return (dp * density).toInt()
  }

  fun setText(text: String) {
    editText.setText(text)
  }

  fun setFontColor(color: Int, color1: String) {
    textColor = color1
    editText.setTextColor(color)
  }

  private fun createEventMap(text: String): WritableMap {
    val event: WritableMap = Arguments.createMap()
    event.putString("text", text)
    return event
  }

  @SuppressLint("UseKtx")
  fun setTintColor(color: String) {
    try {
      ImageViewCompat.setImageTintList(
        currencySymbol, android.content.res.ColorStateList.valueOf(Color.parseColor(color))
      )
    } catch (e: IllegalArgumentException) {
      // Invalid color format, fallback to default
      ImageViewCompat.setImageTintList(
        currencySymbol, ContextCompat.getColorStateList(context, android.R.color.darker_gray)
      )
    }
  }

  fun setPlaceHolderColor(color: Int, color1: String) {
    hintColor = color1
    editText.setHintTextColor(color)
  }
}
