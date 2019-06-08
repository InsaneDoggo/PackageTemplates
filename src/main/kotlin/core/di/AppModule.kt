package core.di

import core.resources.Dimens
import core.resources.Images
import core.resources.Strings
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun appModule(): Module = module {
    scope(named("App")) {
        scoped { Dimens() }
        scoped { Strings() }
        scoped { Images() }
    }
}