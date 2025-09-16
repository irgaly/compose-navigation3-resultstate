package io.github.irgaly.navigation3.resultstate

object NavigationResultMetadata {
    val metadataKey: String = "io.github.irgaly.navigation3.resultstate.NavigationResultMetadata.metadataKey"

    /**
     * Define the result keys that this entry want to receive
     *
     * ## usage
     *
     * ```kotlin
     * @Serializable sealed interface Screen: NavKey
     * val entryProvider = entryProvider<Screen> {
     *   entry<Screen1>(
     *     metadata = NavigationResultMetadata.resultConsumer(
     *        "Screen2Result",
     *     )
     *   ) {
     *     ...
     *   }
     * }
     * ...
     * ```
     */
    fun resultConsumer(
        vararg resultKeys: String
    ): Map<String, Any> {
        return mapOf(
            metadataKey to ResultConsumer(resultKeys.toList())
        )
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
 *     metadata = NavigationResultMetadata.resultConsumer(
 *        Screen2ResultKey,
 *     )
 *   ) {
 *     ...
 *   }
 * }
 * ...
 * ```
 */
fun NavigationResultMetadata.resultConsumer(
    vararg resultKeys: SerializableNavigationResultKey<*>
): Map<String, Any> {
    return NavigationResultMetadata.resultConsumer(
        *resultKeys.map { it.resultKey }.toTypedArray()
    )
}