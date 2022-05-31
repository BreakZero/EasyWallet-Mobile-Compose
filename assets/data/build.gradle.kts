import com.easy.version.androidTestDependencies
import com.easy.version.hiltDependencies
import com.easy.version.roomDependencies
import com.easy.version.unitTestDependencies

kotlinter {
    disabledRules = arrayOf("no-wildcard-imports", "filename")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":assets:domain"))
    implementation(project(":wallets"))

    roomDependencies()
    hiltDependencies()
    unitTestDependencies()
    androidTestDependencies()
}