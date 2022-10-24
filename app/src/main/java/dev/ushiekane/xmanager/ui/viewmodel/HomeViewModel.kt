package dev.ushiekane.xmanager.ui.viewmodel

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.ushiekane.xmanager.api.API
import dev.ushiekane.xmanager.dto.Release
import dev.ushiekane.xmanager.util.install
import dev.ushiekane.xmanager.util.openUrl
import kotlinx.coroutines.launch

class HomeViewModel(
    private val app: Application,
    private val API: API
) : ViewModel() {
    val normalReleasesList = mutableStateListOf<Release.NormalReleases>()
    val amoledReleasesList = mutableStateListOf<Release.AmoledReleases>()
    val normalClonedReleasesList = mutableStateListOf<Release.NormalClonedReleases>()
    val amoledClonedReleasesList = mutableStateListOf<Release.AmoledClonedReleases>()
    val changeLog = mutableStateListOf<Release.Changelogs>()
    var latestNormalRelease by mutableStateOf("N/A")
    var latestAmoledRelease by mutableStateOf("N/A")

    private fun loadReleases() {
        viewModelScope.launch {
            val releases = API.fetchReleases()
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

    fun fixer(url: String) {
        val clipboard = app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("url", url))
        app.openUrl(url)
    }


    fun downloadApk(url: String) {
        viewModelScope.launch {
            val file = API.download(url)
            app.install(file)
        }
    }

    init {
        viewModelScope.launch {
            loadReleases()
        }
    }
}