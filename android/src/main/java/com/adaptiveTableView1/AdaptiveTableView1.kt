package com.adaptiveTableView1

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.events.RCTEventEmitter
import com.myreactnativepackage1.R


class AdaptiveTableView1 @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

  private val list: RecyclerView
  private val adapter = ItemAdapter()
  private val pages = mutableListOf<View>()

  init {
    LayoutInflater.from(context).inflate(R.layout.adaptive_table_view, this, true)
    list = findViewById(R.id.list)
    list.layoutManager = LinearLayoutManager(context)
    list.adapter = adapter

    list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
        val totalItemCount = adapter.itemCount

        if (lastVisibleItem == totalItemCount - 1) {
          sendOnEndReachedEvent()
        }
      }
    })
  }

  private fun sendOnEndReachedEvent() {
    (context as? ReactContext)?.getJSModule(RCTEventEmitter::class.java)
      ?.receiveEvent(id, "onEndReached", Arguments.createMap())
  }

  fun addPage(view: View, index: Int) {
    if (view.parent != null) {
      (view.parent as ViewGroup).removeView(view)
    }
    pages.add(index, view)
    adapter.setItems(pages.toList())
  }

  fun getPage(index: Int): View = pages[index]
  val pageCount: Int get() = pages.size

  fun removePage(index: Int) {
    pages.removeAt(index)
    adapter.setItems(pages.toList())
  }

  fun removeAllPages() {
    pages.clear()
    adapter.setItems(emptyList())
  }

  inner class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    private val items = mutableListOf<View>()

    fun setItems(newItems: List<View>) {
      items.clear()
      items.addAll(newItems)
      notifyDataSetChanged()
    }

    inner class ViewHolder(val frame: FrameLayout) : RecyclerView.ViewHolder(frame)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val container = FrameLayout(parent.context)
      return ViewHolder(container)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val view = items[position]
      holder.frame.layoutParams = RecyclerView.LayoutParams(view.width, view.height)
      (view.parent as? ViewGroup)?.removeView(view)
      holder.frame.removeAllViews()
      holder.frame.addView(view)
    }

    override fun getItemCount(): Int = items.size
  }
}
