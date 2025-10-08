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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.irgaly.navigation3.resultstate.LocalNavigationResultConsumer
import io.github.irgaly.navigation3.resultstate.LocalNavigationResultProducer
import io.github.irgaly.navigation3.resultstate.SerializableNavigationResultKey
import io.github.irgaly.navigation3.resultstate.getResultState
import io.github.irgaly.navigation3.resultstate.setResult
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.ui.tooling.preview.Preview

@Serializable
data object Screen2 : Screen

val Screen2ResultKey = SerializableNavigationResultKey(
    serializer = Screen2Result.serializer(),
    resultKey = "Screen2Result",
)

@Serializable
data class Screen2Result(
    val result: String,
)

@Composable
fun Screen2(
    json: Json,
    onBack: () -> Unit,
    onNavigateScreen3: () -> Unit
) {
    val resultProducer = LocalNavigationResultProducer.current
    val resultConsumer = LocalNavigationResultConsumer.current
    var result3 by rememberSaveable {
        mutableStateOf("{empty}")
    }
    val screen3Result by remember(resultConsumer) {
        resultConsumer.getResultState(json, Screen3ResultKey)
    }
    LaunchedEffect(screen3Result) {
        val result = screen3Result
        if (result != null) {
            result3 = result.getResult().result
            resultConsumer.clearResult(result.resultKey)
        }
    }
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text("Screen2")
            Button(onClick = {
                onBack()
            }) {
                Text("Back")
            }
            Button(onClick = {
                onNavigateScreen3()
            }) {
                Text("to Screen3")
            }
            HorizontalDivider()
            Button(onClick = {
                resultProducer.setResult(
                    json,
                    Screen2ResultKey,
                    Screen2Result("my result of screen2!"),
                )
            }) {
                Text("set result2")
            }
            HorizontalDivider()
            Text("Screen3 Result: $result3")
        }
    }
}

@Preview
@Composable
private fun Screen2Preview() {
    Screen2(
        json = Json,
        onBack = {},
        onNavigateScreen3 = {},
    )
}