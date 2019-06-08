package entryPoint

import com.intellij.openapi.module.ModuleComponent
import core.logs.LogScopes
import core.logs.Logger
import core.misc.Const.PLUGIN_NAME

class ModuleComponentImpl: ModuleComponent {

    override fun getComponentName() = "$PLUGIN_NAME.ModuleComponent"

    override fun disposeComponent() {
        Logger.i("disposeComponent", LogScopes.Module)
    }

    override fun initComponent() {
        Logger.i("initComponent", LogScopes.Module)
    }

    override fun moduleAdded() {
        Logger.i("moduleAdded", LogScopes.Module)
    }
}