package com.adaptiveTableView

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
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.events.RCTEventEmitter
import com.myreactnativepackage1.R


@SuppressLint("ResourceAsColor")
class AdaptiveTableView @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

  private val list: RecyclerView
  private var reactContext: ReactContext? = null
  val children = mutableListOf<View>()
  private val pages = mutableListOf<View>()
  private val adapter = ItemAdapter(pages)

  private var itemWidth = 0
  private var itemHeight = 0


  init {
    LayoutInflater.from(context).inflate(R.layout.adaptive_table_view, this, true)
    list = findViewById(R.id.list)
    list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    list.adapter = adapter
  }


  fun setReactContext(reactContext: ReactContext) {
    this.reactContext = reactContext
  }


  fun setItemSize(width: Int, height: Int) {
    itemWidth = width
    itemHeight = height
  }

  private fun onItemSelectedSelected(position: Int) {
    val event = Arguments.createMap().apply {
      putInt("position", position)
    }
    reactContext?.getJSModule(RCTEventEmitter::class.java)
      ?.receiveEvent(id, "onItemSelectedSelected", event)
  }


  inner class ItemAdapter(private val views: MutableList<View> = mutableListOf()) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    inner class ViewHolder(val container: View) : RecyclerView.ViewHolder(container)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val container = FrameLayout(parent.context)
      container.layoutParams = FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.MATCH_PARENT,
        FrameLayout.LayoutParams.WRAP_CONTENT,
      )
      return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val view = views[position]

      // Detach view if already attached
      (view.parent as? ViewGroup)?.removeView(view)

      val container = holder.container as LinearLayout
      container.removeAllViews()
      container.addView(view)

      view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
          view.viewTreeObserver.removeOnGlobalLayoutListener(this)

          val width = view.measuredWidth
          val height = view.measuredHeight

          Log.d("AdaptiveTableView", "ReactView measured: $width x $height")

          if (width > 0 && height > 0) {
            val layoutParams = RecyclerView.LayoutParams(width, height)
            holder.container.layoutParams = layoutParams

            view.layoutParams = FrameLayout.LayoutParams(width, height)

            view.measure(
              View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
              View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
            )
            view.layout(0, 0, width, height)
          }
        }
      })
    }

//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//      val view = views[position]
//      if (view.parent != null) {
//        (view.parent as ViewGroup).removeView(view)
//        Log.d("onBindViewHolder", position.toString())
//
//      }
//
//
//      view.viewTreeObserver.addOnGlobalLayoutListener {
//        val width = view.width
//        val height = view.height
//        Log.d("ChildViewSize", "Width: $width, Height: $height")
//      }
//
//      (holder.container as LinearLayout).let {
//
//        Log.d("view.width",view.width.toString())
//        Log.d("view.width",view.height.toString())
//
//
//        val layoutParams = LinearLayout.LayoutParams(
//         ViewGroup.LayoutParams.WRAP_CONTENT,
//          ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//
//        view.layoutParams = layoutParams
//        view.measure(
//          View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//          View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
//        )
//
//        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
//        holder.container.addView(view)
//        view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
//          override fun onGlobalLayout() {
//          view.viewTreeObserver.removeOnGlobalLayoutListener(this)
//
//           val width = view.measuredWidth
//           val height = view.measuredHeight
//
//           Log.d("ReactView", "Width: $width, Height: $height")
//         }
//        })
//
//      }
//
//      holder.container.removeAllViews()
//      holder.container.addView(view)
//    }

    override fun getItemCount(): Int = views.size

    @SuppressLint("NotifyDataSetChanged")
    fun add(view: View) {
      val newData = mutableListOf<View>()
      newData.addAll(views)
      newData.add(view)
      views.clear()
      views.addAll(newData)
      notifyDataSetChanged()
    }
  }

  class HorizontalSpacingItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
      outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
      outRect.right = space
      outRect.left = space
    }
  }

  private fun toPx(value: Int): Int {
    return TypedValue.applyDimension(
      TypedValue.COMPLEX_UNIT_DIP,
      value.toFloat(),
      context.resources.displayMetrics
    ).toInt()
  }


  fun addPage(view: View, index: Int) {
    pages.add(index, view)
    adapter.add(view)
  }

  fun getPage(index: Int): View = pages[index]

  val pageCount: Int
    get() = pages.size
}
