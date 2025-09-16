package io.github.irgaly.navigation3.resultstate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavEntry

/**
 * Navigation3 Result State Holder
 */
class NavigationResultStateHolder <T: Any>(
    val navBackStack: SnapshotStateList<T>,
    val entryProvider: (T) -> NavEntry<*>,
    val contentKeyToString: (Any) -> String,
    /**
     * SavedState for results
     *
     * * Map Key: contentKey
     * * Map Value:
     *     * Map Key: resultKey
     *     * Map Value: result String
     */
    val savedStateResults: MutableState<Map<String, Map<String, String>>>
): NavigationResultProducer {
    /**
     * The resultKey list that each NavEntry wants to receive
     *
     * * Map Key: contentKey
     * * Map Value: resultKeys to receive
     */
    private val consumerResultKeys: State<Map<String, List<String>>> = derivedStateOf {
        navBackStack.mapNotNull { key ->
            val entry = entryProvider(key)
            val contentKeyString = contentKeyToString(entry.contentKey)
            val metadata =
                entry.metadata[NavigationResultMetadata.metadataKey] as? NavigationResultMetadata.ResultConsumer
            if (metadata != null && metadata.resultKeys.isNotEmpty()) {
                Pair(contentKeyString, metadata.resultKeys.distinct())
            } else {
                null
            }
        }.associate { it }
    }

    /**
     * The contentKey list that it is a consumer of resultKey's results
     *
     * * Map Key: resultKey
     * * Map Value: contentKeys who receive the resultKey's result
     */
    private val consumerContentKeys: State<Map<String, List<String>>> = derivedStateOf {
        consumerResultKeys.value.flatMap { entry ->
            entry.value.map { it to entry.key }
        }.groupBy({ it.first }) { it.second }
    }

    override fun setResult(
        resultKey: String,
        result: String,
    ) {
        val targetContentKeys = consumerContentKeys.value[resultKey]
        if (targetContentKeys != null) {
            val result = buildMap {
                putAll(savedStateResults.value)
                targetContentKeys.forEach { contentKey ->
                    val existing = get(contentKey) ?: emptyMap()
                    put(contentKey, existing + mapOf(resultKey to result))
                }
            }
            savedStateResults.value = result
        }
    }

    override fun clearResult(
        resultKey: String,
    ) {
        val result = savedStateResults.value.mapNotNull { entry ->
            val value = entry.value.filterKeys { it != resultKey }
            if (value.isNotEmpty()) {
                Pair(entry.key, value)
            } else {
                null
            }
        }.associate { it }
        savedStateResults.value = result
    }

    /**
     * Returns NavigationResultConsumer for the NavEntry
     */
    fun getNavigationResultConsumer(
        entry: NavEntry<*>,
    ): NavigationResultConsumer {
        val contentKeyString = contentKeyToString(entry.contentKey)
        return object: NavigationResultConsumer {
            override fun getResultState(
                resultKey: String,
            ): State<NavigationResult?> {
                val metadata =
                    entry.metadata[NavigationResultMetadata.metadataKey] as? NavigationResultMetadata.ResultConsumer
                if (metadata == null || resultKey !in metadata.resultKeys) {
                    throw IllegalStateException("resultKey \"$resultKey\" is not registered to NavEntry.metadata: entry=${entry.contentKey}")
                }
                return derivedStateOf {
                    val result = savedStateResults.value[contentKeyString]?.get(resultKey)
                    if (result != null) {
                        NavigationResult(
                            resultKey = resultKey,
                            result = result,
                        )
                    } else {
                        null
                    }
                }
            }

            override fun clearResult(resultKey: String) {
                val result = savedStateResults.value.mapNotNull { entry ->
                    val value = if (entry.key == contentKeyString) {
                        entry.value.filterKeys { it != resultKey }
                    } else {
                        entry.value
                    }
                    if (value.isNotEmpty()) {
                        Pair(entry.key, value)
                    } else {
                        null
                    }
                }.associate { it }
                savedStateResults.value = result
            }
        }
    }

    /**
     * NavEntryDecorator.onPop is occurred
     */
    fun onPop(contentKey: Any) {
        val contentKeyString = contentKeyToString(contentKey)
        val availableContentKeys = consumerResultKeys.value.keys
        if (contentKeyString !in availableContentKeys) {
            val result = savedStateResults.value.filterKeys { key ->
                (key != contentKeyString)
            }
            savedStateResults.value = result
        }
    }
}

/**
 * create and remember NavigationResultStateHolder
 *
 * @param entryProvider the same entryProvider lambda as it is specified to AppNavHost
 */
@Composable
fun <T: Any> rememberNavigationResultStateHolder(
    navBackStack: SnapshotStateList<T>,
    entryProvider: (T) -> NavEntry<*>,
    contentKeyToString: (Any) -> String = { it.toString() },
    savedStateResults: MutableState<Map<String, Map<String, String>>> = rememberSaveable {
        mutableStateOf(emptyMap())
    }
): NavigationResultStateHolder<T> {
    return remember(navBackStack, entryProvider, savedStateResults) {
        NavigationResultStateHolder(
            navBackStack = navBackStack,
            entryProvider = entryProvider,
            contentKeyToString = contentKeyToString,
            savedStateResults = savedStateResults,
        )
    }
}




