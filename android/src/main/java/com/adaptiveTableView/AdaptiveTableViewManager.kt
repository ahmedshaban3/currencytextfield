package com.adaptiveTableView

import android.util.Log
import android.view.View
import com.adaptiveTableView1.AdaptiveTableView1
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.annotations.ReactProp


class AdaptiveTableViewManager : ViewGroupManager<AdaptiveTableView>() {

  override fun getName() = "AdaptiveTableView"

  override fun needsCustomLayoutForChildren() = true

  override fun createViewInstance(reactContext: ThemedReactContext): AdaptiveTableView {
    val view = AdaptiveTableView(reactContext)
    view.setReactContext(reactContext)
    return view
  }

  @ReactProp(name = "itemSize")
  fun setItemSize(view: AdaptiveTableView1, map: ReadableMap) {
    val width = map.getInt("width")
    val height = map.getInt("height")
    Log.d("itemWidth",width.toString())
    Log.d("itemWidth",height.toString())
    //view.setItemSize(width, height)
  }

  override fun addView(parent: AdaptiveTableView, child: View, index: Int) {
    Log.d("addView1","${child.height} , ${child.width}")
    parent.addPage(child, index)
  }

  override fun getChildCount(parent: AdaptiveTableView) = parent.pageCount
  override fun getChildAt(parent: AdaptiveTableView, index: Int): View = parent.getPage(index)
  override fun removeViewAt(parent: AdaptiveTableView, index: Int) {}
  override fun getExportedCustomDirectEventTypeConstants(): MutableMap<String, Any> {
    return mutableMapOf("onPageSelected" to mutableMapOf("registrationName" to "onPageSelected"))
  }
}

