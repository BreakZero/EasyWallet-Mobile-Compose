package com.easy.version

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class ManifestTransformerTask : DefaultTask() {
    @get:InputFile
    abstract val gitInfoFile: RegularFileProperty
    @get:InputFile
    abstract val mergedManifest: RegularFileProperty

    @get:OutputFile
    abstract val updatedManifest: RegularFileProperty

    @TaskAction
    fun mergedTask() {
        val gitVersion = gitInfoFile.get().asFile.readText()
        var manifest = mergedManifest.get().asFile.readText()
        manifest = manifest.replace(
            "android:versionName=\"v1.0.0\"",
            "android:versionName=\"$gitVersion\""
        )
        updatedManifest.get().asFile.writeText(manifest)
    }
}