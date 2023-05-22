package dev.ushiekane.xmanager.domain.manager

import io.ktor.client.HttpClient
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.contentLength
import io.ktor.utils.io.core.isEmpty
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.core.use
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import java.io.File

class DownloadManager(
    private val client: HttpClient
) {
    private var downloadJob: Job? = null

    suspend fun download(
        url: String,
        out: File,
        progress: MutableSharedFlow<Pair<Int, Int>?>? = null
    ): File = withContext(Dispatchers.IO) {
        val tmpOut = out.resolveSibling("${out.name}.tmp")
            .apply { exists() || createNewFile() }

        tmpOut.outputStream().use { out ->
            client.prepareGet(url).execute {
                var offset = 0
                val channel = it.bodyAsChannel()
                val content = it.contentLength()?.toInt() ?: 0
                while (!channel.isClosedForRead) {
                    val packet = channel.readRemaining(1024 * 1000 * 1)
                    while (!packet.isEmpty) {
                        val bytes = packet.readBytes()
                        withContext(Dispatchers.IO) {
                            out.write(bytes)
                            out.flush()
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

            }
        }
        tmpOut.renameTo(out)
        return@withContext out
    }

}