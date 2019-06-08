package core.di

import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

object ModuleModule {

    val SCOPE = named("ModuleModule")

    fun createModule(): Module = module {
        scope(SCOPE) {
            // todo add stuff
        }
    }
}
