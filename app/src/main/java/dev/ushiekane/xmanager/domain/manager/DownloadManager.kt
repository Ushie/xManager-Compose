package dev.ushiekane.xmanager.domain.manager

import io.ktor.client.HttpClient
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.request
import io.ktor.http.contentLength
import io.ktor.utils.io.core.isEmpty
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.core.use
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import java.io.File

class DownloadManager(
    private val client: HttpClient
) {
    suspend fun downloadApk(
        urls: MutableList<String>,
        out: File,
        progress: MutableSharedFlow<Pair<Int, Int>?>? = null
    ): File = withContext(Dispatchers.IO) {
        try {
            urls[0].let { url ->
                client.prepareGet(url).execute {
                    val file = out.resolve(it.request.headers["Location"]!!)

                    val tmpOut = out.resolve("${out.name}.tmp")
                        .apply { exists() || createNewFile() }

                    var offset = 0
                    val channel = it.bodyAsChannel()
                    val content = it.contentLength()?.toInt() ?: 0
                    while (!channel.isClosedForRead) {
                        val packet = channel.readRemaining(1024 * 1000 * 1)
                        while (!packet.isEmpty) {
                            val bytes = packet.readBytes()
                            withContext(Dispatchers.IO) {
                                file.outputStream().use { os ->
                                    os.write(bytes)
                                    os.flush()
                                }
                            }
                            if (progress != null) {
                                if (content > 0) {
                                    offset += bytes.size
                                    progress.emit(content to offset)
                                } else {
                                    progress.emit(null)
                                }
                            }
                        }
                        channel.awaitContent()
                    }
                    tmpOut.renameTo(out)
                    return@execute out
                }
            }
        } catch (e: Exception) {
            if (urls.isEmpty()) throw e // no more urls to try

            urls.removeAt(0) // remove the url that failed
            downloadApk(urls, out, progress) // try again
        }
    }
}