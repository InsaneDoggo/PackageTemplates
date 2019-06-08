package entryPoint

import com.intellij.openapi.components.ProjectComponent
import core.logs.LogScopes
import core.logs.Logger

class ProjectComponentImpl : ProjectComponent {

    override fun getComponentName() = super.getComponentName()

    override fun disposeComponent() {
        Logger.i("disposeComponent $componentName", LogScopes.Project)
    }

    override fun projectClosed() {
        Logger.i("projectClosed $componentName", LogScopes.Project)
    }

    override fun initComponent() {
        Logger.i("initComponent $componentName", LogScopes.Project)
    }

    override fun projectOpened() {
        Logger.i("projectOpened $componentName", LogScopes.Project)
    }
}