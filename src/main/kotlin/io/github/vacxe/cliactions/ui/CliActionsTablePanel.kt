package io.github.vacxe.cliactions.ui

import com.charleskorn.kaml.Yaml
import com.intellij.icons.AllIcons
import com.intellij.ui.components.JBTabbedPane
import io.github.vacxe.cliactions.configurations.ConfigurationProvider
import io.github.vacxe.cliactions.model.Command
import io.github.vacxe.cliactions.model.Config
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import javax.swing.*

class CliActionsTablePanel(
    private val configurationFinder: ConfigurationProvider,
    private val runTerminalCommand: (String, String) -> Unit
) : JPanel() {
    private val loadingMessage = loadingMessage()
    private val noConfigFilesMessage = noConfigFilesMessage()

    private fun build() {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        add(loadingMessage)
        configurationFinder.find { configFiles ->
            remove(loadingMessage)
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
                add(noConfigFilesMessage)
            }
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
                    runTerminalCommand.invoke(iCmdCommand.name, iCmdCommand.command)
                }
            } else {
                runTerminalCommand.invoke(iCmdCommand.name, iCmdCommand.command)
            }
        }
        panel.add(button)
        val stripLabel = JLabel(iCmdCommand.name)
        panel.add(stripLabel, BorderLayout.WEST)
        panel.maximumSize = panel.preferredSize;
        return panel
    }

    private fun noConfigFilesMessage(): JComponent {
        val panel = JPanel()
        panel.alignmentX = Component.CENTER_ALIGNMENT
        panel.alignmentY = Component.CENTER_ALIGNMENT
        val stripLabel = JLabel("Can't find any config files. Please define `<name>.cliactions.yaml` in the project root directory")
        panel.add(stripLabel)
        return panel
    }

    private fun loadingMessage(): JComponent {
        val panel = JPanel()
        panel.alignmentX = Component.CENTER_ALIGNMENT
        panel.alignmentY = Component.CENTER_ALIGNMENT
        val stripLabel = JLabel("Searching for configurations...")
        panel.add(stripLabel)
        return panel
    }

    fun dispose() {}
    fun initialise() {
        build()
    }
}
