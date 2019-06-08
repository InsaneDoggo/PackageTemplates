package entryPoint

import com.intellij.openapi.components.ApplicationComponent
import core.di.ApplicationModule
import core.di.ModuleModule
import core.di.ProjectModule
import core.logs.LogScopes
import core.logs.Logger
import core.misc.Const.PLUGIN_NAME
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.logger.EmptyLogger

class ApplicationComponentImpl : ApplicationComponent {

    companion object {
        lateinit var koin: Koin
    }

    override fun getComponentName() = "$PLUGIN_NAME.ApplicationComponent"

    override fun initComponent() {
        Logger.i("initComponent", LogScopes.Application)

        iniDI()
    }

    override fun disposeComponent() {
        Logger.i("disposeComponent", LogScopes.Application)
    }

    private fun iniDI() {
        koin = startKoin {
            logger(EmptyLogger())

            modules(arrayListOf(
                    ApplicationModule.createModule(),
                    ProjectModule.createModule(),
                    ModuleModule.createModule()
            ))
        }.koin
    }
}