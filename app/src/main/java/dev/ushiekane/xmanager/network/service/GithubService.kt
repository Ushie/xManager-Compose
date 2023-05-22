package dev.ushiekane.xmanager.network.service

import dev.ushiekane.xmanager.domain.dto.Release
import dev.ushiekane.xmanager.domain.dto.Response
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class GithubService(private val client: HttpClient) {

    suspend fun fetchReleases(): Response = withContext(Dispatchers.IO) {
        client.get(apiUrl).body()
    }

    private companion object {
        private const val apiUrl =
            "The API link is currently closed sourced. Will add once API implementation is fixed."
    }
}