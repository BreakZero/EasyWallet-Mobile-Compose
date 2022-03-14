import com.easy.version.BuildConfig
import com.easy.version.androidTestDependencies
import com.easy.version.dependencies.AndroidX
import com.easy.version.dependencies.Other
import com.easy.version.hiltDependencies
import com.easy.version.unitTestDependencies

dependencies {
    implementation(kotlin("stdlib-jdk8", BuildConfig.kotlinVersion))
    api(AndroidX.coreKtx)

    api(Other.threetenabp)

    api(Other.coroutineCore)
    api(Other.coroutineAndroid)
    api("com.squareup.logcat:logcat:0.1")

    api(Other.Ktor.client_core)
    api(Other.Ktor.client_cio)
    api(Other.Ktor.client_log)
    api(Other.Ktor.client_gson)
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    api("com.trustwallet:wallet-core:2.6.35") {
        exclude(group = "com.google.guava", module = "listenablefuture")
    }

    hiltDependencies()

    unitTestDependencies()
    androidTestDependencies()
}

kapt {
    correctErrorTypes = true
}
