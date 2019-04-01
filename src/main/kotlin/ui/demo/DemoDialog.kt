package ui.demo

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import ui.base.BaseDialog
import javax.swing.JButton

class DemoDialog(project: Project) :
        BaseDialog(
                project,
                true,
                "CommandDialog"
        ) {

    override fun initUI() {
        root.add(JButton("Start demo").apply {
            addActionListener { Messages.showInfoMessage(project, "Demo started", "Demo Action") }
        })
    }
}