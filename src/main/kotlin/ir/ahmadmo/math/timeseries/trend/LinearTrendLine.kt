package ir.ahmadmo.math.timeseries.trend

import ir.ahmadmo.math.EPSILON
import ir.ahmadmo.math.timeseries.LinearLine
import ir.ahmadmo.math.timeseries.Point
import ir.ahmadmo.math.timeseries.Side
import ir.ahmadmo.math.timeseries.isLocalMax

/**
 * A linear trend line is a bounding [LinearLine] for the y value movement of a trend.
 */
typealias LinearTrendLine = LinearLine

fun LinearTrendLine.weight(trend: Trend): LinearTrendLineWeight {
    fun height(x: Int) = value(x) - trend[x]
    val left = Side.LEFT.range(trend, pivot).sumByDouble(::height)
    val right = Side.RIGHT.range(trend, pivot).sumByDouble(::height)
    return LinearTrendLineWeight(left, right)
}

fun LinearTrendLine.nextMaxPivot(trend: Trend, side: Side): Point? =
    side.range(trend, pivot)
        .asSequence()
        .filter(trend::isLocalMax)
        .map { time ->
            val p = Point(time, trend[time])
            LinearTrendLine(p, rotateTo(p))
        }
        .filter { line ->
            trend.indices.none { x -> line.value(x) < trend[x] - EPSILON }
        }
        .sortedByDescending(LinearTrendLine::theta)
        .firstOrNull()?.pivot

fun LinearTrendLine.mirror(trend: Trend) =
    LinearTrendLine(pivot.copy(value = trend[pivot.time]), -theta)
