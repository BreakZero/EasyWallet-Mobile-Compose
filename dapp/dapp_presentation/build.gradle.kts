import com.easy.version.androidTestDependencies
import com.easy.version.composeUI
import com.easy.version.hiltDependencies
import com.easy.version.unitTestDependencies

dependencies {
  implementation(project(":core"))
  implementation(project(":core-ui"))
  implementation(project(":dapp:dapp_domain"))

  composeUI()
  hiltDependencies()
  unitTestDependencies()
  androidTestDependencies()
}