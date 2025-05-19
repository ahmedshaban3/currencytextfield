package com.cardScanner

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

class CardScannerManager : SimpleViewManager<CardScannerView>() {
  override fun createViewInstance(context: ThemedReactContext): CardScannerView {
    val view = CardScannerView(context)
    return view
  }

  override fun getName(): String = "CardScannerView"

  @SuppressLint("UseKtx")
  @ReactProp(name = "borderColor")
  fun setBorderColor(view: CardScannerView, color: String) {
    try {
      val parsedColor = Color.parseColor(color)
      view.setBorderColor(parsedColor)
    } catch (e: IllegalArgumentException) {
      Log.e("MyCustomViewManager", "Invalid color: $color")
    }
  }

  @SuppressLint("UseKtx")
  @ReactProp(name = "cardHeight")
  fun setBorderColor(view: CardScannerView, height: Int) {
    view.setCardHeight(height)
  }


  override fun getExportedCustomDirectEventTypeConstants(): MutableMap<String, Any> {
    return mutableMapOf("onCardScanned" to mutableMapOf("registrationName" to "onCardScanned"))
  }

}
