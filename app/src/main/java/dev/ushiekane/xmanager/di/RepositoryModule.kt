package dev.ushiekane.xmanager.di

import dev.ushiekane.xmanager.domain.repository.GithubRepository
import dev.ushiekane.xmanager.network.service.GithubService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::GithubService)
    singleOf(::GithubRepository)
}