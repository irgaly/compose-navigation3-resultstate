package io.github.irgaly.navigation3.resultstate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.navEntryDecorator

/**
 * NavEntryDecorator to provide LocalNavigationResultProducer and LocalNavigationResultConsumer
 */
@Suppress("FunctionName")
fun <T: Any> NavigationResultNavEntryDecorator(
    navigationResultStateHolder: NavigationResultStateHolder<T>,
): NavEntryDecorator<T> {
    return navEntryDecorator(
        onPop = { contentKey ->
            navigationResultStateHolder.onPop(contentKey)
        },
        decorator = { entry ->
            CompositionLocalProvider(
                LocalNavigationResultProducer provides navigationResultStateHolder,
                LocalNavigationResultConsumer provides navigationResultStateHolder.getNavigationResultConsumer(
                    entry
                ),
            ) {
                entry.Content()
            }
        }
    )
}

/**
 * remember [NavigationResultNavEntryDecorator]
 */
@Composable
fun <T: Any> rememberNavigationResultNavEntryDecorator(
    navigationResultStateHolder: NavigationResultStateHolder<T>,
): NavEntryDecorator<T> {
    return remember(navigationResultStateHolder) {
        NavigationResultNavEntryDecorator(navigationResultStateHolder)
    }
}

/**
 * remember [NavigationResultStateHolder]
 * and then, remember [NavigationResultNavEntryDecorator]
 *
 * @param entryProvider the same entryProvider lambda as it is specified to AppNavHost
 * @param contentKeyToString lambda to convert NavEntry.contentKey to String for serialization for SavedState
 * @see [rememberNavigationResultStateHolder]
 */
@Composable
fun <T : Any> rememberNavigationResultNavEntryDecorator(
    navBackStack: SnapshotStateList<T>,
    entryProvider: (T) -> NavEntry<*>,
    contentKeyToString: (Any) -> String = { it.toString() },
    savedStateResults: MutableState<Map<String, Map<String, String>>> = rememberSaveable {
        mutableStateOf(emptyMap())
    },
): NavEntryDecorator<T> {
    val navigationResultStateHolder = rememberNavigationResultStateHolder(
        navBackStack = navBackStack,
        entryProvider = entryProvider,
        contentKeyToString = contentKeyToString,
        savedStateResults = savedStateResults,
    )
    return remember(navigationResultStateHolder) {
        NavigationResultNavEntryDecorator(navigationResultStateHolder)
    }
}