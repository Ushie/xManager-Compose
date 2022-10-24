package dev.ushiekane.xmanager.di

import dev.ushiekane.xmanager.api.API
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::API)
}