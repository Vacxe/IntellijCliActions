package io.github.vacxe.cliactions.configurations

import com.intellij.openapi.project.Project
import org.apache.commons.io.FileUtils
import java.io.File
import java.lang.Thread.sleep
import kotlin.concurrent.thread

class CliActionsConfigurationProvider(private val project: Project) : ConfigurationProvider{
    override fun find(result: (Sequence<File>) -> Unit) {
        thread(start = true) {
            val projectConfigs = File(project.basePath ?: throw Exception("Project basePath cannot be found"))
                .walk()
                .filter { it.name.endsWith(ConfigurationFileExtension) }

            FileUtils.getUserDirectory()

            val userConfigs = FileUtils.getUserDirectory()
                .walk()
                .maxDepth(1)
                .filter { it.name.endsWith(ConfigurationFileExtension) }

            result.invoke(projectConfigs + userConfigs)
        }
    }
}