package com.adaptiveTableView

import android.view.View
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager


class AdaptiveTableViewManager : ViewGroupManager<AdaptiveTableView>() {
  override fun getName(): String = "AdaptiveTableView"

  override fun createViewInstance(reactContext: ThemedReactContext): AdaptiveTableView {
    return AdaptiveTableView(reactContext)
  }

  override fun addView(parent: AdaptiveTableView, child: View, index: Int) {
    parent.addPage(child, index)
  }

  override fun getChildCount(parent: AdaptiveTableView): Int {
    return parent.pageCount
  }

  override fun getChildAt(parent: AdaptiveTableView, index: Int): View {
    return parent.getPage(index)
  }

  override fun removeAllViews(parent: AdaptiveTableView) {
    parent.removeAllPages()
  }

  override fun removeViewAt(parent: AdaptiveTableView, index: Int) {
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




