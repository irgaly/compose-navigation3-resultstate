package io.github.irgaly.navigation3.resultstate

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

/**
 * Navigation Result Setter
 */
interface NavigationResultProducer {
    /**
     * register Navigation Result
     *
     * @param resultKey Result Key to produce
     * @param result Result String
     */
    fun setResult(resultKey: String, result: String)

    /**
     * remove Navigation Result
     *
     * @param resultKey Result Key to produce
     */
    fun clearResult(resultKey: String)
}

/**
 * register Navigation Result with encoding to JSON String
 *
 * @throws SerializationException rethrows from [Json.decodeFromString]
 * @throws IllegalArgumentException rethrows from [Json.decodeFromString]
 */
fun <Result> NavigationResultProducer.setResult(
    json: Json,
    key: SerializableNavigationResultKey<Result>,
    result: Result,
) {
    setResult(key.resultKey, json.encodeToString(key.serializer, result))
}

/**
 * remove Navigation Result
 *
 * @param key Result Key to produce
 */
fun NavigationResultProducer.clearResult(key: SerializableNavigationResultKey<*>) {
    clearResult(key.resultKey)
}

val LocalNavigationResultProducer: ProvidableCompositionLocal<NavigationResultProducer> =
    staticCompositionLocalOf {
        EmptyNavigationResultProducer()
    }

private class EmptyNavigationResultProducer: NavigationResultProducer {
    override fun setResult(resultKey: String, result: String) = Unit
    override fun clearResult(resultKey: String) = Unit
}
