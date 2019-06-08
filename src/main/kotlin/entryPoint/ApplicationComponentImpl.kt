package entryPoint

import com.intellij.openapi.components.ApplicationComponent
import core.di.appModule
import core.logs.LogScopes
import core.logs.Logger
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.logger.EmptyLogger

class ApplicationComponentImpl : ApplicationComponent {

    override fun getComponentName() = "PackageTemplatesAppComponent"


    //==================================================================================================================
    //  LifeCycle
    //==================================================================================================================
    companion object {
        lateinit var koin: Koin
    }

    override fun initComponent() {
        Logger.i("initComponent", LogScopes.Application)

        koin = startKoin {
            // declare used logger
            logger(EmptyLogger())
            // declare used modules
            modules(appModule())
        }.koin
    }

    override fun disposeComponent() {
        Logger.i("disposeComponent", LogScopes.Application)
    }
}