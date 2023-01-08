package dev.ushiekane.xmanager.network.service

import dev.ushiekane.xmanager.domain.dto.Release
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class GithubService(private val client: HttpClient) {

    suspend fun fetchReleases(): Release = withContext(Dispatchers.IO) {
        client.get(apiUrl).body()
    }

    private companion object {
        private const val apiUrl =
            "https://raw.githubusercontent.com/Ushie/xManager-Compose/main/api.json"
    }
}