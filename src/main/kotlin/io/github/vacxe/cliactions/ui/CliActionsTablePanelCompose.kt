package io.github.vacxe.cliactions.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.charleskorn.kaml.Yaml
import com.intellij.icons.AllIcons
import io.github.vacxe.cliactions.configurations.ConfigurationProvider
import io.github.vacxe.cliactions.model.Command
import io.github.vacxe.cliactions.model.Config
import io.github.vacxe.cliactions.ui.toolwindow.ToolWindowState
import org.jetbrains.jewel.ui.Orientation
import org.jetbrains.jewel.ui.component.Divider
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.OutlinedButton
import org.jetbrains.jewel.ui.component.TabData
import org.jetbrains.jewel.ui.component.TabStrip
import org.jetbrains.jewel.ui.component.Text
import java.io.File
import javax.swing.JOptionPane

@Composable
fun CliActionsTablePanelCompose(
    configurationFinder: ConfigurationProvider,
    runTerminalCommand: (String, String) -> Unit
) {
    var toolWindowState: ToolWindowState by remember { mutableStateOf(ToolWindowState.Loading("Searching for configurations...")) }

    PanelContent(toolWindowState, runTerminalCommand)

    DisposableEffect(Unit) {
        configurationFinder.subscribe { toolWindowState = configsUpdate(it) }

        // When the effect leaves the Composition, remove the observer
        onDispose {
            configurationFinder.unsubscribe()
        }
    }
}

private fun configsUpdate(configFiles: Sequence<File>): ToolWindowState {
    return if (configFiles.toList().isNotEmpty()) {
        try {
            val groups = configFiles.map { file ->
                Yaml.default.decodeFromString(
                    Config.serializer(), file.readText()
                ).groups
            }.flatten().toList()
            ToolWindowState.Content(groups)
        } catch (e: com.charleskorn.kaml.UnknownPropertyException) {
            ToolWindowState.Error("Unable to parse configuration ${e.message}")
        }
    } else {
        ToolWindowState.Error("Can't find any config files. Please define `<name>.cliactions.yaml` in the project root directory")
    }
}


@Composable
fun PanelContent(viewState: ToolWindowState, runTerminalCommand: (String, String) -> Unit) {
    when (viewState) {
        is ToolWindowState.Content -> {
            Tabs(viewState, runTerminalCommand)
        }

        is ToolWindowState.Error -> {
            Row {
                InformationView(viewState.message)
            }
        }

        is ToolWindowState.Loading -> {
            Row {
                InformationView(viewState.message)
            }
        }
    }
}

@Composable
private fun Tabs(
    viewState: ToolWindowState.Content,
    runTerminalCommand: (String, String) -> Unit
) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    val tabs = remember(viewState.groups, selectedTabIndex) {
        viewState.groups.mapIndexed { index, group ->
            TabData.Default(
                selected = index == selectedTabIndex,
                label = group.name,
                closable = false,
                onClick = { selectedTabIndex = index },
            )
        }
    }
    Column(
        Modifier.fillMaxWidth()
    ) {
        TabStrip(tabs)
        viewState.groups[selectedTabIndex].commands.forEach { command ->
            Column {
                CmdShortcutItem(command, runTerminalCommand)
                Divider(Orientation.Horizontal)
            }
        }
    }
}

@Composable
fun CmdShortcutItem(iCmdCommand: Command, runTerminalCommand: (String, String) -> Unit) {
    Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
        OutlinedButton(
            onClick = { runCommand(iCmdCommand, runTerminalCommand) },
            Modifier.requiredWidth(IntrinsicSize.Min)
        ) {
            Icon("actions/execute_stroke.svg", null, AllIcons::class.java, Modifier.padding(end = 8.dp))
            Text("Run")
        }
        Spacer(Modifier.width(12.dp))
        Text(iCmdCommand.name, maxLines = 3)
    }
}

private fun runCommand(iCmdCommand: Command, runTerminalCommand: (String, String) -> Unit) {
    if (iCmdCommand.prompt) {
        if (JOptionPane.showConfirmDialog(
                null, "Run: ${iCmdCommand.name} ?", "Confirm Action", JOptionPane.YES_NO_OPTION
            ) == 0
        ) {
            runTerminalCommand.invoke(iCmdCommand.name, iCmdCommand.command)
        }
    } else {
        runTerminalCommand.invoke(iCmdCommand.name, iCmdCommand.command)
    }
}

@Composable
fun InformationView(message: String) {
    Text(message, Modifier.padding(12.dp))
}
