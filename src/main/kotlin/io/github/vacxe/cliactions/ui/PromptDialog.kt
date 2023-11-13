package io.github.vacxe.cliactions.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import io.github.vacxe.cliactions.ui.theme.WidgetTheme
import io.github.vacxe.cliactions.ui.widgets.Buttons
import javax.swing.JComponent

class PromptDialog(project: Project): DialogWrapper(project) {
    init {
        title = "Demo"
        init()
    }
    override fun createCenterPanel(): JComponent {
        return ComposePanel().apply {
            setBounds(0, 0, 800, 600)
            setContent {
                WidgetTheme(darkTheme = true) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        Row {
                            Column(
                                modifier = Modifier.fillMaxHeight().weight(1f)
                            ) {
                                Buttons()
                            }
                            Box(
                                modifier = Modifier.fillMaxHeight().weight(1f)
                            ) {
                                LazyScrollable {

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
