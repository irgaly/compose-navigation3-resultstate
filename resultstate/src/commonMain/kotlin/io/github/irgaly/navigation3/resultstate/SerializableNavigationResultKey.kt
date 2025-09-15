package io.github.irgaly.navigation3.resultstate

import kotlinx.serialization.KSerializer

/**
 * Typed Navigation Result Key
 */
open class SerializableNavigationResultKey<Result>(
    val serializer: KSerializer<Result>,
    val resultKey: String
)