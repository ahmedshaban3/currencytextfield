package com.currencyInput

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

class CurrencyInputViewManager : SimpleViewManager<View>() {
  override fun getName() = "CurrencyTextField"

  @RequiresApi(Build.VERSION_CODES.O)
  override fun createViewInstance(reactContext: ThemedReactContext): ViewGroup {
    return CurrencyInput(reactContext)
  }

  // Allow setting the text from React Native
  @RequiresApi(Build.VERSION_CODES.O)
  @ReactProp(name = "text")
  fun setText(view: CurrencyInput, text: String?) {
    view.setText(text ?: "")
  }

  @SuppressLint("UseKtx")
  @RequiresApi(Build.VERSION_CODES.O)
  @ReactProp(name = "fontColor")
  fun setFontColor(view: CurrencyInput, color: String) {
    try {
      val parsedColor = Color.parseColor(color) // Convert hex string to Color
      view.setFontColor(parsedColor, color)
    } catch (e: IllegalArgumentException) {
      Log.e("MyCustomViewManager", "Invalid color: $color")
    }
  }

  @SuppressLint("UseKtx")
  @RequiresApi(Build.VERSION_CODES.O)
  @ReactProp(name = "placeholderColor")
  fun setPlaceHolderColor(view: CurrencyInput, color: String) {
    try {
      val parsedColor = Color.parseColor(color) // Convert hex string to Color
      view.setPlaceHolderColor(parsedColor, color)
    } catch (e: IllegalArgumentException) {
      Log.e("MyCustomViewManager", "Invalid color: $color")
    }
  }

  @SuppressLint("UseKtx")
  @RequiresApi(Build.VERSION_CODES.O)
  @ReactProp(name = "currency")
  fun setCurrency(view: CurrencyInput, currency: String) {
    view.setCurrency(currency)
  }

  @SuppressLint("UseKtx")
  @RequiresApi(Build.VERSION_CODES.O)
  @ReactProp(name = "fontSize")
  fun fontSize(view: CurrencyInput, size: Int) {
    view.setFontSize(size)
  }

  override fun getExportedCustomDirectEventTypeConstants(): MutableMap<String, Any> {
    return mutableMapOf("onChangeText" to mutableMapOf("registrationName" to "onChangeText"))
  }
}
