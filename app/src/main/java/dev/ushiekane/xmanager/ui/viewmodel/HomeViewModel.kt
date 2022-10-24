package dev.ushiekane.xmanager.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.ushiekane.xmanager.api.API
import dev.ushiekane.xmanager.dto.Release
import dev.ushiekane.xmanager.util.openUrl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
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

    fun downloadApk(url: String) {
        viewModelScope.launch { API.download(app.cacheDir, url) }
    }

    init {
        viewModelScope.launch {
            loadReleases()
        }
    }
}