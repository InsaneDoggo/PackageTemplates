package entryPoint

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import features.demo.ui.DemoDialog

class EntryPointAction: AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val virtualFile = event.getData(CommonDataKeys.VIRTUAL_FILE)
        val project = event.project

        //todo some stuff
        DemoDialog(project!!).show()
    }
}