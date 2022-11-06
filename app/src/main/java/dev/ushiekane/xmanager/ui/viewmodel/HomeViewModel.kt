package dev.ushiekane.xmanager.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Environment
import android.webkit.URLUtil
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.download.library.DownloadImpl
import com.download.library.DownloadListenerAdapter
import com.download.library.DownloadTask
import com.download.library.Extra
import com.vk.knet.core.Knet
import dev.ushiekane.xmanager.domain.dto.Release
import dev.ushiekane.xmanager.domain.repository.GithubRepository
import dev.ushiekane.xmanager.util.get
import dev.ushiekane.xmanager.util.install
import dev.ushiekane.xmanager.util.openUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

class HomeViewModel(
    private val app: Application,
    private val repository: GithubRepository,
    private val client: Knet,
    private val download: DownloadImpl
) : ViewModel() {
    val normalReleasesList = mutableStateListOf<Release.NormalReleases>()
    val amoledReleasesList = mutableStateListOf<Release.AmoledReleases>()
    val normalClonedReleasesList = mutableStateListOf<Release.NormalClonedReleases>()
    val amoledClonedReleasesList = mutableStateListOf<Release.AmoledClonedReleases>()
    val changeLog = mutableStateListOf<Release.Changelogs>()
    var latestNormalRelease by mutableStateOf("N/A")
    var latestAmoledRelease by mutableStateOf("N/A")
    private lateinit var file: String

    init {
        viewModelScope.launch {
            loadReleases()
        }
    }

    sealed class Status {
        object Downloading: Status()
        object Failed: Status()
        object Successful: Status()
        object Idle: Status()
        object Confirm: Status()
    }
    var downloadStatus by mutableStateOf<Status>(Status.Idle)
    var downloadedSize by mutableStateOf<Long>(0)
    var totalSize by mutableStateOf<Long>(0)

    private suspend fun enqueueDownload(url: String, fileName: String) = withContext(Dispatchers.IO) {
        file = Environment.getExternalStoragePublicDirectory("XManager").resolve(fileName).absolutePath
        download.url(url).target(file).setForceDownload(true).setRetry(3)
            .quickProgress().enqueue(object : DownloadListenerAdapter() {
                override fun onStart(
                    url: String?,
                    userAgent: String?,
                    contentDisposition: String?,
                    mimetype: String?,
                    contentLength: Long,
                    extra: Extra?
                ) {
                    super.onStart(url, userAgent, contentDisposition, mimetype, contentLength, extra)
                    if (extra != null) {
                        totalSize = extra.contentLength
                    }
                }


                override fun onProgress(url: String?, downloaded: Long, length: Long, usedTime: Long) {
                    super.onProgress(url, downloaded, length, usedTime)
                    downloadedSize = downloaded
                }

                @SuppressLint("SwitchIntDef")
                override fun onDownloadStatusChanged(extra: Extra?, status: Int) {
                    when (status) {
                        DownloadTask.STATUS_SUCCESSFUL -> {downloadStatus = Status.Successful}
                        DownloadTask.STATUS_DOWNLOADING -> {downloadStatus = Status.Downloading}
                        DownloadTask.STATUS_ERROR -> {downloadStatus = Status.Failed}
                        DownloadTask.STATUS_CANCELED -> {downloadStatus = Status.Idle}
                    }
                }
            })
    }

    private fun loadReleases() {
        viewModelScope.launch {
            val releases = repository.fetchReleases()
            normalReleasesList.addAll(releases.releases.reversed())
            amoledReleasesList.addAll(releases.amoledReleases.reversed())
            normalClonedReleasesList.addAll(releases.clonedReleases.reversed())
            amoledClonedReleasesList.addAll(releases.amoledClonedReleases.reversed())
            changeLog.addAll(releases.modChangelogs.reversed())
            latestNormalRelease = releases.latest
            latestAmoledRelease = releases.amoledLatest
        }
    }

    fun openDownloadLink(link: String) {
        app.openUrl(link)
    }

    fun installApk()  {
        if (this::file.isInitialized) {
            app.install(File(file))
        } else {
            Toast.makeText(app, "APK file not found.", Toast.LENGTH_SHORT).show()
        }
    }

    fun fixer(url: String) {
        val clipboard = app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("url", url))
        app.openUrl(url)
    }

    fun downloadApk(url: String) {
        viewModelScope.launch {
            downloadStatus = Status.Downloading
            val (resolvedUrl, fileName) = resolveUrlandName(url)
            enqueueDownload(resolvedUrl, fileName)
        }
    }
    private fun resolveUrlandName(url: String): Pair<String, String> {
       val redirectedUrl = client.get(url).url
       val fileName = URLUtil.guessFileName(redirectedUrl, null, null)
       return Pair(redirectedUrl, fileName)
    }

    fun calculateSize(size: Long): Double {
        val df = DecimalFormat("##.###")
        df.roundingMode = RoundingMode.CEILING
        return df.format(size.div(1024 * 1024)).toDouble()
    }

    fun onComplete() {
        downloadStatus = Status.Successful
    }
    fun cancel() = download.cancelAll()

    fun calculateBar(download: Long, total: Long): Float = calculatePercentage(download, total).div(100).toFloat()

    fun calculatePercentage(download: Long, total: Long): Double {
        if (total.toInt() == 0) {
            return 0.0
        }
        val downloaded = calculateSize(download)
        val totalSize = calculateSize(total)
        return (downloaded / totalSize * 100).roundToInt().toDouble()
    }
}