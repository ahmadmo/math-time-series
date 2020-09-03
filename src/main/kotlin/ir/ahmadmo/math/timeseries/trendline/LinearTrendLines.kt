package ir.ahmadmo.math.timeseries.trendline

/**
 * A linear trend line is a bounding [LinearTrendLine] for the y value movement of a time-series.
 *
 * @param resistance a resistance trend line is formed when y value increases and then rebounds at a pivot point
 *                   that aligns with at least two previous resistance pivot points.
 * @param support    a support trend line is formed when y value decreases and then rebounds at a pivot point
 *                   that aligns with at least two previous support pivot points.
 * @param average    represents the average or the mirror line of the resistance and the support trend lines.
 */
data class LinearTrendLines(
    val resistance: LinearTrendLine,
    val support: LinearTrendLine,
    val average: LinearTrendLine
)
