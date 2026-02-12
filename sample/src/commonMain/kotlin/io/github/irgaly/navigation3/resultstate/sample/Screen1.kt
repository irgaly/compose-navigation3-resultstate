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
import io.github.irgaly.navigation3.resultstate.getResultState
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.ui.tooling.preview.Preview

@Serializable
data object Screen1 : Screen

@Composable
fun Screen1(
    json: Json,
    onNavigateScreen2: () -> Unit
) {
    val resultConsumer = LocalNavigationResultConsumer.current
    var result2 by rememberSaveable {
        mutableStateOf("{empty}")
    }
    var result3 by rememberSaveable {
        mutableStateOf("{empty}")
    }
    val screen2Result by remember (resultConsumer) {
        resultConsumer.getResultState(Screen2ResultKey, json)
    }
    LaunchedEffect(screen2Result) {
        val result = screen2Result
        if (result != null) {
            result2 = result.getResult().result
            resultConsumer.clearResult(result.resultKey)
        }
    }
    val screen3Result by remember {
        resultConsumer.getResultState(Screen3ResultKey, json)
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
            Text("Screen1")
            Button(onClick = {
                onNavigateScreen2()
            }) {
                Text("to Screen2")
            }
            HorizontalDivider()
            Button(onClick = {
                result2 = "{reset}"
                result3 = "{reset}"
            }) {
                Text("Reset values")
            }
            Text("Screen2 Result: $result2")
            Text("Screen3 Result: $result3")
        }
    }
}

@Preview
@Composable
private fun Screen1Preview() {
    Screen1(
        json = Json,
        onNavigateScreen2 = {},
    )
}