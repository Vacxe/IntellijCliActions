package io.github.vacxe.cliactions.ui.toolwindow

import  io.github.vacxe.cliactions.model.Group
sealed class ToolWindowState {
    data class Loading(val message: String) : ToolWindowState()
    data class Error(val message: String): ToolWindowState()
    data class Content(val groups: List<Group>): ToolWindowState()
}
