import com.easy.version.androidTestDependencies
import com.easy.version.composeUI
import com.easy.version.unitTestDependencies

dependencies {
    implementation(com.easy.version.dependencies.Other.zxing)

    // CameraX
    implementation(com.easy.version.dependencies.AndroidX.Camera.camera)
    implementation(com.easy.version.dependencies.AndroidX.Camera.cameraLife)
    implementation(com.easy.version.dependencies.AndroidX.Camera.cameraView)

    composeUI()
    unitTestDependencies()
    androidTestDependencies()
}