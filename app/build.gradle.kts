import com.easy.version.*
import com.easy.version.dependencies.Compose

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.devtools.ksp") version "1.6.10-1.0.2"
    id("dagger.hilt.android.plugin")
}
apply<MergeManifestPlugin>()
android {
    compileSdk = BuildConfig.compileSdkVersion

    defaultConfig {
        applicationId = "com.easy.wallet.c"
        minSdk = BuildConfig.minSdkVersion
        targetSdk = BuildConfig.targetSdkVersion
        versionCode = BuildConfig.versionCode
        versionName = BuildConfig.versionName

        // testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
//        vectorDrawables {
//            useSupportLibrary true
//        }
    }

    val keyProperties = keyStoreProperties()
    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file(keyProperties.getProperty("storeFile"))
            storePassword = keyProperties.getProperty("storePassword")
            keyAlias = keyProperties.getProperty("keyAlias")
            keyPassword = keyProperties.getProperty("keyPassword")
        }
        create("release") {
            storeFile = rootProject.file(keyProperties.getProperty("storeFile"))
            storePassword = keyProperties.getProperty("storePassword")
            keyAlias = keyProperties.getProperty("keyAlias")
            keyPassword = keyProperties.getProperty("keyPassword")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = false
        }
        debug {
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Compose.version
    }
    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

kotlin {
    sourceSets {
        debug {
            kotlin.srcDir("build/generated/ksp/debug/kotlin")
        }
        release {
            kotlin.srcDir("build/generated/ksp/release/kotlin")
        }
    }
}

dependencies {
    implementation(com.easy.version.dependencies.AndroidX.coreKtx)
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation(project(":core"))
    implementation(project(":core-ui"))
    implementation(project(":wallets"))
    implementation(project(":intro"))
    implementation(project(":dapp:dapp_domain"))
    implementation(project(":dapp:dapp_presentation"))
    implementation(project(":assets:domain"))
    implementation(project(":assets:data"))
    implementation(project(":assets:presentation"))
    implementation(project(":settings:settings_domain"))
    implementation(project(":settings:settings_presentation"))

    implementation("com.google.guava:guava:30.1.1-android")

    implementation(com.easy.version.dependencies.Other.material)

    composeUI()

    implementation(com.easy.version.dependencies.AndroidX.Lifecycle.runtime)
    implementation("androidx.activity:activity-compose:1.4.0")

    // hilt
    hiltDependencies()
    roomDependencies()

    unitTestDependencies()
    androidTestDependencies()
}

//tasks.register("gitVersion", GitVersionTask::class.java) {
//    println(project.name)
//    this.gitVersionOutputFile.set(File(project.buildDir,"intermediates/gitVersionProvider/output"))
//    this.outputs.upToDateWhen { false }
//}