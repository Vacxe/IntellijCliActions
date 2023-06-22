package io.github.vacxe.icmd.ui

import com.charleskorn.kaml.Yaml
import com.intellij.openapi.project.Project
import io.github.vacxe.icmd.model.ICmdShortcut
import io.github.vacxe.icmd.model.ICmdConfig
import org.jetbrains.plugins.terminal.TerminalView
import java.awt.BorderLayout
import java.awt.Component
import java.io.IOException
import java.nio.file.Path
import javax.swing.*


class ICmdTablePanel(project: Project) : JPanel() {
    private val project: Project

    init {
        this.project = project
    }

    private fun build() {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)

        val config = "${project.basePath}/icmd.yaml"
        val commands = Yaml.default.decodeFromString(ICmdConfig.serializer(), Path.of(config).toFile().readText()).commands

        commands.forEach {
            add(createShortcut(it))
        }
    }

    private fun createShortcut(cmdShortcut: ICmdShortcut): JComponent {
        val panel = JPanel()
        panel.setAlignmentX( Component.LEFT_ALIGNMENT )

        val button = JButton("Run", ImageIcon("images/start.gif"))
        button.addActionListener {
            val terminalView: TerminalView = TerminalView.getInstance(project)
            val command = cmdShortcut.command
            try {
                terminalView.createLocalShellWidget(project.basePath, cmdShortcut.name).executeCommand(command)
            } catch (err: IOException) {
                err.printStackTrace()
            }
        }
        panel.add(button)
        val stripLabel = JLabel(cmdShortcut.name)
        panel.add(stripLabel, BorderLayout.WEST)

        panel.maximumSize = panel.preferredSize;
        return panel
    }

    fun dispose() {}
    fun initialise() {
        build()
    }
}
