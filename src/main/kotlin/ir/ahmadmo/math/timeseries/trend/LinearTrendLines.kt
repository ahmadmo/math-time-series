package ir.ahmadmo.math.timeseries.trend

/**
 * A linear trend line is a bounding linear line for the y value movement of a trend.
 *
 * @param resistance a resistance trend line is formed when y value increases and then rebounds at a pivot point
 *                   that aligns with at least two previous resistance pivot points.
 * @param support    a support trend line is formed when y value decreases and then rebounds at a pivot point
 *                   that aligns with at least two previous support pivot points.
 * @param average    represents the average or the mirror line of resistance and support trend lines.
 *
 * @see ir.ahmadmo.math.timeseries.LinearLine
 * @see LinearTrendLine
 */
data class LinearTrendLines(
    val resistance: LinearTrendLine,
    val support: LinearTrendLine,
    val average: LinearTrendLine
)
