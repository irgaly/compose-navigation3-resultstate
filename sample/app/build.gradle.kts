plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.buildlogic.android.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    androidLibrary {
        namespace = "io.github.irgaly.navigation3.resultstate.sample.app"
    }
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "App"
            isStatic = true
        }
    }
    sourceSets {
        commonMain.dependencies {
            implementation(projects.resultstate)
            implementation(libs.kotlinx.serialization.json)
            implementation(compose.material3)
            implementation(libs.compose.navigation3.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
        }
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.compose.activity)
        }
    }
}