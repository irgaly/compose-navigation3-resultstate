package io.github.irgaly.navigation3.resultstate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.navEntryDecorator

/**
 * NavEntryDecorator to provides LocalNavigationResultProducer and LocalNavigationResultConsumer
 */
@Suppress("FunctionName")
fun <T: Any> NavigationResultNavEntryDecorator(
    navigationResultStateHolder: NavigationResultStateHolder<T>,
): NavEntryDecorator<Any> {
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

@Composable
fun <T: Any> rememberNavigationResultNavEntryDecorator(
    navigationResultStateHolder: NavigationResultStateHolder<T>,
): NavEntryDecorator<Any> {
    return remember(navigationResultStateHolder) {
        NavigationResultNavEntryDecorator(navigationResultStateHolder)
    }
}