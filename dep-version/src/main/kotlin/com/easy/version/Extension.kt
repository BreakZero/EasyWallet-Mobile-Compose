package com.easy.version

import com.easy.version.dependencies.AndroidX
import com.easy.version.dependencies.Compose
import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.unitTestDependencies() {
    "testImplementation"("junit:junit:4.13.2")
}

fun DependencyHandlerScope.androidTestDependencies() {
    "androidTestImplementation"("androidx.test.ext:junit:1.1.3")
    "androidTestImplementation"("androidx.test.espresso:espresso-core:3.4.0")
}

fun DependencyHandlerScope.hiltDependencies() {
    "implementation"(Compose.Hilt.hilt_android)
    "kapt"(Compose.Hilt.hilt_android_compiler)
    "implementation"(Compose.Hilt.hilt_lifecycle_viewmodel)
    "kapt"(Compose.Hilt.hilt_compiler)
    "implementation"(Compose.Hilt.hilt_nav_compose)
}

fun DependencyHandlerScope.roomDependencies() {
    "kapt"(AndroidX.Room.roomCompiler)
    "implementation"(AndroidX.Room.roomKtx)
    "implementation"(AndroidX.Room.roomRuntime)
}

fun DependencyHandlerScope.composeUI() {
    "implementation"(Compose.ui)
    "implementation"(Compose.material)
    "implementation"(Compose.preview)
    "implementation"(Compose.icons)
    "implementation"(Compose.coil)
    "implementation"(Compose.navigation)
    "implementation"(Compose.Accompanist.windowInsets)
    "implementation"(Compose.Accompanist.uiController)
    "implementation"(Compose.Accompanist.permission)
    "implementation"(Compose.Accompanist.navigation)
    "implementation"(Compose.Accompanist.webview)
    "implementation"(Compose.Accompanist.swiperefresh)
    "implementation"(Compose.Accompanist.navigationAnimation)
    "implementation"(Compose.Accompanist.pager)
    "implementation"(Compose.Accompanist.pager_indicators)
}