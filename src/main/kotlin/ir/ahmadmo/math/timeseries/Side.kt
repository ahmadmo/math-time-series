package ir.ahmadmo.math.timeseries

enum class Side {

    LEFT {
        override fun range(ts: TimeSeries, point: Point): IntRange {
            check(point.time in ts.indices)
            return 0 until point.time
        }
    },

    RIGHT {
        override fun range(ts: TimeSeries, point: Point): IntRange {
            check(point.time in ts.indices)
            return point.time + 1 until ts.size
        }
    };

    abstract fun range(ts: TimeSeries, point: Point): IntRange
}
