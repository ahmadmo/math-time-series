package ir.ahmadmo.math.timeseries

/**
 * A time-series is simply an array of [Double]s.
 */
typealias TimeSeries = DoubleArray

/**
 * Tests whether the value at the given [time] is a local maximum or not.
 *
 *           max
 *           /\
 *         /   \     max
 *     __/      \_____
 *                    \__ time-series
 */
fun TimeSeries.isLocalMax(time: Int): Boolean {
    val prev = getOrNull(time - 1) ?: Double.NEGATIVE_INFINITY
    val next = getOrNull(time + 1) ?: Double.NEGATIVE_INFINITY
    return get(time) - prev > next - get(time)
}

/**
 * Reflects values of this time-series above the horizontal line
 * which intersects the global maximum.
 *
 *                          __
 *       __          _____/
 *         \       /
 *          \    /
 *           \ /
 *      --max-*------------ horizontal line
 *           /\
 *         /   \
 *     __/      \_____
 *                    \__ time-series
 */
fun TimeSeries.mirror(): TimeSeries {
    val max = maxOrNull()!!
    return TimeSeries(size) { x -> max + (max - get(x)) }
}
