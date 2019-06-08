package entryPoint

import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleComponent
import core.di.ModuleModule
import core.logs.LogScopes
import core.logs.Logger
import core.misc.Const.PLUGIN_NAME
import org.koin.core.scope.Scope

class ModuleComponentImpl(var module: Module) : ModuleComponent {

    override fun getComponentName() = "$PLUGIN_NAME.ModuleComponent"

    override fun initComponent() {
        Logger.i("initComponent", LogScopes.Module)

        initDI()
    }

    override fun disposeComponent() {
        Logger.i("disposeComponent", LogScopes.Module)

        disposeDI()
    }

    override fun moduleAdded() {
        Logger.i("moduleAdded", LogScopes.Module)
    }


    //==================================================================================================================
    //  DI
    //==================================================================================================================
    lateinit var scope: Scope

    private fun toScopeId() = "${module.project.name}.${module.name}"

    private fun initDI() {
        scope = ApplicationComponentImpl.koin.createScope(toScopeId(), ModuleModule.SCOPE)
    }

    private fun disposeDI() {
        scope.close()
    }

}