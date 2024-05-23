import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun CustomPieChart(
    data: List<Pair<String, Float>>,
    modifier: Modifier = Modifier
) {
    val totalValue = data.sumOf { it: Pair<String, Float> -> it.second }
    val startAngle = -90f
    val colors = listOf(
        Color.Blue,
        Color.Green,
        Color.Red,
        Color.Yellow,
        Color.Magenta,
        Color.Cyan
    )
    Column(modifier = modifier) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            Canvas(
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp)
            ) {
                var currentAngle: Float = startAngle
                data.forEach { (label, value) ->
                    val sweepAngle: Float = (value / totalValue.toFloat()) * 360f // Explicitly specify Float
                    drawArc(
                        color = colors[Random.nextInt(colors.size)],
                        startAngle = currentAngle,
                        sweepAngle = sweepAngle,
                        useCenter = true,
                        topLeft = Offset.Zero,
                        size = Size(size.width, size.height),
                        style = Stroke(width = 60.dp.toPx().toFloat())
                    )
                    currentAngle += sweepAngle
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                data.forEach { (label, value) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(getRandomColor(), shape = CircleShape)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "$label: ${"%.1f".format((value / totalValue.toFloat()) * 100)}%")
                    }
                }
            }
        }
    }
}

private fun <T> Iterable<T>.sumOf(function: (T) -> Float): Float {
    var sum = 0f
    for (element in this) {
        sum += function(element)
    }
    return sum
}


@Composable
fun getRandomColor(): Color {
    val colors = listOf(
        Color.Blue,
        Color.Green,
        Color.Red,
        Color.Yellow,
        Color.Magenta,
        Color.Cyan
    )
    return colors.random()
}
