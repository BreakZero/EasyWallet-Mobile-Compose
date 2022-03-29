package com.easy.version

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

class MergeManifestPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val gitVersionTask = target.tasks.register("GitVersion", GitVersionTask::class.java) {
            this.gitVersionOutputFile.set(
                File(
                    project.buildDir,
                    "intermediates/gitVersionProvider/output"
                )
            )
            this.outputs.upToDateWhen { false }
        }
        val manifestUpdater =
            target.tasks.register("ManifestUpdater", ManifestTransformerTask::class.java) {
                this.gitInfoFile.set(gitVersionTask.flatMap(GitVersionTask::gitVersionOutputFile))
            }
        val androidComponents =
            target.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants {
            it.artifacts.use(manifestUpdater)
                .wiredWithFiles(
                    ManifestTransformerTask::mergedManifest,
                    ManifestTransformerTask::updatedManifest
                ).toTransform(SingleArtifact.MERGED_MANIFEST)
        }
    }
}