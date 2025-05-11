package com.adaptivePagerView

import android.view.View
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.annotations.ReactProp


class AdaptivePagerViewManager : ViewGroupManager<AdaptivePagerView>() {

  override fun getName() = "AdaptivePagerView"

  override fun needsCustomLayoutForChildren() = true

  override fun createViewInstance(reactContext: ThemedReactContext): AdaptivePagerView {
    val view = AdaptivePagerView(reactContext)
    view.setReactContext(reactContext)
    return view
  }

  @ReactProp(name = "itemSize")
  fun setItemSize(view: AdaptivePagerView, map: ReadableMap) {
    val width = map.getInt("width")
    val height = map.getInt("height")
    view.setItemSize(width, height)
  }

  @ReactProp(name = "itemSpacing", defaultInt = 8)
  fun setItemSpacing(view: AdaptivePagerView, spacing: Int) {
  }

  override fun addView(parent: AdaptivePagerView, child: View, index: Int) {
    parent.addPage(child, index)
  }

  override fun getChildCount(parent: AdaptivePagerView) = parent.pageCount
  override fun getChildAt(parent: AdaptivePagerView, index: Int): View = parent.getPage(index)
  override fun removeViewAt(parent: AdaptivePagerView, index: Int) {}
  override fun getExportedCustomDirectEventTypeConstants(): MutableMap<String, Any> {
    return mutableMapOf("onPageSelected" to mutableMapOf("registrationName" to "onPageSelected"))
  }
}

