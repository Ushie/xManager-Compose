package dev.ushiekane.xmanager.domain.manager

import io.ktor.client.HttpClient
import io.ktor.client.request.head
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentLength
import io.ktor.utils.io.core.isEmpty
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.core.use
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

class DownloadManager(
    private val client: HttpClient
) {
    suspend fun downloadApk(
        urls: MutableList<String>,
        out: File,
        progress: MutableSharedFlow<Pair<Int, Double>?>? = null
    ): File = withContext(Dispatchers.IO) {
        urls.firstNotNullOf { url ->
            client.head(url).takeIf { it.status == HttpStatusCode.OK }?.headers?.get("Location")
        }.let {
            client.prepareGet(it).execute { response ->
                val file =
                    response.request.url.encodedPath.substringAfterLast("/").let { name ->
                        out.resolve(name)
                    }

                val tmpOut = out.resolve("${out.name}.tmp")
                    .apply { exists() || createNewFile() }

                var offset = 0
                val channel = response.bodyAsChannel()
                val content = response.contentLength()?.toInt() ?: 0
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

                                val percent =
                                    (offset.toDouble() / content.toDouble() * 100).roundToInt()

                                val df = DecimalFormat("##.##")
                                df.roundingMode = RoundingMode.CEILING

                                val total =
                                    df.format(content.toDouble().div(1024 * 1024)).toDouble()
                                progress.emit(percent to total)
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
    }
}