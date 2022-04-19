package io.github.grishaninvyacheslav.moving_car_animation

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import androidx.core.content.ContextCompat

fun Path.moveTo(pxPoint: PointF) = this.moveTo(pxPoint.x, pxPoint.y)

fun Path.cubicTo(firstControlPoint: PointF, secondControlPoint: PointF, endPoint: PointF) =
    this.cubicTo(
        firstControlPoint.x,
        firstControlPoint.y,
        secondControlPoint.x,
        secondControlPoint.y,
        endPoint.x,
        endPoint.y
    )

fun getBitmap(vectorDrawable: VectorDrawable): Bitmap {
    val bitmap = Bitmap.createBitmap(
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
    vectorDrawable.draw(canvas)
    return bitmap
}

fun getBitmap(context: Context, drawableId: Int): Bitmap {
    val drawable = ContextCompat.getDrawable(context, drawableId)
    return if (drawable is BitmapDrawable) {
        BitmapFactory.decodeResource(context.resources, drawableId)
    } else if (drawable is VectorDrawable) {
        getBitmap(drawable)
    } else {
        throw IllegalArgumentException("unsupported drawable type")
    }
}
