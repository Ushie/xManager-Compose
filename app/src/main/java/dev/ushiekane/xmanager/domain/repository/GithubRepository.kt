package dev.ushiekane.xmanager.domain.repository

import dev.ushiekane.xmanager.network.service.GithubService

class GithubRepository(private val service: GithubService) {
    suspend fun fetchReleases() = service.fetchReleases()
}