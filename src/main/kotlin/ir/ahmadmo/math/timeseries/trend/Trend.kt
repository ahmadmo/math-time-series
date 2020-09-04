package ir.ahmadmo.math.timeseries.trend

import ir.ahmadmo.math.EPSILON
import ir.ahmadmo.math.timeseries.*
import org.apache.commons.math3.stat.StatUtils
import kotlin.math.abs
import kotlin.math.atan

/**
 * A trend is simply a [TimeSeries].
 */
typealias Trend = TimeSeries

fun Trend.linearTrendLine(): LinearTrendLine {

    check(isNotEmpty())

    var line = withIndex().maxByOrNull { it.value }!!.let { maxY ->
        LinearTrendLine(Point(maxY.index, maxY.value), theta = 0.0)
    }

    while (true) {

        val weightSign = line.weight(this).sign
        if (weightSign == 0) break

        val direction = if (weightSign == 1) Side.LEFT else Side.RIGHT

        val newPivot = line.nextPivot(this, direction) ?: break
        val newTheta = line.rotateTo(newPivot)
        val newLine = LinearTrendLine(newPivot, newTheta)
        val newWeightSign = newLine.weight(this).sign

        if (weightSign + newWeightSign == 0) {
            val height = line.pivot.value - newPivot.value
            var solution = line.copy(theta = newTheta)
            binarySearch(height) { h ->
                val p = newPivot.copy(value = newPivot.value + h)
                solution = line.copy(theta = line.rotateTo(p))
                solution.weight(this).sign * weightSign
            }
            return solution
        }

        line = newLine
    }

    return line
}

private fun binarySearch(length: Double, comparator: (Double) -> Int) {
    var low = 0.0
    var high = length - EPSILON
    while (low <= high) {
        val mid = (low + high) / 2.0
        val cmp = comparator(mid)
        when {
            cmp < 0 -> low = mid + EPSILON
            cmp > 0 -> high = mid - EPSILON
            else -> break
        }
    }
}

fun Trend.linearTrendLines(smoothingFactors: DoubleArray): LinearTrendLines {

    val resistance = linearTrendLine()
    val support = mirror().linearTrendLine().mirror(this)
    val average = average(resistance, support)

    return if (smoothingFactors.isEmpty()) {
        LinearTrendLines(resistance, support, average)
    } else {
        val smoothed = smooth(average, smoothingFactors.first())
        smoothed.linearTrendLines(smoothingFactors.sliceArray(1 until smoothingFactors.size))
    }
}

fun Trend.average(resistance: LinearTrendLine, support: LinearTrendLine): LinearTrendLine {
    check(isNotEmpty())
    val w = size - 1
    val yStart = (resistance.value(0) + support.value(0)) / 2.0
    val yEnd = (resistance.value(w) + support.value(w)) / 2.0
    val h = yEnd - yStart
    return LinearTrendLine(Point(0, yStart), theta = atan(h / w))
}

fun Trend.smooth(average: LinearTrendLine, factor: Double): Trend {
    check(isNotEmpty())
    check(factor in 0.0..1.0)
    val avg = Trend(size, average::value)
    val dist = DoubleArray(size) { x -> abs(avg[x] - get(x)) }
    val maximaDist = dist.filterIndexed { x, y -> y >= avg[x] && isLocalMax(x) }.toDoubleArray()
    val minimaDist = dist.filterIndexed { x, y -> y < avg[x] && isLocalMin(x) }.toDoubleArray()
    val pth = (1.0 - factor) * 100.0
    val smoothedMaxima = StatUtils.percentile(maximaDist, pth)
    val smoothedMinima = StatUtils.percentile(minimaDist, pth)
    return Trend(size) { x ->
        val y = get(x)
        if (y >= avg[x]) {
            val error = dist[x] - smoothedMaxima
            if (error > 0.0) y - error else y
        } else {
            val error = dist[x] - smoothedMinima
            if (error > 0.0) y + error else y
        }
    }
}
