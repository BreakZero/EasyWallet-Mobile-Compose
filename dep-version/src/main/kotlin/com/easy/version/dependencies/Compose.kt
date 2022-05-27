package com.easy.version.dependencies

object Compose {
    const val version = "1.2.0-beta02"
    const val ui = "androidx.compose.ui:ui:$version"
    const val material = "androidx.compose.material:material:$version"
    const val preview = "androidx.compose.ui:ui-tooling-preview:$version"
    const val icons = "androidx.compose.material:material-icons-extended:$version"
    const val uiTestJunit = "androidx.compose.ui:ui-test-junit4:$version"
    const val uiTooling = "androidx.compose.ui:ui-tooling:$version"
    const val coil = "io.coil-kt:coil-compose:2.0.0"
    const val navigation = "androidx.navigation:navigation-compose:2.4.2"
    const val paging = "androidx.paging:paging-compose:1.0.0-alpha14"


    object Navigation {

    }

    object Material3 {
        private const val version = "1.0.0-alpha12"
        const val material3 = "androidx.compose.material3:material3:$version"
        const val material3_window_size = "androidx.compose.material3:material3-window-size-class:$version"
    }

    object Hilt {
        private const val version = "2.38.1"
        const val hilt_android = "com.google.dagger:hilt-android:$version"
        const val hilt_android_compiler = "com.google.dagger:hilt-android-compiler:$version"
        const val hilt_lifecycle_viewmodel = "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03"
        const val hilt_compiler = "androidx.hilt:hilt-compiler:1.0.0"
        const val hilt_nav_compose = "androidx.hilt:hilt-navigation-compose:1.0.0-rc01"
    }

    object Accompanist {
        private const val version = "0.24.9-beta"
        const val windowInsets = "com.google.accompanist:accompanist-insets:$version"
        const val uiController = "com.google.accompanist:accompanist-systemuicontroller:$version"
        const val permission = "com.google.accompanist:accompanist-permissions:$version"
        const val navigation = "com.google.accompanist:accompanist-navigation-material:$version"
        const val swiperefresh = "com.google.accompanist:accompanist-swiperefresh:$version"
        const val navigationAnimation = "com.google.accompanist:accompanist-navigation-animation:$version"
        const val webView = "com.google.accompanist:accompanist-webview:$version"
        const val pager = "com.google.accompanist:accompanist-pager:$version"
        const val pager_indicators = "com.google.accompanist:accompanist-pager-indicators:$version"
    }
}