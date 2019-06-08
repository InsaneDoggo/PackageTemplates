package features.demo.ui

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import core.base.BaseDialog
import javax.swing.JButton

class DemoDialog(project: Project) :
        BaseDialog(
                project,
                true,
                "CommandDialog",
                200, 100
        ) {

    override fun initUI() {
        root.add(JButton("Start demo").apply {
            ApplicationManager.getApplication().runWriteAction {
                addActionListener {
                    Thread.sleep(2000L)
                    Messages.showInfoMessage(project, "Done!", "Write Action")
                }
            }
        })
    }
}