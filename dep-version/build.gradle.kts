plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    compileOnly("com.android.tools.build:gradle:7.0.4")
    compileOnly("com.android.tools.build:gradle-api:7.0.4")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.20")
}

java.sourceCompatibility = JavaVersion.VERSION_11
tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java) {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

gradlePlugin {
    plugins {
        create("VersionPlugin") {
            id = "version-plugin"
            implementationClass = "com.easy.version.DependencyVersionPlugin"
        }
    }
}