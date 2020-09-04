# Mathematics for Time Series

The goal of this library is to contain a bunch of common time-series analysis tools and indicators mostly used in [technical analysis](https://en.wikipedia.org/wiki/Technical_analysis) in finance.

## List of contents

- [Linear Trend Line](#linear-trend-line)

## Build Instructions

You can use Maven or Gradle to add the library to your project.

#### Maven
```xml
<dependency>
    <groupId>com.github.ahmadmo</groupId>
    <artifactId>math-time-series</artifactId>
    <version>0.0.4</version>
</dependency>
```

#### Gradle
```gradle
dependencies {
    compile 'com.github.ahmadmo:math-time-series:0.0.4'
}
```

## Linear Trend Line

#### Imports
```kotlin
import ir.ahmadmo.math.timeseries.trend.*
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.Styler
import org.knowm.xchart.style.markers.SeriesMarkers
import kotlin.math.abs
import kotlin.math.min
```

You need to add the dependency for [xchart](https://knowm.org/open-source/xchart/) to run the sample code:

```xml
<dependency>
    <groupId>org.knowm.xchart</groupId>
    <artifactId>xchart</artifactId>
    <version>3.6.5</version>
</dependency>
```

#### Sample Code
```kotlin
val trend = generateRandomTrend(size = 120)
val smoothingFactors = doubleArrayOf(0.01, 0.02, 0.05, 0.1, 0.15, 0.25)

val (resistance, support, average) = trend.linearTrendLines(smoothingFactors)

val resistanceData = DoubleArray(trend.size, resistance::value)
val supportData = DoubleArray(trend.size, support::value)
val averageData = DoubleArray(trend.size, average::value)
```
Notice that this is the user's responsibility to choose the rigth values for `smoothingFactors` from `0.0` to `1.0` to achieve better results. Choosing higher values results in smoother trends and consequently tighter trend lines. An empty `smoothingFactors` disables smoothing.

The function for generating a random trend (time-series):

```kotlin
fun generateRandomTrend(size: Int): Trend {
    val trend = Trend(size)
    var min = Double.MAX_VALUE
    for (x in trend.indices.drop(1)) {
        trend[x] = trend[x - 1] + Math.random() - 0.5
        min = min(min, trend[x])
    }
    val shift = abs(min(min, 0.0))
    return if (shift == 0.0) trend
    else Trend(size) { x -> trend[x] + shift }
}
```

Finally, to visualize the computed trend lines using xchart: ([learn more](https://knowm.org/open-source/xchart/xchart-example-code/))
```kotlin
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
```

[Here](https://github.com/ahmadmo/math-time-series/blob/master/src/test/kotlin/ir/ahmadmo/math/timeseries/trend/LinearTrendLineDrawer.kt) is the complete sample code demonstrating how to draw linear trend lines for a time-series.

#### Sample Results

|||
| ------------- | ------------- |
| ![1.png](https://github.com/ahmadmo/math-time-series/blob/master/src/test/resources/image/trendline/linear/1.png) | ![2.png](https://github.com/ahmadmo/math-time-series/blob/master/src/test/resources/image/trendline/linear/2.png) |
| ![3.png](https://github.com/ahmadmo/math-time-series/blob/master/src/test/resources/image/trendline/linear/3.png) | ![4.png](https://github.com/ahmadmo/math-time-series/blob/master/src/test/resources/image/trendline/linear/4.png) |
| ![5.png](https://github.com/ahmadmo/math-time-series/blob/master/src/test/resources/image/trendline/linear/5.png) | ![6.png](https://github.com/ahmadmo/math-time-series/blob/master/src/test/resources/image/trendline/linear/6.png) |
