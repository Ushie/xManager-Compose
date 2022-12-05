package dev.ushiekane.xmanager.util

import android.os.Environment
import java.io.File

const val clonedPackageName = "com.spotify.musix"
const val packageName = "com.spotify.music"
val xManagerDirectory: File = Environment.getExternalStoragePublicDirectory("XManager").also { it.mkdirs() }