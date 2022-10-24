package dev.ushiekane.xmanager.api

import android.os.Environment
import android.webkit.URLUtil
import com.vk.knet.core.Knet
import com.vk.knet.core.http.HttpMethod
import com.vk.knet.core.http.HttpRequest
import com.vk.knet.cornet.CronetKnetEngine
import dev.ushiekane.xmanager.dto.Release
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

class API(cronet: CronetKnetEngine, private val json: Json) {

    private val client = Knet.Build(cronet)

    suspend fun ping(): Int {
        return try {
            withContext(Dispatchers.Main) {
                client.execute(HttpRequest(HttpMethod.GET, "https://api.github.com/")).statusCode
            }
        } catch (e: Exception) {
            return 0
        }
    }

    suspend fun fetchReleases() = withContext(Dispatchers.IO) {
        val stream = client.execute(HttpRequest(HttpMethod.GET, apiUrl)).body!!.asString()
        json.decodeFromString(stream) as Release
    }

    suspend fun download(url: String): File {
        val file = withContext(Dispatchers.IO) {
            client.execute(
                HttpRequest(
                    HttpMethod.GET,
                    url
                )
            )
        }
        val out = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).resolve(URLUtil.guessFileName(file.url, null, null))
        if (out.exists()) {
            return out
        }
        out.writeBytes(file.body!!.asBytes())
        return out
    }

    private companion object {
        private const val apiUrl =
            "https://raw.githubusercontent.com/xManager-v2/xManager-Spotify-Core/main/api/public.json"
    }
}