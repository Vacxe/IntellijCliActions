package io.github.vacxe.cliactions.terminal

import com.intellij.openapi.project.Project
import org.jetbrains.plugins.terminal.TerminalView
import java.io.IOException

class IntellijIDETerminal(private val project: Project) : Terminal {
    override fun run(name: String, command: String) {
        val terminalView: TerminalView = TerminalView.getInstance(project)
        try {
            terminalView.createLocalShellWidget(project.basePath, name).executeCommand(command)
        } catch (err: IOException) {
            err.printStackTrace()
        }
    }
}