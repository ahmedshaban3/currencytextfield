package com.cardScanner

import android.view.View
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext

class CardScannerManager : SimpleViewManager<CardScannerView>() {
  override fun createViewInstance(context: ThemedReactContext): CardScannerView {
    val view = CardScannerView(context)
    return view
  }

  override fun getName(): String = "CardScannerView"


}
