plugins {
    id("com.android.application") version "7.1.2" apply false
    id("com.android.library") version "7.1.2" apply false
    id("version-plugin") apply false
    id("org.jetbrains.kotlin.android") version "1.6.21" apply false
    kotlin("plugin.serialization") version "1.6.21" apply false
}

buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.38.1")
    }
}

subprojects {
    this.apply(from = "${rootProject.rootDir}/lint.gradle.kts")
    this.apply(plugin = "version-plugin")
    tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java) {
        kotlinOptions {
            freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
            jvmTarget = JavaVersion.VERSION_11.toString()
        }
    }
}

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}
