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
import dev.ushiekane.xmanager.domain.dto.AmoledRelease
import dev.ushiekane.xmanager.domain.dto.Changelogs
import dev.ushiekane.xmanager.domain.dto.ClonedRelease
import dev.ushiekane.xmanager.domain.dto.ExperimentalRelease
import dev.ushiekane.xmanager.domain.dto.Lite
import dev.ushiekane.xmanager.domain.dto.Release
import dev.ushiekane.xmanager.domain.dto.Stock
import dev.ushiekane.xmanager.domain.dto.StockCloned
import dev.ushiekane.xmanager.domain.dto.StockClonedExperimental
import dev.ushiekane.xmanager.domain.dto.StockExperimental
import dev.ushiekane.xmanager.domain.manager.DownloadManager
import dev.ushiekane.xmanager.domain.repository.GithubRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.StringJoiner
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
    var selectedRelease by mutableStateOf<Release?>(null)
        private set

    private val xManagerDirectory: File =
        Environment.getExternalStoragePublicDirectory("XManager").also { it.mkdirs() }
    private var downloadJob: Job? = null


    var isCloned = false
    var isExperimental = false
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
        xManagerDirectory.listFiles()
            ?.filterNot { it.isDirectory }
            ?.forEach { it.delete() }
    }

    fun checkIfExists(release: Release) {
        selectedRelease = release
        val exists = xManagerDirectory.listFiles { f -> f.isFile }
            ?.any { it.name.contains(release.version) }
        if (exists == true) {
            startDownload()
        }
    }


    fun startDownload() {
        downloadJob = viewModelScope.launch(Dispatchers.Main) {
            val url = selectedRelease!!.downloadUrl
            val fileName = StringJoiner(" ")
                .add("${selectedRelease!!.version} ")
                .add(if (selectedRelease is AmoledRelease) "Amoled" else "")
                .add(if (selectedRelease is ClonedRelease) "Cloned" else "")
                .add(if (selectedRelease is ExperimentalRelease) "Experimental" else "")
                .toString().plus(".apk")

            withContext(Dispatchers.IO) {
                downloadManager.download(url, xManagerDirectory.resolve(fileName), progressFlow)
            }

            status = Status.Downloading
            progressFlow.collect {
                if (it != null) {
                    downloaded = convert(it.first.toDouble().div(1024 * 1024))
                    total = convert(it.second.toDouble().div(1024 * 1024))
                    percentage = convert((it.first.toDouble() / it.second.toDouble() * 100)).roundToInt()

                }
            }
        }
    }

    fun installApk() {
        // app.install(file)
    } // TODO: FIXXXXXXX
    
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

    fun fixer() {
        copyToClipboard(selectedRelease!!)
        openDownloadLink(selectedRelease!!.downloadUrl)
    }

    fun cancel() {
        downloadJob?.cancel()
    }

    private fun convert(int: Double): Double {
        val df = DecimalFormat("##.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(int).toDouble()
    }
}