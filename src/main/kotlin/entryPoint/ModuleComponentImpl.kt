package entryPoint

import com.intellij.openapi.module.ModuleComponent
import core.logs.LogScopes
import core.logs.Logger

class ModuleComponentImpl: ModuleComponent {
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