package com.easy.version

import com.easy.version.dependencies.AndroidX
import com.easy.version.dependencies.Compose
import com.easy.version.dependencies.Other
import com.easy.version.dependencies.Tester
import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.unitTestDependencies() {
    "testImplementation"("junit:junit:4.13.2")
    "testImplementation"(Tester.arch_core_testing)
    "testImplementation"(Tester.mockito_kotlin)
    "testImplementation"(Tester.coroutines_test)
    "testImplementation"(Tester.turbine)
    "testImplementation"(Other.Ktor.client_test)
}

fun DependencyHandlerScope.androidTestDependencies() {
    "androidTestImplementation"("androidx.test.ext:junit:1.1.3")
    "androidTestImplementation"("androidx.test.espresso:espresso-core:3.4.0")
    "androidTestImplementation"("com.google.guava:guava:30.1.1-android")
    "androidTestImplementation"(Tester.arch_core_testing)
    "androidTestImplementation"(Tester.mockito_kotlin)
    "androidTestImplementation"(Tester.coroutines_test)
    "androidTestImplementation"(Tester.turbine)
    "androidTestImplementation"(Other.Ktor.client_test)
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
    "implementation"(Compose.Material3.material3)
    "implementation"(Compose.preview)
    "implementation"(Compose.icons)
    "implementation"(Compose.coil)
    "implementation"(Compose.paging)
    "implementation"(Compose.navigation)
    "implementation"(Compose.Accompanist.windowInsets)
    "implementation"(Compose.Accompanist.uiController)
    "implementation"(Compose.Accompanist.permission)
    "implementation"(Compose.Accompanist.navigation)
    "implementation"(Compose.Accompanist.swiperefresh)
    "implementation"(Compose.Accompanist.navigationAnimation)
    "implementation"(Compose.Accompanist.webView)
    "implementation"(Compose.Accompanist.pager)
    "implementation"(Compose.Accompanist.pager_indicators)
}