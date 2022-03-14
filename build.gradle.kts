
plugins {
    id("com.android.application") version "7.1.2" apply false
    id("com.android.library") version "7.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.6.10" apply false
    kotlin("plugin.serialization") version "1.6.10" apply false
    id("version-plugin")
}

buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.38.1")
    }
}

subprojects {
    this.apply(plugin = "version-plugin")
}

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}