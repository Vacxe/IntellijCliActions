package io.github.vacxe.icmd.ui

import com.charleskorn.kaml.Yaml
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBTabbedPane
import io.github.vacxe.icmd.model.ICmdCommand
import io.github.vacxe.icmd.model.ICmdConfig
import org.jetbrains.plugins.terminal.TerminalView
import java.awt.BorderLayout
import java.awt.Component
import java.io.File
import java.io.IOException
import javax.swing.*


class ICmdTablePanel(project: Project) : JPanel() {
    private val project: Project

    init {
        this.project = project
    }

    private fun build() {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        // TODO : Add reload config button

        val configFiles = File(project.basePath ?: throw Exception("Project basePath cannot be found"))
            .walk()
            .filter { it.name.endsWith(".icmd.yaml") }

        if (configFiles.toList().isNotEmpty()) {
            val jbTabbedPane = JBTabbedPane()
            configFiles.map { file -> Yaml.default.decodeFromString(ICmdConfig.serializer(), file.readText()).groups }
                .forEach { groups ->
                    groups.forEach { group ->
                        val commandsLayout = JPanel()
                        commandsLayout.layout = BoxLayout(commandsLayout, BoxLayout.Y_AXIS);

                        group.commands.forEach { command ->
                            commandsLayout.add(addCmdShortcutItem(command))
                        }
                        jbTabbedPane.add(group.name, commandsLayout)
                    }
                }

            add(jbTabbedPane)
        } else {
            //TODO : Add hint with configuration requirements
        }
    }
    private fun addCmdShortcutItem(iCmdCommand: ICmdCommand): JComponent {
        val panel = JPanel()
        panel.alignmentX = Component.LEFT_ALIGNMENT

        val button = JButton("Run", ImageIcon("images/start.gif"))
        button.addActionListener {
            if(iCmdCommand.prompt) {
                //TODO : Add prompt
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

    fun dispose() {}
    fun initialise() {
        build()
    }

    private fun runCommand(name: String, command: String) {
        val terminalView: TerminalView = TerminalView.getInstance(project)
        val command = command
        try {
            terminalView.createLocalShellWidget(project.basePath, name).executeCommand(command)
        } catch (err: IOException) {
            err.printStackTrace()
        }
    }
}