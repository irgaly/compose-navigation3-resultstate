package io.github.irgaly.navigation3.resultstate

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation3.runtime.NavEntry
import kotlinx.serialization.json.Json

/**
 * Navigation Result Receiver
 */
interface NavigationResultConsumer {
    /**
     * Get NavigationResult as observable State
     * This function returns the result as a State, so it's recommended to use this with remembered.
     *
     * ## usage
     *
     * ```kotlin
     * val resultConsumer = LocalNavigationResultConsumer.current
     * val screen2Result by remember (resultConsumer) {
     *   resultConsumer.getResultState("Screen2ResultKey")
     * }
     * ```
     *
     * @param resultKey Result Key for receiving
     * @throws IllegalStateException resultKey is not registered to NavEntry for consuming, because [NavigationResultStateHolder.resultConsumer] is not assigned to [NavEntry.metadata].
     */
    fun getResultState(resultKey: String): State<NavigationResult?>
    /**
     * Remove NavigationResult
     *
     * This function is usually for remove a result after that did received and proceeded.
     *
     * @param resultKey Result Key to remove
     */
    fun clearResult(resultKey: String)
}

/**
 * Get SerializedNavigationResult as observable State
 * This function returns the result as a State, so it's recommended to use this with remembered.
 *
 * ## usage
 *
 * ```kotlin
 * val resultConsumer = LocalNavigationResultConsumer.current
 * val screen2Result by remember (resultConsumer) {
 *   resultConsumer.getResultState(json, Screen2ResultKey)
 * }
 * ```
 *
 * @param json a Json instance for deserialization
 * @param key Result Key for receiving
 * @throws IllegalStateException key is not registered to NavEntry for consuming, because [NavigationResultStateHolder.resultConsumer] is not assigned to [NavEntry.metadata].
 */
@Deprecated("Use getResultState(key: SerializableNavigationResultKey<Result>, json: Json) instead.")
fun <Result> NavigationResultConsumer.getResultState(
    json: Json,
    key: SerializableNavigationResultKey<Result>,
): State<SerializedNavigationResult<Result>?> {
    val resultState = getResultState(resultKey = key.resultKey)
    return derivedStateOf {
        val result = resultState.value
        if (result != null) {
            SerializedNavigationResult(
                resultKey = result.resultKey,
                resultString = result.result,
                json = json,
                serializer = key.serializer,
            )
        } else {
            null
        }
    }
}

/**
 * Get SerializedNavigationResult as observable State
 * This function returns the result as a State, so it's recommended to use this with remembered.
 *
 * ## usage
 *
 * ```kotlin
 * val resultConsumer = LocalNavigationResultConsumer.current
 * val screen2Result by remember (resultConsumer) {
 *   resultConsumer.getResultState(Screen2ResultKey)
 * }
 * ```
 *
 * @param key Result Key for receiving
 * @param json a Json instance for deserialization. This Json instance should have same configuration as that is used in NavigationResultProducer.
 * @throws IllegalStateException key is not registered to NavEntry for consuming, because [NavigationResultStateHolder.resultConsumer] is not assigned to [NavEntry.metadata].
 */
fun <Result> NavigationResultConsumer.getResultState(
    key: SerializableNavigationResultKey<Result>,
    json: Json = Json,
): State<SerializedNavigationResult<Result>?> {
    val resultState = getResultState(resultKey = key.resultKey)
    return derivedStateOf {
        val result = resultState.value
        if (result != null) {
            SerializedNavigationResult(
                resultKey = result.resultKey,
                resultString = result.result,
                json = json,
                serializer = key.serializer,
            )
        } else {
            null
        }
    }
}

/**
 * Remove NavigationResult
 *
 * This function is usually for remove a result after that did received and proceeded.
 *
 * @param key Result Key to remove
 */
fun NavigationResultConsumer.clearResult(key: SerializableNavigationResultKey<*>) {
    clearResult(resultKey = key.resultKey)
}

val LocalNavigationResultConsumer: ProvidableCompositionLocal<NavigationResultConsumer> =
    staticCompositionLocalOf {
        EmptyNavigationResultConsumer()
    }

private class EmptyNavigationResultConsumer: NavigationResultConsumer {
    override fun getResultState(resultKey: String): State<NavigationResult?> = mutableStateOf(null)
    override fun clearResult(resultKey: String) = Unit
}