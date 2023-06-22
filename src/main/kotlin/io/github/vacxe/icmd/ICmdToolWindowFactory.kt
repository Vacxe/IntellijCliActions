package io.github.vacxe.icmd

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import io.github.vacxe.icmd.ui.ICmdTablePanel

class ICmdToolWindowFactory : DumbAware, ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val cmdPanel = ICmdTablePanel(project)
        val content: Content = ContentFactory.SERVICE.getInstance().createContent(cmdPanel, null, true)
        content.setDisposer(cmdPanel::dispose)
        content.preferredFocusableComponent = cmdPanel
        toolWindow.contentManager.addContent(content)
        cmdPanel.initialise()
    }
}