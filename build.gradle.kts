plugins {
    kotlin("js") version "1.4.32"
}

group = "net.aridai"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    //  https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core-js
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.4.3")
}

kotlin {
    js(LEGACY) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }
}