package dev.ushiekane.xmanager.ui.viewmodel

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.ushiekane.xmanager.BuildConfig
import dev.ushiekane.xmanager.domain.dto.Amoled
import dev.ushiekane.xmanager.domain.dto.AmoledCloned
import dev.ushiekane.xmanager.domain.dto.AmoledClonedExperimental
import dev.ushiekane.xmanager.domain.dto.AmoledExperimental
import dev.ushiekane.xmanager.domain.dto.Changelogs
import dev.ushiekane.xmanager.domain.dto.Lite
import dev.ushiekane.xmanager.domain.dto.Release
import dev.ushiekane.xmanager.domain.dto.Stock
import dev.ushiekane.xmanager.domain.dto.StockCloned
import dev.ushiekane.xmanager.domain.dto.StockClonedExperimental
import dev.ushiekane.xmanager.domain.dto.StockExperimental
import dev.ushiekane.xmanager.domain.manager.DownloadManager
import dev.ushiekane.xmanager.domain.repository.GithubRepository
import dev.ushiekane.xmanager.installer.utils.install
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

class HomeViewModel(
    private val app: Application,
    private val repository: GithubRepository,
    private val downloadManager: DownloadManager,
) : ViewModel() {
    val stockReleases = mutableStateListOf<Stock>()
    val amoledReleases = mutableStateListOf<Amoled>()
    val stockClonedReleases = mutableStateListOf<StockCloned>()
    val amoledClonedReleases = mutableStateListOf<AmoledCloned>()
    val stockExperimentalReleases = mutableStateListOf<StockExperimental>()
    val amoledExperimentalReleases = mutableStateListOf<AmoledExperimental>()
    val stockClonedExperimentalReleases = mutableStateListOf<StockClonedExperimental>()
    val amoledClonedExperimentalReleases = mutableStateListOf<AmoledClonedExperimental>()

    val liteReleases = mutableStateListOf<Lite>()
    val changeLog = mutableStateListOf<Changelogs>()

    var status by mutableStateOf<Status>(Status.Idle)
        private set

    private var progressFlow = MutableSharedFlow<Pair<Int, Int>?>()
    var downloaded by mutableStateOf(0.0)
        private set
    var total by mutableStateOf(0.0)
        private set
    var percentage by mutableStateOf(0)
        private set

    private val xManagerDirectory: File =
        Environment.getExternalStoragePublicDirectory("XManager").also { it.mkdirs() }

    private var downloadJob: Job? = null

    var selectedRelease by mutableStateOf<Release?>(null)
        private set

    init {
        viewModelScope.launch {
            loadReleases()
        }
    }

    sealed interface Status {
        object Downloading : Status
        class Successful(val file: File) : Status
        object Confirm : Status

        // object Existing : Status
        object Idle : Status
    }
    private suspend fun loadReleases() = withContext(Dispatchers.IO) {
        val releases = repository.fetchReleases()

        withContext(Dispatchers.Main) {
            stockReleases.addAll(releases.stockReleases.reversed())
            amoledReleases.addAll(releases.amoledReleases.reversed())
            stockClonedReleases.addAll(releases.clonedReleases.reversed())
            amoledClonedReleases.addAll(releases.amoledClonedReleases.reversed())
            stockExperimentalReleases.addAll(releases.experimentalReleases.reversed())
            amoledExperimentalReleases.addAll(releases.amoledExperimentalReleases.reversed())
            stockClonedExperimentalReleases.addAll(releases.clonedExperimentalReleases.reversed())
            amoledClonedExperimentalReleases.addAll(releases.amoledClonedExperimentalReleases.reversed())
            changeLog.addAll(releases.changelogs.reversed())
        }
    }

    fun openDownloadLink(url: String) {
        app.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }

    fun delete() {
        xManagerDirectory.deleteRecursively()
    }


    fun startDownload(release: Release) = viewModelScope.launch(Dispatchers.Main) {
            status = Status.Downloading

            val urls = mutableListOf(release.downloadUrl, release.downloadUrl2, release.downloadUrl3)

            val collector = viewModelScope.launch(Dispatchers.IO) {
                progressFlow
                    .collect { pair ->
                        if (pair == null) return@collect
                        downloaded = convert(pair.first.toDouble().div(1024 * 1024))
                        total = convert(pair.second.toDouble().div(1024 * 1024))
                        percentage = convert((pair.first.toDouble() / pair.second.toDouble() * 100)).roundToInt()
                    }
            }

            val file = withContext(Dispatchers.IO) {
                downloadManager.downloadApk(urls, xManagerDirectory, progressFlow)
            }

            status = Status.Successful(file)
            collector.cancel()
            downloaded = 0.0
            total = 0.0
            percentage = 0
        }

    fun installApk(file: File) {
        app.install(file)
    }
    
    fun copyToClipboard(release: Release) {
        val clipboard = app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(
            ClipData.newPlainText(
                BuildConfig.APPLICATION_ID, release.downloadUrl
            )
        )
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

    fun fixer(release: Release) {
        copyToClipboard(release)
        openDownloadLink(release.downloadUrl)
    }

    fun dismissDialogAndCancel() {
        downloadJob?.cancel()
        selectedRelease = null
        status = Status.Idle
    }
    fun confirmDialog(release: Release) {
        selectedRelease = release
        status = Status.Confirm
    }

    private fun convert(int: Double): Double {
        val df = DecimalFormat("##.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(int).toDouble()
    }
}