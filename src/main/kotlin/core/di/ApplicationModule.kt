package core.di

import core.resources.Dimens
import core.resources.Images
import core.resources.Strings
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module


object ApplicationModule {

    val SCOPE = named("ApplicationModule")

    fun createModule(): Module = module {
        scope(SCOPE) {
            scoped { Dimens() }
            scoped { Strings() }
            scoped { Images() }
        }
    }
}
