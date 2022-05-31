import com.easy.version.androidTestDependencies
import com.easy.version.hiltDependencies
import com.easy.version.unitTestDependencies

kotlinter {
    disabledRules = arrayOf("no-wildcard-imports", "filename")
}

dependencies {
    implementation(project(":core"))

    hiltDependencies()
    unitTestDependencies()
    androidTestDependencies()
}