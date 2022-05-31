import com.easy.version.*

kotlinter {
    disabledRules = arrayOf("no-wildcard-imports", "filename")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":core-ui"))

    composeUI()
    hiltDependencies()
    unitTestDependencies()
    androidTestDependencies()
}