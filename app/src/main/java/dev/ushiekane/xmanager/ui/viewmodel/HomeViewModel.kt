package dev.ushiekane.xmanager.ui.viewmodel

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.webkit.URLUtil
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk.knet.core.Knet
import dev.ushiekane.xmanager.api.API
import dev.ushiekane.xmanager.domain.dto.Release
import dev.ushiekane.xmanager.domain.repository.GithubRepository
import dev.ushiekane.xmanager.ui.component.DownloadStatus
import dev.ushiekane.xmanager.util.get
import dev.ushiekane.xmanager.util.install
import dev.ushiekane.xmanager.util.openUrl
import kotlinx.coroutines.launch
import java.io.File

class HomeViewModel(
    private val app: Application,
    private val repository: GithubRepository,
    private val client: Knet,
    private val API: API,
) : ViewModel() {
    val normalReleasesList = mutableStateListOf<Release.NormalReleases>()
    val amoledReleasesList = mutableStateListOf<Release.AmoledReleases>()
    val normalClonedReleasesList = mutableStateListOf<Release.NormalClonedReleases>()
    val amoledClonedReleasesList = mutableStateListOf<Release.AmoledClonedReleases>()
    val changeLog = mutableStateListOf<Release.Changelogs>()
    var latestNormalRelease by mutableStateOf("N/A")
    var latestAmoledRelease by mutableStateOf("N/A")
    lateinit var fileLocation: String
    var status = DownloadStatus.DOWNLOADING
    var progress by mutableStateOf(0)
    var downloaded by mutableStateOf(0)
    var total by mutableStateOf(0)

    init {
        viewModelScope.launch {
            loadReleases()
        }
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

    fun installApk() = app.install(File(fileLocation))

    fun fixer(url: String) {
        val clipboard = app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("url", url))
        app.openUrl(url)
    }

    fun downloadApk(url: String) {
        viewModelScope.launch {
            val (resolvedUrl, fileName) = resolveUrlandName(url)
            val file = API.enqueueDownload(resolvedUrl, fileName)
            app.install(file)
        }
    }

    private fun resolveUrlandName(url: String): Pair<String, String> {
       val redirectedUrl = client.get(url).url
       val fileName = URLUtil.guessFileName(redirectedUrl, null, null)
       return Pair(redirectedUrl, fileName)
    }

}