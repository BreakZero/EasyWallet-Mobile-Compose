package com.easy.version

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.LibraryVariant
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport
import java.math.BigDecimal
import java.util.*

private val config_excludes = setOf(
    "**/R.class",
    "**/R$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*Test*.*",
    "android/**/*.*",
    // Kotlin
    "**/*MapperImpl*.*",
    "**/BuildConfig.*",
    "**/*Component*.*",
    "**/*BR*.*",
    "**/Manifest*.*",
    "**/*Companion*.*",
    "**/*Module*.*",
    "**/*Dagger*.*",
    "**/*Hilt*.*",
    "**/*MembersInjector*.*",
    "**/*_MembersInjector.class",
    "**/*_Factory*.*",
    "**/*_Provide*Factory*.*",
    "**/*Extensions*.*"
)
class UnitTestCoverageTask : Plugin<Project> {
    override fun apply(target: Project) {
        target.extensions.getByType(LibraryExtension::class).run {
            this.libraryVariants.all {
                createVariantCoverage(target, this)
            }
        }
    }
    private fun createVariantCoverage(project: Project, variant: LibraryVariant) {
        val variantName = variant.name
        val testTaskName = "test${variantName.capitalize(Locale.getDefault())}UnitTest"

        project.tasks.register("${testTaskName}Coverage", JacocoReport::class.java) {
            dependsOn(testTaskName, "createDebugUnitTestCoverageReport")
            group = "Report"
            description = "Generate Jacoco coverage reports for the ${variantName.capitalize()} build."
            reports {
                html.required.set(true)
                csv.required.set(true)
            }
            with(project) {
                val baseDir = "${buildDir}/tmp/kotlin-classes/${variantName}"
                val javaClasses = fileTree(variant.javaCompileProvider.get().destinationDirectory) {
                    exclude(config_excludes)
                }
                val kotlinClasses = fileTree(baseDir) {
                    exclude(config_excludes)
                }
                classDirectories.setFrom(files(javaClasses, kotlinClasses))
                sourceDirectories.setFrom(
                    files(
                        "${project.projectDir}/src/main/java",
                        "${project.projectDir}/src/${variantName}/java",
                        "${project.projectDir}/src/main/kotlin",
                        "${project.projectDir}/src/${variantName}/kotlin"
                    )
                )
                val path =
                    "${project.buildDir}/outputs/unit_test_code_coverage/${variantName}UnitTest/${testTaskName}.exec"
                executionData.setFrom(path)
            }
        }

        project.tasks.register(
            "${testTaskName}CoverageVerification",
            JacocoCoverageVerification::class.java
        ) {
            dependsOn("${testTaskName}Coverage")
            group = "verification"
            description = "Verifies Jacoco coverage for the ${variantName.capitalize()} build."
            violationRules {
                rule {
                    limit {
                        minimum = BigDecimal.ZERO
                    }
                }
                rule {
                    element = "BUNDLE"
                    limit {
                        counter = "LINE"
                        value = "COVEREDRATIO"
                        minimum = "0.3".toBigDecimal()
                    }
                }
            }
            with(project) {
                val javaClasses = fileTree(variant.javaCompileProvider.get().destinationDirectory) {
                    exclude(config_excludes)
                }
                val kotlinClasses = fileTree("${buildDir}/tmp/kotlin-classes/${variantName}") {
                    exclude(config_excludes)
                }
                classDirectories.setFrom(files(javaClasses, kotlinClasses))
                sourceDirectories.setFrom(
                    files(
                        "${project.projectDir}/src/main/java",
                        "${project.projectDir}/src/${variantName}/java",
                        "${project.projectDir}/src/main/kotlin",
                        "${project.projectDir}/src/${variantName}/kotlin"
                    )
                )
                executionData.setFrom(
                    files(
                        "${project.buildDir}/outputs/unit_test_code_coverage/${variantName}UnitTest/${testTaskName}.exec"
                    )
                )
            }
        }
    }
}
