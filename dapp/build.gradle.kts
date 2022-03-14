import com.easy.version.androidTestDependencies
import com.easy.version.composeUI
import com.easy.version.hiltDependencies
import com.easy.version.unitTestDependencies

dependencies {

    implementation(project(":core"))
    implementation(project(":core-ui"))
    hiltDependencies()
    composeUI()

    unitTestDependencies()
    androidTestDependencies()
}