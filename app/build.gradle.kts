import com.easy.version.*
import com.easy.version.dependencies.Compose

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("org.jmailen.kotlinter")
}
apply<MergeManifestPlugin>()
android {
    compileSdk = BuildConfig.compileSdkVersion

    defaultConfig {
        applicationId = "com.easy.wallet"
        minSdk = BuildConfig.minSdkVersion
        targetSdk = BuildConfig.targetSdkVersion
        versionCode = BuildConfig.versionCode
        versionName = BuildConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

kotlinter {
    disabledRules = arrayOf("no-wildcard-imports","filename")
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
    implementation(com.easy.version.dependencies.Special.web3j_abi)

    composeUI()

    implementation(com.easy.version.dependencies.AndroidX.Lifecycle.runtime)
    implementation("androidx.activity:activity-compose:1.4.0")

    // hilt
    hiltDependencies()
    roomDependencies()

    unitTestDependencies()
    androidTestDependencies()
}

tasks.register("installGitHook", Copy::class.java) {
    from(File(rootProject.rootDir, "scripts/pre-commit"))
    into(File(rootProject.rootDir, ".git/hooks"))
}

tasks.getByPath(":app:preBuild").dependsOn("installGitHook")

//tasks.register("gitVersion", GitVersionTask::class.java) {
//    println(project.name)
//    this.gitVersionOutputFile.set(File(project.buildDir,"intermediates/gitVersionProvider/output"))
//    this.outputs.upToDateWhen { false }
//}