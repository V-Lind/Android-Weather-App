package main.src.openmeteoweatherapp.ui.util

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries


@Composable
fun VicoLineGraph(temperatures: List<Double>?) {
    val modelProducer = remember { CartesianChartModelProducer.build() }
    if (temperatures == null) {
        return
    }

    val chart = rememberCartesianChart(
        rememberLineCartesianLayer(),
        startAxis = rememberStartAxis(
            guideline = null
        ),
        bottomAxis = rememberBottomAxis(
            itemPlacer = AxisItemPlacer.Horizontal.default(
                spacing = 1,
                offset = 24,
                shiftExtremeTicks = true
            ),
            guideline = null
        ),

    )

    modelProducer.tryRunTransaction {
        lineSeries {
            series(temperatures)
        }
    }

    CartesianChartHost(
        chart = chart,
        modelProducer = modelProducer,
        modifier = Modifier.height(150.dp)
    )
}




