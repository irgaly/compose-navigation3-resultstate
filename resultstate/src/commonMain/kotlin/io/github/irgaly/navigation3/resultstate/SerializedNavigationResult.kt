package io.github.irgaly.navigation3.resultstate

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

/**
 * Navigation Result with JSON deserialization feature
 */
data class SerializedNavigationResult<Result>(
    val resultKey: String,
    val resultString: String,
    val json: Json,
    val serializer: KSerializer<Result>,
) {
    /**
     * @throws SerializationException rethrows from [Json.decodeFromString]
     * @throws IllegalArgumentException rethrows from [Json.decodeFromString]
     */
    fun getResult(): Result {
        return json.decodeFromString(
            serializer,
            resultString
        )
    }
}