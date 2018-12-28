package com.sabadac.rotatingpaperclips

import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

class RotatingPaperClips @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {

    private val canvasSide = dpToPx(400, context)
    private val middleCircleRadius = 40
    private val widthOfShapes = 12
    private val cornerRadius = 80
    private val maxWidth = 360
    private val maxHeight = 68

    private val buttonRectF = RectF()
    private val bitmap = Bitmap.createBitmap(canvasSide.toInt(), canvasSide.toInt(), Bitmap.Config.ARGB_8888)
    private val bitmapCanvas = Canvas(bitmap)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        bitmapCanvas.save()
        bitmapCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        paint.color = ContextCompat.getColor(context, R.color.topSimpleRounderRect)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = dpToPx(widthOfShapes, context)
        buttonRectF.left = (bitmap.width - dpToPx(maxWidth, context)) / 2f
        buttonRectF.top = (bitmap.height - dpToPx(maxHeight, context)) / 2f
        buttonRectF.right = (bitmap.width + dpToPx(maxWidth, context)) / 2f
        buttonRectF.bottom = (bitmap.height + dpToPx(maxHeight, context)) / 2f
        bitmapCanvas.drawRoundRect(
            buttonRectF,
            dpToPx(cornerRadius / 2, context),
            dpToPx(cornerRadius / 2, context),
            paint
        )

        paint.color = ContextCompat.getColor(context, R.color.bottomSimpleRounderRect)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = dpToPx(widthOfShapes, context)
        buttonRectF.left = (bitmap.width - dpToPx(maxWidth, context)) / 2f
        buttonRectF.top = (bitmap.height - dpToPx(maxHeight, context)) / 2f
        buttonRectF.right = (bitmap.width + dpToPx(maxHeight, context)) / 2f
        buttonRectF.bottom = (bitmap.height + dpToPx(maxHeight, context)) / 2f
        bitmapCanvas.drawRoundRect(
            buttonRectF,
            dpToPx(cornerRadius / 2, context),
            dpToPx(cornerRadius / 2, context),
            paint
        )

        bitmapCanvas.restore()

        bitmapCanvas.save()
        paint.color = ContextCompat.getColor(context, R.color.topComplexRounderRect)
        buttonRectF.left = (bitmap.width - dpToPx(maxWidth, context)) / 2f
        buttonRectF.top = (bitmap.height - dpToPx(maxHeight, context)) / 2f
        buttonRectF.right = (bitmap.width + dpToPx(maxWidth, context)) / 2f
        buttonRectF.bottom = (bitmap.height + dpToPx(maxHeight, context)) / 2f

        bitmapCanvas.rotate(90f, bitmap.width / 2f, bitmap.height / 2f)
        bitmapCanvas.drawRoundRect(
            buttonRectF,
            dpToPx(cornerRadius / 2, context),
            dpToPx(cornerRadius / 2, context),
            paint
        )
        bitmapCanvas.restore()

        bitmapCanvas.save()
        paint.color = ContextCompat.getColor(context, R.color.bottomComplexRounderRect)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = dpToPx(widthOfShapes, context)
        buttonRectF.left = (bitmap.width - dpToPx(maxWidth, context)) / 2f
        buttonRectF.top = (bitmap.height - dpToPx(maxHeight, context)) / 2f
        buttonRectF.right = (bitmap.width + dpToPx(maxHeight, context)) / 2f
        buttonRectF.bottom = (bitmap.height + dpToPx(maxHeight, context)) / 2f
        bitmapCanvas.rotate(90f, bitmap.width / 2f, bitmap.height / 2f)
        bitmapCanvas.drawRoundRect(
            buttonRectF,
            dpToPx(cornerRadius / 2, context),
            dpToPx(cornerRadius / 2, context),
            paint
        )
        bitmapCanvas.drawCircle(
            bitmap.width / 2f - dpToPx(maxWidth / 2 - maxHeight / 2, context),
            bitmap.height / 2f,
            dpToPx(maxHeight / 2, context),
            paint
        )

        paint.color = ContextCompat.getColor(context, R.color.topComplexRounderRect)
        bitmapCanvas.drawCircle(
            bitmap.width / 2f + dpToPx(maxWidth / 2 - maxHeight / 2, context),
            bitmap.height / 2f,
            dpToPx(maxHeight / 2, context),
            paint
        )
        bitmapCanvas.restore()

        bitmapCanvas.save()
        paint.color = ContextCompat.getColor(context, R.color.middleCircle)
        paint.style = Paint.Style.FILL
        bitmapCanvas.drawCircle(bitmap.width / 2f, bitmap.height / 2f, dpToPx(middleCircleRadius, context), paint)
        bitmapCanvas.restore()

        paint.color = Color.TRANSPARENT
        paint.alpha = 255
        canvas?.drawBitmap(bitmap, (width - bitmap.width) / 2f, (height - bitmap.height) / 2f, paint)
    }

    private fun dpToPx(dp: Int, context: Context): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics)

}