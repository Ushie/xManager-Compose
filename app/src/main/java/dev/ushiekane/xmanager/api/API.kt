package dev.ushiekane.xmanager.api

import android.os.Environment
import com.vk.knet.core.Knet
import com.vk.knet.core.http.HttpMethod
import com.vk.knet.core.http.HttpRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class API(private val client: Knet)
{

    suspend fun enqueueDownload(url: String, fileName: String): File {
        val file = withContext(Dispatchers.IO) {
            client.execute(
                HttpRequest(
                    HttpMethod.GET,
                    url
                )
            )
        }
        val out = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).resolve(fileName)
        if (out.exists()) {
            return out
        }
        out.writeBytes(file.body!!.asBytes())
        return out
    }
}