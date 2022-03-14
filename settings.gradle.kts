pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven(uri("https://maven.pkg.github.com/trustwallet/wallet-core")) {
            credentials {
                username = userProperty().getProperty("gpr.name")
                password = userProperty().getProperty("gpr.key")
            }
        }
    }
}

fun userProperty(): java.util.Properties {
    val properties = java.util.Properties()
    val localProperties = File(rootDir, "local.properties")

    if (localProperties.isFile) {
        java.io.InputStreamReader(
            java.io.FileInputStream(localProperties)
        ).use { reader ->
            properties.load(reader)
        }
    }
    return properties
}

rootProject.name = "EasyWallet"
includeBuild("dep-version")
include(":app")
include(":core")
include(":core-ui")
include(":dapp")
include(":intro")
include(":settings")
include(":assets:domain")
include(":assets:data")
include(":assets:presentation")
