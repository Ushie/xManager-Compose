package dev.ushiekane.xmanager.network.service

import com.vk.knet.core.Knet
import dev.ushiekane.xmanager.domain.dto.Release
import dev.ushiekane.xmanager.util.body
import dev.ushiekane.xmanager.util.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json


class GithubService(private val client: Knet, private val json: Json) {

    suspend fun fetchReleases(): Release = withContext(Dispatchers.IO) {
        client.get(apiUrl).body(json)
    }

    private companion object {
        private const val apiUrl =
            "https://raw.githubusercontent.com/xManager-v2/xManager-Spotify-Core/main/api/public.json"
    }
}