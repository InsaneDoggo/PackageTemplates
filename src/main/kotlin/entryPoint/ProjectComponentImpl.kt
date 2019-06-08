package entryPoint

import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.project.Project
import core.di.ProjectModule
import core.logs.LogScopes
import core.logs.Logger
import core.misc.Const.PLUGIN_NAME
import org.koin.core.scope.Scope

class ProjectComponentImpl(val project: Project) : ProjectComponent {

    override fun getComponentName() = "$PLUGIN_NAME.ProjectComponent"

    private fun toScopeId() = project.name

    override fun initComponent() {
        Logger.i("initComponent", LogScopes.Project)

        initDI()
    }

    override fun disposeComponent() {
        Logger.i("disposeComponent", LogScopes.Project)

        disposeDI()
    }

    override fun projectOpened() {
        Logger.i("projectOpened", LogScopes.Project)
    }

    override fun projectClosed() {
        Logger.i("projectClosed", LogScopes.Project)
    }


    //==================================================================================================================
    //  DI
    //==================================================================================================================
    lateinit var scope: Scope

    private fun initDI() {
        scope = ApplicationComponentImpl.koin.createScope(toScopeId(), ProjectModule.SCOPE)
    }

    private fun disposeDI() {
        scope.close()
    }
}