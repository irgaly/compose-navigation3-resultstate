import org.jetbrains.dokka.gradle.tasks.DokkaGeneratePublicationTask

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.buildlogic.multiplatform.library)
    alias(libs.plugins.dokka)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    androidLibrary {
        namespace = "io.github.irgaly.navigation3.resultstate"
    }
    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.serialization.json)
            api(libs.compose.navigation3.runtime)
        }
        commonTest.dependencies {
        }
    }
}

val dokkaGeneratePublicationHtml by tasks.getting(DokkaGeneratePublicationTask::class)
val javadocJar by tasks.registering(Jar::class) {
    from(dokkaGeneratePublicationHtml.outputDirectory)
    archiveClassifier = "javadoc"
}