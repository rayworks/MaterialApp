package com.rayworks.materialapp.widget

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.FloatRange

//
// The Circular progress bar
// Code taken and modified based on
// https://stackoverflow.com/questions/36639660/android-circular-progress-bar-with-rounded-corners/53830379#53830379
//
class CircularProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val progressPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.parseColor("#ee2e71")
    }
    private val backgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.parseColor("#f5f6fa")
    }

    private val rect = RectF()
    private val startAngle = 90f
    private val maxAngle = 360f
    private val maxProgress = 100

    private var diameter = 0f
    private var angle = 0f

    private var progress: Float = .0f
    private var objectAnim: ObjectAnimator? = null

    override fun onDraw(canvas: Canvas) {
        drawCircle(maxAngle, canvas, backgroundPaint)
        drawCircle(angle, canvas, progressPaint)
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        diameter = Math.min(width, height).toFloat()
        updateRect()
    }

    private fun updateRect() {
        val strokeWidth = backgroundPaint.strokeWidth
        rect.set(strokeWidth, strokeWidth, diameter - strokeWidth, diameter - strokeWidth)
    }

    private fun drawCircle(angle: Float, canvas: Canvas, paint: Paint) {
        canvas.drawArc(rect, startAngle, angle, false, paint)
    }

    private fun calculateAngle(progress: Float) = maxAngle / maxProgress * progress

    fun setProgress(@FloatRange(from = 0.0, to = 100.0) value: Float) {
        this.progress = value;
        angle = calculateAngle(progress)
        invalidate()
    }

    fun animateProgress(
        @FloatRange(from = 0.0, to = 100.0) progress: Float,
        animationCompleted: () -> Unit = {}
    ) {
        if (objectAnim == null) {
            objectAnim = ObjectAnimator.ofFloat(this, "progress", 0f, progress).apply {
                duration = 1000
                interpolator = AccelerateDecelerateInterpolator()
            }
        }

        objectAnim!!.addUpdateListener { animation ->
            val animatedValue = animation?.getAnimatedValue("progress")
            animatedValue?.let {
                if (it == progress) {
                    println(">>> animation end with value : + $progress")
                    animationCompleted.invoke()
                }
            }
        }

        objectAnim!!.start()
    }

    fun setProgressColor(color: Int) {
        progressPaint.color = color
        invalidate()
    }

    fun setProgressBackgroundColor(color: Int) {
        backgroundPaint.color = color
        invalidate()
    }

    fun setProgressWidth(width: Float) {
        progressPaint.strokeWidth = width
        backgroundPaint.strokeWidth = width
        updateRect()
        invalidate()
    }

    fun setRounded(rounded: Boolean) {
        progressPaint.strokeCap = if (rounded) Paint.Cap.ROUND else Paint.Cap.BUTT
        invalidate()
    }
}