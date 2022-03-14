import com.easy.version.androidTestDependencies
import com.easy.version.composeUI
import com.easy.version.unitTestDependencies

dependencies {
    implementation("com.google.zxing:core:3.4.1")

    // CameraX
    implementation("androidx.camera:camera-camera2:1.1.0-beta02")
    implementation("androidx.camera:camera-lifecycle:1.1.0-beta02")
    implementation("androidx.camera:camera-view:1.1.0-beta02")

    composeUI()
    unitTestDependencies()
    androidTestDependencies()
}