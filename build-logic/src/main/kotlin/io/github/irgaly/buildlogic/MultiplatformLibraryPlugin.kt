package io.github.irgaly.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project

class MultiplatformLibraryPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.pluginId("kotlin-multiplatform"))
                apply(libs.pluginId("ksp"))
                apply(libs.pluginId("android-library"))
                apply(libs.pluginId("kotest"))
            }
            configureMultiplatformLibrary()
            configureAndroidLibrary()
        }
    }
}
