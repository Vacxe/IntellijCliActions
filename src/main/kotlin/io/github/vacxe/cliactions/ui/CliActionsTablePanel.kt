package io.github.vacxe.cliactions.ui

import com.charleskorn.kaml.Yaml
import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBTabbedPane
import io.github.vacxe.cliactions.model.Command
import io.github.vacxe.cliactions.model.Config
import org.jetbrains.plugins.terminal.TerminalView
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import java.io.File
import java.io.IOException
import javax.swing.*

class CliActionsTablePanel(project: Project) : JPanel() {
    private val project: Project

    init {
        this.project = project
    }

    private fun build() {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        val configFiles = File(project.basePath ?: throw Exception("Project basePath cannot be found"))
            .walk()
            .filter { it.name.endsWith(".cliactions.yaml") }

        if (configFiles.toList().isNotEmpty()) {
            val jbTabbedPane = JBTabbedPane()
            configFiles.map { file -> Yaml.default.decodeFromString(Config.serializer(), file.readText()).groups }
                .forEach { groups ->
                    groups.forEach { group ->
                        val commandsLayout = JPanel()
                        commandsLayout.border = BorderFactory.createEmptyBorder(5,5,5,5)
                        commandsLayout.layout = BoxLayout(commandsLayout, BoxLayout.Y_AXIS);
                        group.commands.forEach { command ->
                            commandsLayout.add(addCmdShortcutItem(command))
                            commandsLayout.add(JSeparator().apply {
                                maximumSize = Dimension(this.maximumSize.width,5)
                            })
                        }
                        jbTabbedPane.add(group.name, commandsLayout)
                    }
                }

            add(jbTabbedPane)
        } else {
            add(addNoConfigFilesMessage())
        }
    }

    private fun addCmdShortcutItem(iCmdCommand: Command): JComponent {
        val panel = JPanel()
        panel.alignmentX = Component.LEFT_ALIGNMENT
        val button = JButton("Run", AllIcons.Actions.Execute)
        button.addActionListener {
            if(iCmdCommand.prompt) {
                if(JOptionPane.showConfirmDialog(null,
                    "Run: ${iCmdCommand.name} ?", "Confirm Action", JOptionPane.YES_NO_OPTION) == 0)
                {
                    runCommand(iCmdCommand.name, iCmdCommand.command)
                }
            } else {
                runCommand(iCmdCommand.name, iCmdCommand.command)
            }
        }
        panel.add(button)
        val stripLabel = JLabel(iCmdCommand.name)
        panel.add(stripLabel, BorderLayout.WEST)
        panel.maximumSize = panel.preferredSize;
        return panel
    }

    private fun addNoConfigFilesMessage(): JComponent {
        val panel = JPanel()
        panel.alignmentX = Component.CENTER_ALIGNMENT
        panel.alignmentY = Component.CENTER_ALIGNMENT
        val stripLabel = JLabel("Can't find any config files. Please define `<name>.cliactions.yaml` in the project root directory")
        panel.add(stripLabel)
        return panel
    }
    fun dispose() {}
    fun initialise() {
        build()
    }

    private fun runCommand(name: String, command: String) {
        val terminalView: TerminalView = TerminalView.getInstance(project)
        try {
            terminalView.createLocalShellWidget(project.basePath, name).executeCommand(command)
        } catch (err: IOException) {
            err.printStackTrace()
        }
    }
}
