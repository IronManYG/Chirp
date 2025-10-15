import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "dev.gaddal.convention.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    implementation(libs.buildkonfig.gradlePlugin)
    implementation(libs.buildkonfig.compiler)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "dev.gaddal.convention.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("androidComposeApplication") {
            id = "dev.gaddal.convention.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }

        register("cmpApplication") {
            id = "dev.gaddal.convention.cmp.application"
            implementationClass = "CmpApplicationConventionPlugin"
        }

        register("kmpLibrary") {
            id = "dev.gaddal.convention.kmp.library"
            implementationClass = "KmpLibraryConventionPlugin"
        }

        register("cmpLibrary") {
            id = "dev.gaddal.convention.cmp.library"
            implementationClass = "CmpLibraryConventionPlugin"
        }

        register("cmpFeature") {
            id = "dev.gaddal.convention.cmp.feature"
            implementationClass = "CmpFeatureConventionPlugin"
        }

        register("buildKonfig") {
            id = "dev.gaddal.convention.buildkonfig"
            implementationClass = "BuildKonfigConventionPlugin"
        }
    }
}