package com.chowziy.atvs.quickmenu

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.GravityCompat
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.Gravity
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.annotation.DrawableRes
import com.chowziy.atvs.databinding.QuickMenuItemLayoutBinding
import com.chowziy.atvs.databinding.QuickPopupMenuLayoutBinding
import androidx.core.view.isNotEmpty
import com.chowziy.atvs.R

class QuickPopupMenu(
    private val context: Context,
    private val menuItems: List<MenuItem>,
    private val menuConfig: MenuConfig? = null
) {
    private val binding = QuickPopupMenuLayoutBinding.inflate(LayoutInflater.from(context))
    private val popupWindow: PopupWindow

    init {
        menuItems.forEach { item ->
            binding.container.addView(getMenuItemView(item))
        }

        popupWindow = PopupWindow(
            binding.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            isOutsideTouchable = true
            isFocusable = true
        }
    }

    private fun getMenuItemView(item: MenuItem): MenuItemView {
        val itemView = MenuItemView(context)
        item.viewHolder = itemView
        itemView.bindMenuItem(
            item = item,
            menuConfig = menuConfig,
            onCancelGroupSelectState = {
                val itemsToUnselect = menuItems.filter {
                    it.selectGroup == item.selectGroup && it != item &&
                            (item.selectMode != MenuItem.SelectMode.INCLUSIVE || it.selectMode == MenuItem.SelectMode.EXCLUSIVE)
                }
                itemsToUnselect.forEach { it.viewHolder?.setUnselected() }
            },
            onClickItem = {
                item.onClick()
                dismiss()
            }
        )
        itemView.showDivider(menuConfig?.showDivider == true && binding.container.isNotEmpty())
        return itemView
    }

    fun show(
        anchorView: View,
        verticalGravity: Int = Gravity.TOP,
        horizontalGravity: Int = Gravity.START,
        xOffset: Int = 0,
        yOffset: Int = 0
    ) {
        if (anchorView.windowToken == null) {
            anchorView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View) {
                    show(anchorView, verticalGravity, horizontalGravity, xOffset, yOffset)
                }

                override fun onViewDetachedFromWindow(v: View) {}
            })
            return
        }

        binding.root.measure(0, 0)

        val startOffset = when (horizontalGravity) {
            Gravity.CENTER_HORIZONTAL -> ((anchorView.measuredWidth / 2) - (binding.root.measuredWidth / 2)) + xOffset
            Gravity.END -> (-binding.root.measuredWidth) + xOffset
            else -> xOffset
        }

        val verticalOffset = if (verticalGravity == Gravity.TOP) {
            ((-anchorView.measuredHeight) - binding.root.measuredHeight) + yOffset
        } else {
            yOffset
        }

        popupWindow.showAsDropDown(
            anchorView,
            startOffset,
            verticalOffset,
            horizontalGravity or verticalGravity
        )
    }

    fun showAtLocation(
        parent: View,
        x : Int = 0,
        y : Int = 0,
        xOffset: Int = 0,
        yOffset: Int = 0
    ) {
        if (parent.windowToken == null) {
            parent.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View) {
                    showAtLocation(parent, x, y)
                }

                override fun onViewDetachedFromWindow(v: View) {}
            })
            return
        }

        binding.root.measure(0, 0)

        // 整个窗口在点击位置的左侧
        val adjustedX = x - binding.root.measuredWidth + xOffset
        val adjustedY = y - binding.root.measuredHeight + yOffset

        popupWindow.showAtLocation(
            parent,
            GravityCompat.START or Gravity.TOP,
            adjustedX,
            adjustedY
        )
    }

    fun dismiss() {
        binding.root.postDelayed({
            menuItems.forEach { it ->
                it.viewHolder = null
            }
            if (popupWindow.isShowing) {
                popupWindow.dismiss()
            }
        }, 300)
    }

    class MenuItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) : FrameLayout(context, attrs, defStyleAttr) {
        val binding: QuickMenuItemLayoutBinding =
            QuickMenuItemLayoutBinding.inflate(LayoutInflater.from(context), this, true).apply {
                root.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            }

        fun bindMenuItem(
            item: MenuItem,
            menuConfig: MenuConfig? = null,
            onCancelGroupSelectState: () -> Unit,
            onClickItem: () -> Unit
        ) {

            if (menuConfig?.iconPosition == MenuConfig.IconPosition.LEFT) {
                binding.containerLeft.visibility = VISIBLE
                binding.containerRight.visibility = GONE
                binding.iconLeft.visibility = VISIBLE
                binding.iconLeft.isSelected = item.isSelected
                binding.iconLeft.setImageResource(item.iconResId)
            } else {
                binding.iconLeft.visibility = GONE
                binding.containerRight.visibility = VISIBLE
                binding.iconRight.visibility = VISIBLE
                binding.iconRight.isSelected = item.isSelected
                binding.iconRight.setImageResource(item.iconResId)
            }
            menuConfig?.let {
                if (it.titleWidth > 0) {
                    binding.title.layoutParams.width = it.titleWidth
                }
            }

            binding.title.text = item.title

            setOnClickListener {
                onCancelGroupSelectState()

                if (item.selectLottieRes == null) {
                    binding.iconRight.isSelected = !binding.iconRight.isSelected
                    onClickItem()
                } else if (!binding.iconRight.isSelected) {
                    handleLikeLottieView(false, item, config = menuConfig, onClickItem)
                } else {
                    binding.iconRight.isSelected = false
                    handleLikeLottieView(true, item,  config = menuConfig, onClickItem)
                }
            }
        }

        private fun handleLikeLottieView(
            on: Boolean,
            item: MenuItem,
            config: MenuConfig? = null,
            onClickItem: () -> Unit
        ) {
            val (lottieView, iconView) = if (config?.iconPosition != MenuConfig.IconPosition.LEFT) {
                binding.lottieRight to binding.iconRight
            } else {
                binding.lottieLeft to binding.iconLeft
            }

            if (!on) {
                iconView.visibility = GONE
                lottieView.visibility = VISIBLE
                lottieView.setAnimation(item.selectLottieRes)
                lottieView.addAnimatorListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        post {
                            lottieView.visibility = GONE
                            iconView.visibility = VISIBLE
                            iconView.isSelected = true
                        }
                        onClickItem()
                    }
                })
                lottieView.playAnimation()
            } else {
                lottieView.visibility = GONE
                onClickItem()
            }
        }

        fun setUnselected() {
            binding.iconRight.isSelected = false
        }

        fun showDivider(show: Boolean) {
            binding.divider.visibility = if (show) VISIBLE else GONE
        }
    }

    data class MenuConfig(
        val iconPosition: IconPosition,
        val titleWidth: Int = 0,
        val showDivider: Boolean = true,
        @DrawableRes
        val background: Int = R.drawable.bg_quick_menu,
    ) {
        enum class IconPosition {
            LEFT,
            RIGHT
        }
    }

    data class MenuItem(
        val iconResId: Int,
        val title: String,
        val isSelected: Boolean = false,
        val selectLottieRes: String? = null,
        val delayDismiss: Long? = 0L,
        val selectGroup: String? = "default",
        val selectMode: SelectMode = SelectMode.EXCLUSIVE,
        val onClick: () -> Unit
    ) {
        var viewHolder: MenuItemView? = null

        enum class SelectMode {
            EXCLUSIVE,
            INCLUSIVE
        }
    }
}