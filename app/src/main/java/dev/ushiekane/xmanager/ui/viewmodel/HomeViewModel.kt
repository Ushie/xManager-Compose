package dev.ushiekane.xmanager.ui.viewmodel

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import dev.ushiekane.xmanager.domain.dto.Release
import dev.ushiekane.xmanager.domain.repository.GithubRepository
import dev.ushiekane.xmanager.util.delete
import dev.ushiekane.xmanager.util.install
import dev.ushiekane.xmanager.util.openUrl
import dev.ushiekane.xmanager.util.xManagerDirectory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

class HomeViewModel(
    private val app: Application,
    private val repository: GithubRepository,
) : ViewModel() {
    val normalReleasesList = mutableStateListOf<Release.NormalReleases>()
    val amoledReleasesList = mutableStateListOf<Release.AmoledReleases>()
    val normalClonedReleasesList = mutableStateListOf<Release.NormalClonedReleases>()
    val amoledClonedReleasesList = mutableStateListOf<Release.AmoledClonedReleases>()
    val changeLog = mutableStateListOf<Release.Changelogs>()
    var latestNormalRelease by mutableStateOf("N/A")
    var latestAmoledRelease by mutableStateOf("N/A")
    private lateinit var file: File
    var status by mutableStateOf<Status>(Status.Idle)
    var downloaded by mutableStateOf(0)
    var total by mutableStateOf(0)
    private val downloader = FileDownloader.getImpl()!!


    init {
        viewModelScope.launch {
            loadReleases()
        }
    }

    sealed class Status {
        object Downloading : Status()
        object Successful : Status()
        object Confirm : Status()
        object Existing : Status()
        object Idle : Status()
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

    fun delete() {
        app.delete()
    }

    fun nameBuilder(version: String, isCloned: Boolean, isAmoled: Boolean): String {
        var string = "Spotify Mod "
        string += version
        if (isCloned) string += "[Cloned]"
        string += if (isAmoled) "(Official & Amoled).apk" else "(Official).apk"
        return string
    }

    fun download(url: String, name: String) {
        status = Status.Downloading
        file = xManagerDirectory.resolve(name)
        val listener = object : FileDownloadListener() {
            override fun pending(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {}

            override fun connected(
                task: BaseDownloadTask?,
                etag: String?,
                isContinue: Boolean,
                soFarBytes: Int,
                totalBytes: Int
            ) {
                total = task?.smallFileTotalBytes ?: 0
            }

            override fun progress(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                downloaded = task?.smallFileSoFarBytes ?: 0
            }

            override fun completed(task: BaseDownloadTask?) {
                status = Status.Successful
            }

            override fun paused(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {}

            override fun error(task: BaseDownloadTask?, e: Throwable?) {
                e?.printStackTrace()
                Toast.makeText(app, "Failed downloading APK.", Toast.LENGTH_LONG).show()
                status = Status.Idle
            }

            override fun warn(task: BaseDownloadTask?) {}
        }
        downloader.create(url).setPath(file.path).setListener(listener).setForceReDownload(true)
            .start()
    }

    fun installApk(name: String) {
        with(xManagerDirectory.resolve(name)) {
            if (this.exists()) {
                app.install(this)
                cancel()
            } else {
                Toast.makeText(app, "APK file not found.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun copyToClipboard(url: String) {
        val clipboard = app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("url", url))
    }

    fun fixer(url: String) {
        copyToClipboard(url)
        openDownloadLink(url)
    }

    fun checkIfExisting(name: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val alreadyExists = xManagerDirectory.resolve(name).exists()
            if (alreadyExists) {
                status = Status.Existing
                return@launch
            } else status = Status.Confirm
        }
    }

    private fun format(int: Double): Double {
        val df = DecimalFormat("##.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(int).toDouble()
    }

    fun calculateSize(size: Int): Double = format(size.toDouble().div(1024 * 1024))

    fun cancel() {
        status = Status.Idle
        downloaded = 0
        total = 0
        downloader.run {
            pauseAll()
            clearAllTaskData()
        }
    }

    fun calculateBar(download: Int, total: Int): Float =
        calculatePercentage(download, total).toDouble().div(100).toFloat()

    fun calculatePercentage(download: Int, total: Int): Int {
        if (total == 0) {
            return 0
        }
        val downloaded = calculateSize(download)
        val totalSize = calculateSize(total)
        return format(downloaded / totalSize * 100).roundToInt()
    }
}