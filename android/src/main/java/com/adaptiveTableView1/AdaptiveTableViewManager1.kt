package com.adaptiveTableView1

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.annotations.ReactProp


class AdaptiveTableViewManager1 : ViewGroupManager<AdaptiveTableView1>() {
  override fun getName(): String = "AdaptiveTableView1"

  override fun createViewInstance(reactContext: ThemedReactContext): AdaptiveTableView1 {
    return AdaptiveTableView1(reactContext)
  }

  override fun addView(parent: AdaptiveTableView1, child: View, index: Int) {
    parent.addPage(child, index)
  }

  override fun getChildCount(parent: AdaptiveTableView1): Int {
    return parent.pageCount
  }

  override fun getChildAt(parent: AdaptiveTableView1, index: Int): View {
    return parent.getPage(index)
  }

  override fun removeAllViews(parent: AdaptiveTableView1) {
    parent.removeAllPages()
  }

  override fun removeViewAt(parent: AdaptiveTableView1, index: Int) {
    parent.removePage(index)
  }

  override fun getExportedCustomBubblingEventTypeConstants(): MutableMap<String, Any> {
    return mutableMapOf(
      "onEndReached" to mapOf(
        "phasedRegistrationNames" to mapOf(
          "bubbled" to "onEndReached"
        )
      )
    )
  }
}




