package io.github.grishaninvyacheslav.moving_car_animation

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.scale
import kotlin.properties.Delegates

class MovingCarView : View {
    private val animationDurationMs: Long = 10000

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(carPath, paint)
        carPosition?.let {
            canvas.drawBitmap(carBitmap, it.x, it.y, null)
        } ?: run {
            canvas.drawBitmap(carBitmap, defaultCarPosition.x, defaultCarPosition.y, null)
        }
    }

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)
        val minViewMeasure = if (viewWidth < viewHeight) viewWidth else viewHeight
        basisCenter.apply {
            x = viewWidth / 2
            y = viewHeight / 2
        }
        iBasisMultiplier = minViewMeasure / 2
        jBasisMultiplier = iBasisMultiplier
        paint.strokeWidth = 0.1f * iBasisMultiplier
        calculatePath()
    }

    private fun calculatePath() {
        carPath.reset()
        carPath.moveTo(bezierPathPoints.first().pxPoint())
        for (index in 1..bezierPathPoints.size - 3 step 3) {
            carPath.cubicTo(
                bezierPathPoints[index].pxPoint(),
                bezierPathPoints[index + 1].pxPoint(),
                bezierPathPoints[index + 2].pxPoint()
            )
        }
        invalidate()
    }

    fun startAnimation() {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = animationDurationMs
            addUpdateListener {
                carPosition = getCarPathPoint(animatedValue as Float)
                invalidate()
            }
        }.apply {
            start()
        }
    }

    private fun getCarPathPoint(distance: Float): PointF = with(PathMeasure(carPath, false)) {
        val pointCoordinates = floatArrayOf(0f, 0f)
        getPosTan(length * distance, pointCoordinates, null)
        pointCoordinates[0] = pointCoordinates[0] - carBitmap.width / 2
        pointCoordinates[1] = pointCoordinates[1] - carBitmap.height / 2
        return PointF(pointCoordinates[0], pointCoordinates[1])
    }

    private fun pxPoint(basedX: Float, basedY: Float) =
        PointF((basedX * iBasisMultiplier + basisCenter.x), (basedY * jBasisMultiplier + basisCenter.y))

    private fun PointF.pxPoint() = pxPoint(this.x, this.y)

    private val defaultCarPosition by lazy {
        PointF(
            bezierPathPoints.first().pxPoint().x - carBitmap.width / 2,
            bezierPathPoints.first().pxPoint().y - carBitmap.height / 2
        )
    }

    private var carPosition: PointF? = null

    private val carBitmap by lazy {
        getBitmap(context, R.drawable.outline_directions_car_24)
            .scale((0.1f * iBasisMultiplier).toInt(), (0.1f * iBasisMultiplier).toInt())
    }

    private var animator: ValueAnimator? = null

    private var iBasisMultiplier by Delegates.notNull<Int>()
    private var jBasisMultiplier by Delegates.notNull<Int>()
    private var basisCenter = Point(0, 0)

    private val paint: Paint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.DKGRAY
    }

    private var carPath: Path = Path()

    private val bezierPathPoints by lazy {
        listOf(
            PointF(0f, 0.95f),

            PointF(0f, 0.810566f),
            PointF(0f, 0.599274f),
            PointF(0f, 0.445983f),

            PointF(0f, 0.335538f),
            PointF(-0.084533f, 0.240457f),
            PointF(-0.27596f, 0.254373f),

            PointF(-0.429003f, 0.265499f),
            PointF(-0.515722f, 0.077766f),
            PointF(-0.390427f, -0.058886f),

            PointF(-0.300867f, -0.156565f),
            PointF(-0.185891f, -0.089975f),
            PointF(-0.129301f, -0.058653f),

            PointF(0.186945f, 0.116383f),
            PointF(0.274568f, -0.13483f),
            PointF(0.162948f, -0.298615f),

            PointF(0.10826f, -0.378861f),
            PointF(-0.011193f, -0.381786f),
            PointF(-0.089972f, -0.438566f),

            PointF(-0.155295f, -0.485648f),
            PointF(-0.188009f, -0.570363f),
            PointF(-0.148382f, -0.640459f),

            PointF(-0.104982f, -0.717229f),
            PointF(0.00011f, -0.711159f),
            PointF(0.00136f, -0.787577f),

            PointF(0.003101f, -0.894001f),
            PointF(0.000333f, -0.937152f),
            PointF(0f, -0.95f)
        )
    }
}