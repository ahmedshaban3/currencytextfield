package com.adaptivePagerView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.events.RCTEventEmitter
import com.myreactnativepackage1.R
import kotlin.math.abs


class AdaptivePagerView @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

  private val pager: RecyclerView
  private var reactContext: ReactContext? = null
  val children = mutableListOf<View>()

  var itemWidth = 0
  var itemHeight = 0


  init {
    LayoutInflater.from(context).inflate(R.layout.adaptive_pager_view, this, true)
    pager = findViewById(R.id.view_pager)
    initViewPager()
  }

  private fun initViewPager() {
    val snapHelper = PagerSnapHelper()
    snapHelper.attachToRecyclerView(pager)
    pager.addItemDecoration(HorizontalSpacingItemDecoration(16))
    pager.clipToPadding = false

    pager.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val center = recyclerView.width / 2
        for (i in 0 until recyclerView.childCount) {
          val child = recyclerView.getChildAt(i)
          val childCenter = (child.left + child.right) / 2
          val distanceFromCenter = abs(center - childCenter)
          val scale = 1 - (0.05f * distanceFromCenter / center.toFloat())
          child.scaleY = scale
          child.scaleX = scale
        }
      }

      override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
          val layoutManager = recyclerView.layoutManager as? LinearLayoutManager
          val snapView = snapHelper.findSnapView(layoutManager)
          val position = layoutManager?.getPosition(snapView ?: return)
          emitPageSelected(position ?: 0)
        }
      }

    })
  }

  fun setReactContext(reactContext: ReactContext) {
    this.reactContext = reactContext
  }

  @SuppressLint("NotifyDataSetChanged")
  fun setViews() {
    pager.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    pager.adapter = PagerAdapter(pages)
    pager.adapter?.notifyDataSetChanged()
  }


  fun setItemSize(width: Int, height: Int) {
    itemWidth = width
    itemHeight = height
  }

  private fun emitPageSelected(position: Int) {
    val event = Arguments.createMap().apply {
      putInt("position", position)
    }
    reactContext?.getJSModule(RCTEventEmitter::class.java)
      ?.receiveEvent(id, "onPageSelected", event)
  }


  inner class PagerAdapter(private val views: List<View>) :
    RecyclerView.Adapter<PagerAdapter.ViewHolder>() {

    inner class ViewHolder(val container: FrameLayout) : RecyclerView.ViewHolder(container)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val container = FrameLayout(parent.context)
      container.layoutParams = RecyclerView.LayoutParams(toPx(itemWidth), toPx(itemWidth))
      return ViewHolder(container)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val view = views[position]

      if (view.parent != null) {
        (view.parent as ViewGroup).removeView(view)
      }

      holder.container.removeAllViews()
      holder.container.addView(view)
    }

    override fun getItemCount(): Int = views.size
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

  private val pages = mutableListOf<View>()

  fun addPage(view: View, index: Int) {
    pages.add(index, view)
    setViews()
  }

  fun getPage(index: Int): View = pages[index]

  val pageCount: Int
    get() = pages.size
}
