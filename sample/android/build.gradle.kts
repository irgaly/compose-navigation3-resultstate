import io.github.irgaly.buildlogic.configureAndroid

plugins {
    alias(libs.plugins.buildlogic.android.application)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

configureAndroid()

android {
    namespace = "io.github.irgaly.navigation3.resultstate.sample"
}

dependencies {
    implementation(projects.sample.app)
    implementation(libs.compose.activity)
    implementation(compose.preview)
    debugImplementation(compose.uiTooling)
}