import com.easy.version.androidTestDependencies
import com.easy.version.hiltDependencies
import com.easy.version.roomDependencies
import com.easy.version.unitTestDependencies

dependencies {
    implementation(project(":core"))

    hiltDependencies()
    unitTestDependencies()
    androidTestDependencies()
}