package ir.ahmadmo.math.timeseries

import kotlin.math.atan
import kotlin.math.tan

/**
 * Represents a linear line in a time-series.
 *
 *    y-axis (value)
 *          |
 *          |        line
 *          |      /
 *          |    /
 *    pivot |  /  θ
 *    ______|/_____________ x-axis (time)
 *         /|
 *       /  |
 *     /    |
 *
 * @param pivot the point which this line rotates around
 * @param theta the angle in radians between this line and the x-axis
 *              which is in range [π/2..-π/2]
 */
data class LinearLine(val pivot: Point, val theta: Double) {

    /**
     * Computes y value of intersection of this line and the y-axis for the given [time].
     */
    fun value(time: Int): Double {
        val w = time - pivot.time
        val h = tan(theta) * w
        return pivot.value + h
    }

    /**
     * Computes new theta by rotating this line to the given [point].
     */
    fun rotateTo(point: Point): Double {
        check(pivot.time != point.time)
        val w = pivot.time - point.time
        val h = pivot.value - point.value
        return atan(h / w)
    }

}
