package com.easy.version

import com.android.build.gradle.BaseExtension
import com.easy.version.dependencies.Compose
import org.gradle.api.DefaultTask
import org.gradle.api.JavaVersion
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.create

abstract class AndroidConfigTask : DefaultTask() {
    private val baseExtension: BaseExtension =
        project.extensions.getByName("android") as? BaseExtension
            ?: project.extensions.create<BaseExtension>("android")

    @TaskAction
    fun execute() {
        baseExtension.run {
            compileSdkVersion(BuildConfig.compileSdkVersion)
            defaultConfig {
                minSdk = BuildConfig.minSdkVersion
                targetSdk = BuildConfig.targetSdkVersion
            }
            buildTypes {
                getByName("release") {
                    isMinifyEnabled = false
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }
            buildFeatures.also {
                it.compose = true
            }
            composeOptions {
                kotlinCompilerExtensionVersion = Compose.version
            }
        }
        println("execute end!!!")
    }
}