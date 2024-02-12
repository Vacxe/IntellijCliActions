package io.github.vacxe.cliactions.configurations

import com.intellij.openapi.project.Project
import org.apache.commons.io.FileUtils
import java.io.File
import kotlin.concurrent.thread

class CliActionsConfigurationProvider(private val project: Project) : ConfigurationProvider {
    private var subscription: ((Sequence<File>) -> Unit)? = null
    private var configurations = listOf<File>()

    private var findConfigsThread: Thread? = null

    private fun findConfigs() = thread(start = true) {
        val projectConfigs = File(project.basePath ?: throw Exception("Project basePath cannot be found"))
            .walk()
            .maxDepth(2)
            .filter { it.name.endsWith(ConfigurationFileExtension) }

        val userConfigs = FileUtils.getUserDirectory()
            .walk()
            .maxDepth(1)
            .filter { it.name.endsWith(ConfigurationFileExtension) }

        if (configurations.toSet() != projectConfigs + userConfigs) {
            configurations = (projectConfigs + userConfigs).toList()
            subscription?.invoke(projectConfigs + userConfigs)
        }
    }

    override fun subscribe(updateSubscription: (Sequence<File>) -> Unit) {
        subscription = updateSubscription
        findConfigsThread = findConfigs()
    }

    override fun unsubscribe() {
        subscription = null
        findConfigsThread?.interrupt()
    }
}
