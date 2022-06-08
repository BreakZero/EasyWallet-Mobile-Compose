import com.easy.version.androidTestDependencies
import com.easy.version.hiltDependencies
import com.easy.version.roomDependencies
import com.easy.version.unitTestDependencies

plugins {
    kotlin("plugin.serialization") version "1.6.21"
}

dependencies {
    implementation(project(":core"))
    implementation(project(":assets:domain"))
    implementation(project(":wallets"))

    roomDependencies()
    hiltDependencies()
    unitTestDependencies()
    androidTestDependencies()
}