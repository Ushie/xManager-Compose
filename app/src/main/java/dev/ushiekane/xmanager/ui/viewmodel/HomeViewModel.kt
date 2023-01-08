package dev.ushiekane.xmanager.ui.viewmodel

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.ushiekane.xmanager.BuildConfig
import dev.ushiekane.xmanager.domain.dto.Release
import dev.ushiekane.xmanager.domain.dto.SpotifyRelease
import dev.ushiekane.xmanager.domain.repository.GithubRepository
import dev.ushiekane.xmanager.installer.utils.install
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

class HomeViewModel(
    private val app: Application,
    private val client: HttpClient,
    private val repository: GithubRepository,
) : ViewModel() {
    val normalReleasesList = mutableStateListOf<Release.NormalReleases>()
    val amoledReleasesList = mutableStateListOf<Release.AmoledReleases>()
    val normalClonedReleasesList = mutableStateListOf<Release.NormalClonedReleases>()
    val amoledClonedReleasesList = mutableStateListOf<Release.AmoledClonedReleases>()
    val changeLog = mutableStateListOf<Release.Changelogs>()
    var latestNormalRelease by mutableStateOf("N/A")
        private set
    var latestAmoledRelease by mutableStateOf("N/A")
        private set
    private lateinit var file: File
    var status by mutableStateOf<Status>(Status.Idle)
        private set
    var downloaded by mutableStateOf(0.0)
        private set
    var total by mutableStateOf(0.0)
        private set
    var progress by mutableStateOf(0)
        private set
    var selectedRelease by mutableStateOf<SpotifyRelease?>(null)
        private set
    private val xManagerDirectory: File =
        Environment.getExternalStoragePublicDirectory("XManager").also { it.mkdirs() }
    val isAmoled =
        selectedRelease is Release.AmoledReleases || selectedRelease is Release.AmoledClonedReleases
    val isLatest =
        selectedRelease?.version == latestNormalRelease || selectedRelease?.version == latestAmoledRelease
    private val isCloned =
        selectedRelease is Release.NormalClonedReleases || selectedRelease is Release.AmoledClonedReleases

    private var downloadJob: Job? = null

    init {
        viewModelScope.launch {
            loadReleases()
        }
    }

    sealed interface Status {
        object Downloading : Status
        object Successful : Status
        object Confirm : Status
        object Existing : Status
        object Idle : Status
    }

    private suspend fun loadReleases() = withContext(Dispatchers.Main) {
        val releases = withContext(Dispatchers.IO) { repository.fetchReleases() }
        normalReleasesList.addAll(releases.releases.reversed())
        amoledReleasesList.addAll(releases.amoledReleases.reversed())
        normalClonedReleasesList.addAll(releases.clonedReleases.reversed())
        amoledClonedReleasesList.addAll(releases.amoledClonedReleases.reversed())
        changeLog.addAll(releases.modChangelogs.reversed())
        latestNormalRelease = releases.latest
        latestAmoledRelease = releases.amoledLatest
    }

    fun openDownloadLink(url: String) {
        app.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }

    private fun nameBuilder(version: String): String {
        val stringBuilder = StringBuilder("Spotify Mod $version ")
        if (isCloned) stringBuilder.append("[Cloned]")
        stringBuilder.append(if (isAmoled) "(Official & Amoled).apk" else "(Official).apk")
        return stringBuilder.toString()
    }

    private fun toast(text: String, duration: Int = Toast.LENGTH_SHORT) =
        viewModelScope.launch(Dispatchers.Main) { Toast.makeText(app, text, duration).show() }

    private fun convert(int: Double): Double {
        val df = DecimalFormat("##.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(int).toDouble()
    }

    fun delete() {
        xManagerDirectory.listFiles()
            ?.filterNot { it.isDirectory }
            ?.forEach { it.delete() }
        toast("Successfully deleted!")
        hideDialog()
    }

    fun openAppInfo(pkg: String) {
        app.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.parse("package:$pkg")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }

    fun openApp(pkg: String) {
        app.startActivity(app.packageManager.getLaunchIntentForPackage(pkg))
    }

    fun uninstall(pkg: String) {
        app.startActivity(Intent(Intent.ACTION_DELETE).apply {
            data = Uri.parse("package:$pkg")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }

    fun showConfirmDialog() {
        status = Status.Confirm
    }

    fun hideDialog() {
        status = Status.Idle
    }

    fun checkIfExisting(release: SpotifyRelease) {
        selectedRelease = release

        val name = nameBuilder(release.version)
        file = xManagerDirectory.resolve(name)
        if (file.exists()) status = Status.Existing else showConfirmDialog()
    }

    fun startDownload() {
        downloadJob = viewModelScope.launch(Dispatchers.Main) {
            val url = selectedRelease?.downloadUrl ?: return@launch
            status = Status.Downloading
            withContext(Dispatchers.IO) {
                try {
                    file.outputStream().use { out ->
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
                                    progress = if (content == 0) 0 else {
                                        convert((offset.toDouble() / content.toDouble() * 100)).roundToInt()
                                    }
                                    offset += bytes.size
                                    downloaded = convert(offset.toDouble().div(1024 * 1024))
                                    total = convert(content.toDouble().div(1024 * 1024))
                                }
                                channel.awaitContent()
                            }
                            status = Status.Successful
                        }
                    }
                } catch (e: CancellationException) {
                    toast("Download cancelled.")
                } catch (t: Throwable) {
                    toast("Download failed. Reason: ${t::class.simpleName}.")
                } finally {
                    downloaded = 0.0
                    total = 0.0
                    progress = 0
                }
            }
        }
    }

    fun installApk() {
        app.install(file)
        hideDialog()
    }

    fun copyToClipboard(release: SpotifyRelease) {
        val clipboard = app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(
            ClipData.newPlainText(
                BuildConfig.APPLICATION_ID, release.downloadUrl
            )
        )
        hideDialog()
    }

    fun fixer(release: SpotifyRelease) {
        copyToClipboard(release)
        openDownloadLink(release.downloadUrl)
        hideDialog()
    }

    fun cancel() {
        downloadJob?.cancel()
        hideDialog()
    }
}