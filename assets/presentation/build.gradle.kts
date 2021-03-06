import com.easy.version.androidTestDependencies
import com.easy.version.composeUI
import com.easy.version.hiltDependencies
import com.easy.version.unitTestDependencies

dependencies {
    implementation(project(":core"))
    implementation(project(":core-ui"))
    implementation(project(":assets:domain"))
    implementation(com.easy.version.dependencies.AndroidX.Paging.runtime)

    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0")

    composeUI()
    hiltDependencies()
    unitTestDependencies()
    androidTestDependencies()
}