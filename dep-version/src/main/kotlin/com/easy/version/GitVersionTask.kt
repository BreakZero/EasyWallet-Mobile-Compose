package com.easy.version

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class GitVersionTask : DefaultTask() {

    @get:OutputFile
    abstract val gitVersionOutputFile: RegularFileProperty

    @TaskAction
    fun getGitVersion() {
        val process = ProcessBuilder(
            "git",
            "branch",
            "--show"
        ).start()
        val error = process.errorStream.readBytes().decodeToString()
        if (error.isNotBlank()) {
            println("Git error: $error")
        }
        val gitVersion = process.inputStream.readBytes().decodeToString()
        println("Git Version: $gitVersion")
        gitVersionOutputFile.get().asFile.writeText("v1.1.2")
    }
}