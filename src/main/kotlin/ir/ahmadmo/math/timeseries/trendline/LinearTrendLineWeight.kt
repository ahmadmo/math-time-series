package ir.ahmadmo.math.timeseries.trendline

import ir.ahmadmo.math.epsilonRange
import kotlin.math.sign

/**
 * Represents weight of a [LinearTrendLine] separated by its pivot.
 * Weight is simply the area between a trend-line and a time-series.
 * Notice that trend-line and time-series must intersect each other only at local maximum(s).
 *
 *         pivot (max)
 *     _________*______________________ line
 *     ......../\......................
 *     ....../   \..............max....
 *     ..../      \............./\.....
 *     __/         \__......../   \____ time-series
 *                    \_____/
 *
 *     weight = dotted area
 *
 * @param left computed weight from start to pivot - 1
 * @param right computed weight from pivot + 1 to end
 */
data class LinearTrendLineWeight(val left: Double, val right: Double) {

    /**
     * Indicates which side of the [LinearTrendLine] has more weight. Returns:
     *   - `1` if left side has more weight,
     *   - zero if weight is (approximately) balanced between left and right sides,
     *   - `-1` if right side has more weight
     */
    val sign: Int
        get() {
            val d = left - right
            return if (d in epsilonRange) 0 else sign(d).toInt()
        }

}
