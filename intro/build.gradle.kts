import com.easy.version.*

dependencies {
    implementation(project(":core"))
    implementation(project(":core-ui"))

    implementation(com.easy.version.dependencies.Other.sqlcipher)

    roomDependencies()
    composeUI()
    hiltDependencies()
    unitTestDependencies()
    androidTestDependencies()
}