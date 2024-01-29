package io.github.vacxe.cliactions.configurations

import com.intellij.openapi.project.Project
import org.apache.commons.io.FileUtils
import java.io.File
import java.lang.Thread.sleep
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class CliActionsConfigurationProvider(private val project: Project) : ConfigurationProvider {
    private var subscription: ((Sequence<File>) -> Unit)? = null
    private var configurations = listOf<File>()

    private var updateThread: Thread? = null

    private fun newThread() = thread {
            while (true) {
                findConfigs()
                try {
                    sleep(TimeUnit.SECONDS.toMillis(10))
                } catch (e: InterruptedException) {
                    // Ignore
                }
            }
        }

    private fun findConfigs() {
        thread(start = true) {
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
    }

    override fun subscribe(updateSubscription: (Sequence<File>) -> Unit) {
        subscription = updateSubscription
        updateThread = newThread()
    }

    override fun unsubscribe() {
        subscription = null
        updateThread?.interrupt()
        updateThread = null
    }
}
