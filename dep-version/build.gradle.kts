/*buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.6.10"))
    }
}*/
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
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
}

java.sourceCompatibility = JavaVersion.VERSION_11

gradlePlugin {
    plugins {
        create("VersionPlugin") {
            id = "version-plugin"
            implementationClass = "com.easy.version.DependencyVersionPlugin"
        }
    }
}