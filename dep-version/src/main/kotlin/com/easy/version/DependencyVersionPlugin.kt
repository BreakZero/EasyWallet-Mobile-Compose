package com.easy.version

import com.android.build.gradle.LibraryExtension
import com.easy.version.dependencies.Compose
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.*

class DependencyVersionPlugin : Plugin<Project> {
    private val ignoreList = listOf("app", "EasyWallet", "assets", "dapp", "settings")

    override fun apply(target: Project) {
        if (target.name !in ignoreList) {
            target.beforeEvaluate { applyPlugin() }
            target.afterEvaluate {
                extensions.getByType(LibraryExtension::class).run {
                    moduleConfig(target.name == "core")
                }
            }
        }
    }

    private fun Project.applyPlugin() {
        apply(plugin = "com.android.library")
        apply(plugin = "kotlin-android")
        apply(plugin = "kotlin-kapt")
        apply(plugin = "kotlin-parcelize")
    }

    @Suppress("UnstableApiUsage")
    private fun LibraryExtension.moduleConfig(isCore: Boolean) {
        compileSdk = BuildConfig.compileSdkVersion
        packagingOptions {
            resources.excludes.add("META-INF/INDEX.LIST")
        }
        lint {
            isCheckDependencies = true
        }

        defaultConfig {
            minSdk = BuildConfig.minSdkVersion
            targetSdk = BuildConfig.targetSdkVersion
            testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        }
        buildTypes {
            if (isCore) {
                val keysProperties = keyStoreProperties()
                val infuraApiKey = keysProperties.getProperty("apikey.infura")
                val etherscanApiKey = keysProperties.getProperty("apikey.etherscan")
                val polygonscanApiKey = keysProperties.getProperty("apikey.polygonscan")
                val figmentApiKey = keysProperties.getProperty("apikey.figment")
                val bscscanApikey = keysProperties.getProperty("apikey.bscscan")
                val bscrpcApikey = keysProperties.getProperty("apikey.bscrpcscan")
                release {
                    isMinifyEnabled = false
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                    buildConfigField("String", "INFURA_APIKEY", infuraApiKey)
                    buildConfigField("String", "ETHERSCAN_APIKEY", etherscanApiKey)
                    buildConfigField("String", "POLYGONSCAN_APIKEY", polygonscanApiKey)
                    buildConfigField("String", "FIGMENT_APIKEY", figmentApiKey)
                    buildConfigField("String", "BSCRPC_APIKEY", bscrpcApikey)
                    buildConfigField("String", "BSCSCAN_APIKEY", bscscanApikey)
                }
                debug {
                    isMinifyEnabled = false
                    isTestCoverageEnabled = true
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                    buildConfigField("String", "INFURA_APIKEY", infuraApiKey)
                    buildConfigField("String", "ETHERSCAN_APIKEY", etherscanApiKey)
                    buildConfigField("String", "POLYGONSCAN_APIKEY", polygonscanApiKey)
                    buildConfigField("String", "FIGMENT_APIKEY", figmentApiKey)
                    buildConfigField("String", "BSCRPC_APIKEY", bscrpcApikey)
                    buildConfigField("String", "BSCSCAN_APIKEY", bscscanApikey)
                }
            } else {
                release {
                    isMinifyEnabled = true
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
                debug {
                    isMinifyEnabled = false
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                    isTestCoverageEnabled = true
                }
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
}

fun keyStoreProperties(): Properties {
    val properties = Properties()
    val keyProperties = File("./keystore", "configs.properties")

    if (keyProperties.isFile) {
        InputStreamReader(FileInputStream(keyProperties), Charsets.UTF_8).use { reader ->
            properties.load(reader)
        }
    }
    return properties
}