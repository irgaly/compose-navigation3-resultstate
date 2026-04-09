package io.github.irgaly.navigation3.resultstate

import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.NavEntry

object NavigationResultMetadata {
    /**
     * The key for [NavEntry.metadata] to define the result keys that this entry want to receive
     *
     * ## usage
     *
     * ```kotlin
     * @Serializable sealed interface Screen: NavKey
     * val entryProvider = entryProvider<Screen> {
     *   entry<Screen1>(
     *     metadata = metadata {
     *       put(
     *         NavigationResultMetadata.ResultConsumerKey,
     *         NavigationResultMetadata.resultConsumer("Screen2Result"),
     *       )
     *     }
     *   ) {
     *     ...
     *   }
     * }
     * ...
     * ```
     */
    object ResultConsumerKey: NavMetadataKey<ResultConsumer>

    /**
     * Define the result keys that this entry want to receive
     *
     * ## usage
     *
     * ```kotlin
     * @Serializable sealed interface Screen: NavKey
     * val entryProvider = entryProvider<Screen> {
     *   entry<Screen1>(
     *     metadata = metadata {
     *       put(
     *         NavigationResultMetadata.ResultConsumerKey,
     *         NavigationResultMetadata.resultConsumer("Screen2Result"),
     *       )
     *     }
     *   ) {
     *     ...
     *   }
     * }
     * ...
     * ```
     */
    fun resultConsumer(
        vararg resultKeys: String
    ): ResultConsumer {
        return ResultConsumer(resultKeys.toList())
    }

    data class ResultConsumer(
        val resultKeys: List<String>,
    )
}

/**
 * Define the result keys that this entry want to receive
 *
 * ## usage
 *
 * ```kotlin
 * @Serializable sealed interface Screen: NavKey
 * val Screen2ResultKey = SerializableNavigationResultKey(Screen2Result.serializer(), "Screen2Result")
 * val entryProvider = entryProvider<Screen> {
 *   entry<Screen1>(
 *     metadata = metadata {
 *       put(
 *         NavigationResultMetadata.ResultConsumerKey,
 *         NavigationResultMetadata.resultConsumer(Screen2ResultKey),
 *       )
 *     }
 *   ) {
 *     ...
 *   }
 * }
 * ...
 * ```
 */
fun NavigationResultMetadata.resultConsumer(
    vararg resultKeys: SerializableNavigationResultKey<*>
): NavigationResultMetadata.ResultConsumer {
    return NavigationResultMetadata.resultConsumer(
        *resultKeys.map { it.resultKey }.toTypedArray()
    )
}