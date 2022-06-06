import com.easy.version.*

dependencies {
    implementation(project(":core"))
    implementation(project(":core-ui"))

    composeUI()
    hiltDependencies()
    unitTestDependencies()
    androidTestDependencies()
}