import com.easy.version.androidTestDependencies
import com.easy.version.hiltDependencies
import com.easy.version.unitTestDependencies

dependencies {
  implementation(project(":core"))

  implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0")

  hiltDependencies()
  unitTestDependencies()
  androidTestDependencies()
}