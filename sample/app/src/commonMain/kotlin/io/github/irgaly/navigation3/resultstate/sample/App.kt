package io.github.irgaly.navigation3.resultstate.sample

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.runtime.serialization.NavBackStackSerializer
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import io.github.irgaly.navigation3.resultstate.NavigationResultMetadata
import io.github.irgaly.navigation3.resultstate.rememberNavigationResultNavEntryDecorator
import io.github.irgaly.navigation3.resultstate.resultConsumer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.jetbrains.compose.ui.tooling.preview.Preview

interface Screen : NavKey

@Composable
@Preview
fun App() {
    MaterialTheme {
        val json = remember { Json }
        val configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(Screen::class) {
                    subclass(Screen1.serializer())
                    subclass(Screen2.serializer())
                    subclass(Screen3.serializer())
                }
            }
        }
        val navBackStack = rememberNavBackStack<Screen>(
            configuration = configuration,
            Screen1,
        )
        val entryProvider = entryProvider<Screen> {
            entry<Screen1>(
                metadata = NavigationResultMetadata.resultConsumer(
                    Screen2ResultKey,
                    Screen3ResultKey,
                )
            ) {
                Screen1(
                    json = json,
                    onNavigateScreen2 = {
                        navBackStack.add(Screen2)
                    }
                )
            }
            entry<Screen2>(
                metadata = NavigationResultMetadata.resultConsumer(
                    Screen3ResultKey,
                )
            ) {
                Screen2(
                    json = json,
                    onBack = {
                        if (1 < navBackStack.size) {
                            navBackStack.removeLastOrNull()
                        }
                    },
                    onNavigateScreen3 = {
                        navBackStack.add(Screen3)
                    }
                )
            }
            entry<Screen3> {
                Screen3(
                    json = json,
                    onBack = {
                        if (1 < navBackStack.size) {
                            navBackStack.removeLastOrNull()
                        }
                    },
                )
            }
        }
        NavDisplay(
            backStack = navBackStack,
            onBack = {
                navBackStack.removeLastOrNull()
            },
            entryDecorators = listOf(
                rememberNavigationResultNavEntryDecorator(
                    backStack = navBackStack,
                    entryProvider = entryProvider,
                ),
                rememberSaveableStateHolderNavEntryDecorator(),
            ),
            entryProvider = entryProvider,
        )
    }
}

@Composable
inline fun <reified T: NavKey> rememberNavBackStack(
    configuration: SavedStateConfiguration,
    vararg elements: T,
): NavBackStack<T> {
    return rememberSerializable(
        configuration = configuration,
        serializer = NavBackStackSerializer(PolymorphicSerializer(T::class)),
    ) {
        NavBackStack(*elements)
    }
}