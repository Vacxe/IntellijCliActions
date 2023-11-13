package io.github.vacxe.cliactions.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import androidx.compose.ui.unit.dp
import com.charleskorn.kaml.Yaml
import com.intellij.icons.AllIcons
import com.intellij.ui.components.JBTabbedPane
import io.github.vacxe.cliactions.configurations.ConfigurationProvider
import io.github.vacxe.cliactions.model.Command
import io.github.vacxe.cliactions.model.Config
import io.github.vacxe.cliactions.ui.toolwindow.ToolWindowState
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import javax.swing.*

class CliActionsTablePanel(
    private val configurationFinder: ConfigurationProvider,
    private val runTerminalCommand: (String, String) -> Unit
) : JPanel() {
    private fun updateState(viewState: ToolWindowState) {
        removeAll()
        when (viewState) {
            is ToolWindowState.Content -> {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)

                val jbTabbedPane = JBTabbedPane()

                viewState.groups.forEach { group ->
                    val commandsLayout = JPanel()
                    commandsLayout.border = BorderFactory.createEmptyBorder(5, 5, 5, 5)
                    commandsLayout.layout = BoxLayout(commandsLayout, BoxLayout.Y_AXIS)
                    group.commands.forEach { command ->
                        commandsLayout.add(CmdShortcutItem(command))
                        commandsLayout.add(JSeparator().apply {
                            maximumSize = Dimension(this.maximumSize.width, 5)
                        })
                    }
                    jbTabbedPane.add(group.name, commandsLayout)
                }
                add(jbTabbedPane)
            }

            is ToolWindowState.Error -> {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                add(InformationView(viewState.message))
            }

            is ToolWindowState.Loading -> {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                add(InformationView(viewState.message))
            }
        }
    }

    private fun CmdShortcutItem(iCmdCommand: Command): JComponent {
        val panel = JPanel()
        panel.alignmentX = Component.LEFT_ALIGNMENT
        val button = JButton("Run", AllIcons.Actions.Execute)
        button.addActionListener {
            if (iCmdCommand.prompt) {
                if (JOptionPane.showConfirmDialog(
                        null,
                        "Run: ${iCmdCommand.name} ?", "Confirm Action", JOptionPane.YES_NO_OPTION
                    ) == 0
                ) {
                    runTerminalCommand.invoke(iCmdCommand.name, iCmdCommand.command)
                }
            } else {
                runTerminalCommand.invoke(iCmdCommand.name, iCmdCommand.command)
            }
        }
        panel.add(button)
        val stripLabel = JLabel(iCmdCommand.name)
        panel.add(stripLabel, BorderLayout.WEST)
        panel.maximumSize = panel.preferredSize
        return panel
    }

    private fun InformationView(message: String): JComponent {
        val panel = JPanel()
        panel.alignmentX = Component.CENTER_ALIGNMENT
        panel.alignmentY = Component.CENTER_ALIGNMENT
        val stripLabel = JLabel(message)
        panel.add(stripLabel)
        return panel
    }

    fun dispose() {}
    fun initialise() {
        updateConfigurations()
    }

    private fun updateConfigurations() {
        updateState(ToolWindowState.Loading("Searching for configurations..."))
        configurationFinder.find { configFiles ->
            if (configFiles.toList().isNotEmpty()) {
                val groups = configFiles.map { file ->
                    Yaml.default.decodeFromString(
                        Config.serializer(),
                        file.readText()
                    ).groups
                }.flatten()
                    .toList()
                updateState(ToolWindowState.Content(groups))
            } else {
                updateState(ToolWindowState.Error("Can't find any config files. Please define `<name>.cliactions.yaml` in the project root directory"))
            }
        }
    }
}
