package com.example.forhabr_swiperefresh
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.webkit.WebView
import androidx.core.view.NestedScrollingChild2
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat

class NestedWebView_2 : WebView, NestedScrollingChild2 {
    companion object {
        // used for touches
        @Suppress("unused")
        private const val X_COORD: Int = 0
        private const val Y_COORD: Int = 1
    }

    private var lastY = 0
    private var nestedOffsetY = 0
    private val childHelper = NestedScrollingChildHelper(this)

    private lateinit var webPageListener: WebViewListener

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    init {
        isNestedScrollingEnabled = false
    }

    fun setWebPageListener(listener: WebViewListener) {
        webPageListener = listener
    }

    private var actionUp = false
    var pointerCount = 0
    var overScroll = true
    override fun onOverScrolled(scrollX: Int, scrollY: Int, clampedX: Boolean, clampedY: Boolean) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY)
        this.overScroll = clampedY
        // Handling fast scrolling up.
        if (actionUp && clampedY) webPageListener.refreshSwipeEnable()
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        val event = MotionEvent.obtain(ev)
        pointerCount = event.pointerCount

        when (event.actionMasked) {

//             A non-primary pointer has gone down.
            MotionEvent.ACTION_POINTER_DOWN -> {
                webPageListener.refreshSwipeEnable()
            }

            MotionEvent.ACTION_DOWN -> {
                this.actionUp = false
                nestedOffsetY = 0
                lastY = event.y.toInt()
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
                webPageListener.refreshSwipeEnable()
            }

            MotionEvent.ACTION_MOVE -> {
                var deltaY = lastY - event.y.toInt()

                val scrollOffset = IntArray(2)
                val scrollConsumed = IntArray(2)
                if (dispatchNestedPreScroll(0, deltaY, scrollConsumed, scrollOffset)) {
                    deltaY -= scrollConsumed[Y_COORD]
                    event.offsetLocation(0f, scrollOffset[Y_COORD].toFloat())
                    nestedOffsetY += scrollOffset[Y_COORD]
                }

                if (dispatchNestedScroll(
                        0,
                        0.coerceAtLeast(scrollY + deltaY) - scrollY,
                        0,
                        deltaY - 0.coerceAtLeast(scrollY + deltaY) + scrollY,
                        scrollOffset
                    )
                ) {
                    lastY -= scrollOffset[Y_COORD]
                    event.offsetLocation(0f, scrollOffset[Y_COORD].toFloat())
                    nestedOffsetY += scrollOffset[Y_COORD]
                }
            }

            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                actionUp = true
                webPageListener.refreshSwipeEnable()
                stopNestedScroll()
            }

            else -> {
            }
        }

        event.recycle()
        return super.onTouchEvent(event)
    }

    override fun startNestedScroll(axes: Int, type: Int) = childHelper.startNestedScroll(axes, type)

    override fun stopNestedScroll(type: Int) = childHelper.stopNestedScroll(type)

    override fun hasNestedScrollingParent(type: Int) = childHelper.hasNestedScrollingParent(type)

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?,
        type: Int
    ) = childHelper.dispatchNestedPreScroll(
        dx,
        dy,
        consumed,
        offsetInWindow,
        type
    )

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?,
        type: Int
    ) = childHelper.dispatchNestedScroll(
        dxConsumed,
        dyConsumed,
        dxUnconsumed,
        dyUnconsumed,
        offsetInWindow,
        type
    )
}
