import com.easy.version.BuildConfig
import com.easy.version.androidTestDependencies
import com.easy.version.dependencies.AndroidX
import com.easy.version.dependencies.Other
import com.easy.version.hiltDependencies
import com.easy.version.unitTestDependencies

plugins {
    kotlin("plugin.serialization") version "1.6.21"
}

dependencies {
    implementation(kotlin("stdlib-jdk8", BuildConfig.kotlin_version))
    api(AndroidX.coreKtx)

    api(Other.threetenabp)

    api(Other.coroutineCore)
    api(Other.coroutineAndroid)
    api(AndroidX.datastore_pref)
    api(AndroidX.datastore)
    api(Other.timber)

    api(Other.Ktor.client_core)
    api(Other.Ktor.client_android)
    api(Other.Ktor.client_log)
    api(Other.Ktor.client_negotiation)
    api(Other.Ktor.client_serializer)
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    api("com.trustwallet:wallet-core:2.7.7") {
        exclude(group = "com.google.guava", module = "listenablefuture")
    }

    hiltDependencies()

    unitTestDependencies()
    androidTestDependencies()
}

kapt {
    correctErrorTypes = true
}
