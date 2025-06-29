package com.chowziy.atvs.quickmenu

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout

class QuickLongPressMenuView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private var touchX = 0f
    private var touchY = 0f

    private var items = mutableListOf<QuickPopupMenu.MenuItem>()

    private var menuConfig = QuickPopupMenu.MenuConfig(
        iconPosition = QuickPopupMenu.MenuConfig.IconPosition.LEFT,
        titleWidth = 0, // 默认不限制标题宽度
    )

    init {
        setOnLongClickListener {
            showPopupMenu()
            true
        }
    }

    private fun showPopupMenu() {
        QuickPopupMenu(context, items, menuConfig)
            .showAtLocation(
                this@QuickLongPressMenuView,
                touchX.toInt(),
                touchY.toInt(),
                xOffset = -100,
                yOffset = 100

            )
    }

    fun setMenuItems(menuItems: List<QuickPopupMenu.MenuItem>) {
        items.clear()
        items.addAll(menuItems)
    }

    fun setMenuConfig(config: QuickPopupMenu.MenuConfig) {
        menuConfig = config
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            if (it.action == MotionEvent.ACTION_DOWN) {
                touchX = it.rawX
                touchY = it.rawY
            }
        }
        return super.onTouchEvent(event)
    }
}