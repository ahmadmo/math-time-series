package ir.ahmadmo.math.timeseries.trendline

import ir.ahmadmo.math.timeseries.TimeSeries
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.Styler
import org.knowm.xchart.style.markers.SeriesMarkers
import kotlin.math.abs
import kotlin.math.min

fun main() {

    val trend = generateRandomTrend(size = 120)
    val smoothingFactors = doubleArrayOf(0.01, 0.02, 0.05, 0.1, 0.15, 0.25)

    val (resistance, support, average) = trend.linearTrendLines(smoothingFactors)

    val resistanceData = DoubleArray(trend.size, resistance::value)
    val supportData = DoubleArray(trend.size, support::value)
    val averageData = DoubleArray(trend.size, average::value)

    val chart = XYChartBuilder()
        .width(1280).height(720)
        .theme(Styler.ChartTheme.Matlab)
        .title("Trend Lines")
        .xAxisTitle("Day").yAxisTitle("Price")
        .build()

    chart.styler.isPlotGridLinesVisible = false
    chart.styler.xAxisTickMarkSpacingHint = 100

    val xData = DoubleArray(trend.size, Int::toDouble)

    chart.addSeries("Trend", xData, trend)
    chart.addSeries("Resistance", xData, resistanceData)
    chart.addSeries("Support", xData, supportData)
    chart.addSeries("Average", xData, averageData)

    chart.seriesMap.values.forEach { it.marker = SeriesMarkers.NONE }

    SwingWrapper(chart).displayChart()
}

fun generateRandomTrend(size: Int): TimeSeries {
    val points = TimeSeries(size)
    var min = Double.MAX_VALUE
    for (x in points.indices.drop(1)) {
        points[x] = points[x - 1] + Math.random() - 0.5
        min = min(min, points[x])
    }
    val shift = abs(min(min, 0.0))
    return if (shift == 0.0) points
    else TimeSeries(size) { x -> points[x] + shift }
}
