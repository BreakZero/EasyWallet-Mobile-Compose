import com.easy.version.*

kotlinter {
    disabledRules = arrayOf("no-wildcard-imports", "filename")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":core-ui"))
    implementation(project(":wallets"))

    implementation(com.easy.version.dependencies.Other.sqlcipher)

    roomDependencies()
    composeUI()
    hiltDependencies()
    unitTestDependencies()
    androidTestDependencies()
}