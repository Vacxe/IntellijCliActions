package io.github.vacxe.cliactions

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import io.github.vacxe.cliactions.configurations.CliActionsConfigurationProvider
import io.github.vacxe.cliactions.terminal.IntellijIDETerminal
import io.github.vacxe.cliactions.ui.CliActionsTablePanelCompose
import org.jetbrains.jewel.bridge.JewelComposePanel
import org.jetbrains.jewel.foundation.enableNewSwingCompositing

class CliActionsToolWindowFactory : DumbAware, ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val terminalProvider = IntellijIDETerminal(project)
        val configurationFinder = CliActionsConfigurationProvider(project)
        enableNewSwingCompositing()

        val tabContent = toolWindow.contentManager.factory.createContent(
            /* component = */
            JewelComposePanel {
                CliActionsTablePanelCompose(
                    configurationFinder = configurationFinder,
                    runTerminalCommand = { name, command -> terminalProvider.run(name, command) }
                )
            },
            /* displayName = */ null,
            /* isLockable = */ false,
        )
        tabContent.isCloseable = false
        toolWindow.contentManager.addContent(tabContent)
    }
}
