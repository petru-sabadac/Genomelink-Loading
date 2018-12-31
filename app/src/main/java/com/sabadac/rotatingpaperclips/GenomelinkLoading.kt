package com.sabadac.rotatingpaperclips

import android.animation.*
import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

class GenomelinkLoading @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {

    private val angleProperty = "angle"
    private val animationDuration = 1900L
    private val canvasSide = dpToPx(400, context)
    private val middleCircleRadius = 40
    private val widthOfShapes = 14
    private val cornerRadius = 80
    private val maxWidth = 360
    private val maxHeight = 68
    private var angle = 60f
    private val bitmap = Bitmap.createBitmap(canvasSide.toInt(), canvasSide.toInt(), Bitmap.Config.ARGB_8888)
    private val bitmapCanvas = Canvas(bitmap)
    private val shapes = Array(7) { RectF() }
    private val colors = IntArray(7)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        paint.strokeWidth = dpToPx(widthOfShapes, context)
        initShapesAndColors()
        loadAnimations()
    }

    private fun loadAnimations() {
        val first = firstAnimation()
        val second = secondAnimation()

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(first, second)
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                animatorSet.start()
            }
        })

        animatorSet.start()
    }

    private fun initShapesAndColors() {
        colors[0] = ContextCompat.getColor(context, R.color.topSimpleRounderRect)
        shapes[0].left = (bitmap.width - dpToPx(maxWidth, context)) / 2f
        shapes[0].top = (bitmap.height - dpToPx(maxHeight, context)) / 2f
        shapes[0].right = (bitmap.width + dpToPx(maxWidth, context)) / 2f
        shapes[0].bottom = (bitmap.height + dpToPx(maxHeight, context)) / 2f

        colors[1] = ContextCompat.getColor(context, R.color.bottomSimpleRounderRect)
        shapes[1].left = (bitmap.width - dpToPx(maxWidth, context)) / 2f
        shapes[1].top = (bitmap.height - dpToPx(maxHeight, context)) / 2f
        shapes[1].right = (bitmap.width + dpToPx(middleCircleRadius, context)) / 2f
        shapes[1].bottom = (bitmap.height + dpToPx(maxHeight, context)) / 2f

        colors[2] = ContextCompat.getColor(context, R.color.topComplexRounderRect)
        shapes[2].left = (bitmap.width - dpToPx(maxWidth, context)) / 2f
        shapes[2].top = (bitmap.height - dpToPx(maxHeight, context)) / 2f
        shapes[2].right = (bitmap.width + dpToPx(maxWidth, context)) / 2f
        shapes[2].bottom = (bitmap.height + dpToPx(maxHeight, context)) / 2f

        colors[3] = ContextCompat.getColor(context, R.color.bottomComplexRounderRect)
        shapes[3].left = (bitmap.width - dpToPx(maxWidth, context)) / 2f
        shapes[3].top = (bitmap.height - dpToPx(maxHeight, context)) / 2f
        shapes[3].right = (bitmap.width + dpToPx(middleCircleRadius, context)) / 2f
        shapes[3].bottom = (bitmap.height + dpToPx(maxHeight, context)) / 2f

        colors[4] = ContextCompat.getColor(context, R.color.bottomComplexRounderRect)
        shapes[4].left = (bitmap.width - dpToPx(maxWidth, context)) / 2f
        shapes[4].top = (bitmap.height - dpToPx(maxHeight, context)) / 2f
        shapes[4].right = (bitmap.width - dpToPx(maxWidth, context)) / 2f + dpToPx(maxHeight, context)
        shapes[4].bottom = (bitmap.height + dpToPx(maxHeight, context)) / 2f

        colors[5] = ContextCompat.getColor(context, R.color.topComplexRounderRect)
        shapes[5].left = (bitmap.width + dpToPx(maxWidth, context)) / 2f - dpToPx(maxHeight, context)
        shapes[5].top = (bitmap.height - dpToPx(maxHeight, context)) / 2f
        shapes[5].right = (bitmap.width + dpToPx(maxWidth, context)) / 2f
        shapes[5].bottom = (bitmap.height + dpToPx(maxHeight, context)) / 2f

        colors[6] = ContextCompat.getColor(context, R.color.middleCircle)
        shapes[6].left = (bitmap.width - dpToPx(cornerRadius, context)) / 2f
        shapes[6].top = (bitmap.height - dpToPx(cornerRadius, context)) / 2f
        shapes[6].right = (bitmap.width + dpToPx(cornerRadius, context)) / 2f
        shapes[6].bottom = (bitmap.height + dpToPx(cornerRadius, context)) / 2f
    }

    private fun firstAnimation(): ValueAnimator {
        val anglePropertyHolder = PropertyValuesHolder.ofFloat(angleProperty, 60f, 240f)
        val valueAnimator = ValueAnimator()
        valueAnimator.setValues(anglePropertyHolder)
        valueAnimator.duration = animationDuration
        valueAnimator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator?) {
                angle = animation?.getAnimatedValue(angleProperty) as Float
                invalidate()
            }
        })
        return valueAnimator
    }

    private fun secondAnimation(): ValueAnimator {
        val anglePropertyHolder = PropertyValuesHolder.ofFloat(angleProperty, 240f, 420f)
        val valueAnimator = ValueAnimator()
        valueAnimator.setValues(anglePropertyHolder)
        valueAnimator.duration = animationDuration
        valueAnimator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator?) {
                angle = animation?.getAnimatedValue(angleProperty) as Float
                invalidate()
            }
        })
        return valueAnimator
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        bitmapCanvas.save()
        bitmapCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        shapes.forEachIndexed { index, shape ->
            bitmapCanvas.save()
            val finalAngle = if (index > 1) -1 * angle else angle
            if (index != 6) {
                paint.style = Paint.Style.STROKE
            } else {
                paint.style = Paint.Style.FILL
            }
            bitmapCanvas.rotate(finalAngle, bitmap.width / 2f, bitmap.height / 2f)
            paint.color = colors[index]
            bitmapCanvas.drawRoundRect(
                shape,
                dpToPx(cornerRadius / 2, context),
                dpToPx(cornerRadius / 2, context),
                paint
            )
            bitmapCanvas.restore()
        }

        canvas?.drawBitmap(bitmap, (width - bitmap.width) / 2f, (height - bitmap.height) / 2f, bitmapPaint)
    }

    private fun dpToPx(dp: Int, context: Context): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics)

}