package io.github.irgaly.navigation3.resultstate.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.irgaly.navigation3.resultstate.LocalNavigationResultProducer
import io.github.irgaly.navigation3.resultstate.SerializableNavigationResultKey
import io.github.irgaly.navigation3.resultstate.setResult
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.ui.tooling.preview.Preview

@Serializable
data object Screen3 : Screen

val Screen3ResultKey = SerializableNavigationResultKey(
    serializer = Screen3Result.serializer(),
    resultKey = "Screen3Result",
)

@Serializable
data class Screen3Result(
    val result: String,
)

@Composable
fun Screen3(
    json: Json,
    onBack: (count: Int) -> Unit,
) {
    val resultProducer = LocalNavigationResultProducer.current
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text("Screen3")
            Button(onClick = {
                onBack(1)
            }) {
                Text("Back")
            }
            Button(onClick = {
                onBack(2)
            }) {
                Text("Back to Screen1")
            }
            HorizontalDivider()
            Button(onClick = {
                resultProducer.setResult(
                    json,
                     Screen3ResultKey,
                    Screen3Result("my result of screen3!")
                )
            }) {
                Text("set result3")
            }
        }
    }
}

@Preview
@Composable
private fun Screen3Preview() {
    Screen3(
        json = Json,
        onBack = {},
    )
}