import com.easy.version.androidTestDependencies
import com.easy.version.composeUI
import com.easy.version.hiltDependencies
import com.easy.version.unitTestDependencies

kotlinter {
  disabledRules = arrayOf("no-wildcard-imports", "filename")
}

dependencies {
  implementation(project(":core"))
  implementation(project(":core-ui"))
  implementation(project(":dapp:dapp_domain"))
  implementation(project(":assets:domain"))

  composeUI()
  hiltDependencies()
  unitTestDependencies()
  androidTestDependencies()
}