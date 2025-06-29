package com.chowziy.atvs

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.graphics.withRotation

/**
 * 自定义View，用于在FrameLayout的四个边缘添加渐变效果
 */
class FadeEdgeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    companion object {
        // 边缘类型常量
        const val EDGE_TYPE_NONE = 0 // 无渐变
        const val EDGE_TYPE_TOP = 1 // 顶部边缘
        const val EDGE_TYPE_BOTTOM = 2 // 底部边缘
        const val EDGE_TYPE_LEFT = 4 // 左侧边缘
        const val EDGE_TYPE_RIGHT = 8 // 右侧边缘
        const val EDGE_TYPE_ALL = 16 // 无渐变
    }

    // 边缘类型掩码常量
    private val topMask: Int = EDGE_TYPE_TOP
    private val bottomMask: Int = EDGE_TYPE_BOTTOM
    private val leftMask: Int = EDGE_TYPE_LEFT
    private val rightMask: Int = EDGE_TYPE_RIGHT

    // 当前边缘类型和宽度
    private var edgeFadePostions: Int
    private var edgeWidth: Float  = 24f // 默认边缘宽度为24dp

    // 渐变色和位置
    private val mGradientColors: IntArray = intArrayOf(-1, 0) // 白色到透明
    private val mGradientPosition: FloatArray = floatArrayOf(0.0f, 1.0f)

    // 渐变画笔
    private val shaderPaint: Paint

    init {
        // 从属性中获取配置
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.FadeEdgeView)
        edgeFadePostions = typedArray.getInt(R.styleable.FadeEdgeView_edge_position, 0)
        edgeWidth = typedArray.getDimension(R.styleable.FadeEdgeView_edge_size, 0.0f)
        typedArray.recycle()

        // 初始化渐变画笔
        shaderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
            shader = LinearGradient(
                0.0f, 0.0f, 0.0f, edgeWidth,
                mGradientColors, mGradientPosition, Shader.TileMode.CLAMP
            )
        }
    }

    override fun drawChild(canvas: Canvas, child: View, drawingTime: Long): Boolean {
        // 保存图层状态
        val saveLayer = canvas.saveLayer(0.0f, 0.0f, width.toFloat(), height.toFloat(), null)

        // 绘制子View
        val result = super.drawChild(canvas, child, drawingTime)

        // 绘制顶部渐变
        if (edgeFadePostions == EDGE_TYPE_ALL || (edgeFadePostions and topMask) != 0) {
            canvas.drawRect(0.0f, 0.0f, canvas.width.toFloat(), edgeWidth, shaderPaint)
        }

        // 绘制底部渐变
        if (edgeFadePostions == EDGE_TYPE_ALL || (edgeFadePostions and bottomMask) != 0) {
            canvas.withRotation(
                180.0f,
                canvas.width.toFloat() / 2.0f,
                canvas.height.toFloat() / 2.0f
            ) {
                canvas.drawRect(0.0f, 0.0f, canvas.width.toFloat(), edgeWidth, shaderPaint)
            }
        }

        // 计算左右边缘的偏移量
        val offset = (canvas.height - canvas.width).toFloat() / 2.0f

        // 绘制左侧渐变
        if (edgeFadePostions == EDGE_TYPE_ALL || (edgeFadePostions and leftMask) != 0) {
            canvas.withRotation(
                90.0f,
                canvas.width.toFloat() / 2.0f,
                canvas.height.toFloat() / 2.0f
            ) {
                canvas.translate(0.0f, offset)
                canvas.drawRect(
                    0.0f - offset,
                    0.0f,
                    canvas.width.toFloat() + offset,
                    edgeWidth,
                    shaderPaint
                )
            }
        }

        // 绘制右侧渐变
        if (edgeFadePostions == EDGE_TYPE_ALL || (edgeFadePostions and rightMask) != 0) {
            canvas.withRotation(
                270.0f,
                canvas.width.toFloat() / 2.0f,
                canvas.height.toFloat() / 2.0f
            ) {
                canvas.translate(0.0f, offset)
                canvas.drawRect(
                    0.0f - offset,
                    0.0f,
                    canvas.width.toFloat() + offset,
                    edgeWidth,
                    shaderPaint
                )
            }
        }

        // 恢复画布状态
        canvas.restoreToCount(saveLayer)
        return result
    }
}