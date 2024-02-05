package io.github.vacxe.cliactions.terminal

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager
import org.jetbrains.plugins.terminal.ShellTerminalWidget
import org.jetbrains.plugins.terminal.TerminalToolWindowFactory
import org.jetbrains.plugins.terminal.TerminalView
import java.io.IOException

class IntellijIDETerminal(private val project: Project) : Terminal {
    override fun run(name: String, command: String) {
        val terminalView: TerminalView = TerminalView.getInstance(project)
        val window = ToolWindowManager.getInstance(project).getToolWindow(TerminalToolWindowFactory.TOOL_WINDOW_ID)
        val contentManager = window?.contentManager

        try {
            val widget = when (val content = contentManager?.findContent(name)) {
                null -> terminalView.createLocalShellWidget(project.basePath, name)
                else -> TerminalView.getWidgetByContent(content) as ShellTerminalWidget
            }

            widget.executeCommand(command)
        } catch (err: IOException) {
            err.printStackTrace()
        }
    }
}