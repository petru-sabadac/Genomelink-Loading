package com.sabadac.rotatingpaperclips

import android.animation.*
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.support.v4.view.animation.PathInterpolatorCompat
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
    private val canvasSide = dpToPx(200)
    private val middleCircleRadius = 20
    private val widthOfShapes = 7
    private val cornerRadius = 40
    private val maxWidth = 180
    private val maxHeight = 34
    private var angle = 30f
    private val bitmap = Bitmap.createBitmap(canvasSide.toInt(), canvasSide.toInt(), Bitmap.Config.ARGB_8888)
    private val bitmapCanvas = Canvas(bitmap)
    private val shapes = Array(7) { RectF() }
    private val colors = IntArray(7)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val animatorSet = AnimatorSet()

    init {
        paint.strokeWidth = dpToPx(widthOfShapes)
        initShapesAndColors()
        loadAnimations()
    }

    override fun clearAnimation() {
        animatorSet.removeAllListeners()
        animatorSet.cancel()
        super.clearAnimation()
    }

    private fun loadAnimations() {
        val first = firstAnimation()
        val second = secondAnimation()

        animatorSet.interpolator = PathInterpolatorCompat.create(0.42f, 0.0f, 0.58f, 1.0f)
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
        shapes[0].left = (bitmap.width - dpToPx(maxWidth)) / 2f
        shapes[0].top = (bitmap.height - dpToPx(maxHeight)) / 2f
        shapes[0].right = (bitmap.width + dpToPx(maxWidth)) / 2f
        shapes[0].bottom = (bitmap.height + dpToPx(maxHeight)) / 2f

        colors[1] = ContextCompat.getColor(context, R.color.bottomSimpleRounderRect)
        shapes[1].left = (bitmap.width - dpToPx(maxWidth)) / 2f
        shapes[1].top = (bitmap.height - dpToPx(maxHeight)) / 2f
        shapes[1].right = (bitmap.width + dpToPx(middleCircleRadius)) / 2f
        shapes[1].bottom = (bitmap.height + dpToPx(maxHeight)) / 2f

        colors[2] = ContextCompat.getColor(context, R.color.topComplexRounderRect)
        shapes[2].left = (bitmap.width - dpToPx(maxWidth)) / 2f
        shapes[2].top = (bitmap.height - dpToPx(maxHeight)) / 2f
        shapes[2].right = (bitmap.width + dpToPx(maxWidth)) / 2f
        shapes[2].bottom = (bitmap.height + dpToPx(maxHeight)) / 2f

        colors[3] = ContextCompat.getColor(context, R.color.bottomComplexRounderRect)
        shapes[3].left = (bitmap.width - dpToPx(maxWidth)) / 2f
        shapes[3].top = (bitmap.height - dpToPx(maxHeight)) / 2f
        shapes[3].right = (bitmap.width + dpToPx(middleCircleRadius)) / 2f
        shapes[3].bottom = (bitmap.height + dpToPx(maxHeight)) / 2f

        colors[4] = ContextCompat.getColor(context, R.color.bottomComplexRounderRect)
        shapes[4].left = (bitmap.width - dpToPx(maxWidth)) / 2f
        shapes[4].top = (bitmap.height - dpToPx(maxHeight)) / 2f
        shapes[4].right = (bitmap.width - dpToPx(maxWidth)) / 2f + dpToPx(maxHeight)
        shapes[4].bottom = (bitmap.height + dpToPx(maxHeight)) / 2f

        colors[5] = ContextCompat.getColor(context, R.color.topComplexRounderRect)
        shapes[5].left = (bitmap.width + dpToPx(maxWidth)) / 2f - dpToPx(maxHeight)
        shapes[5].top = (bitmap.height - dpToPx(maxHeight)) / 2f
        shapes[5].right = (bitmap.width + dpToPx(maxWidth)) / 2f
        shapes[5].bottom = (bitmap.height + dpToPx(maxHeight)) / 2f

        colors[6] = ContextCompat.getColor(context, R.color.middleCircle)
        shapes[6].left = (bitmap.width - dpToPx(cornerRadius)) / 2f
        shapes[6].top = (bitmap.height - dpToPx(cornerRadius)) / 2f
        shapes[6].right = (bitmap.width + dpToPx(cornerRadius)) / 2f
        shapes[6].bottom = (bitmap.height + dpToPx(cornerRadius)) / 2f
    }

    private fun firstAnimation(): ValueAnimator {
        val anglePropertyHolder = PropertyValuesHolder.ofFloat(angleProperty, 60f, 240f)
        val valueAnimator = ValueAnimator()
        valueAnimator.setValues(anglePropertyHolder)
        valueAnimator.duration = animationDuration
        valueAnimator.addUpdateListener { animation ->
            angle = animation.getAnimatedValue(angleProperty) as Float
            invalidate()
        }
        return valueAnimator
    }

    private fun secondAnimation(): ValueAnimator {
        val anglePropertyHolder = PropertyValuesHolder.ofFloat(angleProperty, 240f, 420f)
        val valueAnimator = ValueAnimator()
        valueAnimator.setValues(anglePropertyHolder)
        valueAnimator.duration = animationDuration
        valueAnimator.addUpdateListener { animation ->
            angle = animation.getAnimatedValue(angleProperty) as Float
            invalidate()
        }
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
                dpToPx(cornerRadius / 2),
                dpToPx(cornerRadius / 2),
                paint
            )
            bitmapCanvas.restore()
        }

        canvas?.drawBitmap(bitmap, (width - bitmap.width) / 2f, (height - bitmap.height) / 2f, bitmapPaint)
    }

    private fun dpToPx(dp: Int): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), Resources.getSystem().displayMetrics)

}