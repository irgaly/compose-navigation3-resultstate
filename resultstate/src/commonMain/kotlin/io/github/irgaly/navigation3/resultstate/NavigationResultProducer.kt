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
 * @param json a Json instance for serialization. This Json instance should have same configuration as that is used in NavigationResultConsumer.
 * @param key Result Key for producing
 * @param result Result value
 * @throws SerializationException rethrows from [Json.decodeFromString]
 * @throws IllegalArgumentException rethrows from [Json.decodeFromString]
 */
@Deprecated("Use setResult(key: SerializableNavigationResultKey<Result>, result: Result, json: Json) instead.")
fun <Result> NavigationResultProducer.setResult(
    json: Json,
    key: SerializableNavigationResultKey<Result>,
    result: Result,
) {
    setResult(key.resultKey, json.encodeToString(key.serializer, result))
}

/**
 * register Navigation Result with encoding to JSON String
 *
 * @param key Result Key for producing
 * @param result Result value
 * @param json a Json instance for serialization. This Json instance should have same configuration as that is used in NavigationResultConsumer.
 * @throws SerializationException rethrows from [Json.decodeFromString]
 * @throws IllegalArgumentException rethrows from [Json.decodeFromString]
 */
fun <Result> NavigationResultProducer.setResult(
    key: SerializableNavigationResultKey<Result>,
    result: Result,
    json: Json = Json,
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
