package org.stepic.droid.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import org.stepic.droid.base.MainApplication
import org.stepic.droid.core.ScreenManager
import javax.inject.Inject

class DownloadClickReceiver : BroadcastReceiver() {

    @Inject
    lateinit var screenProvider : ScreenManager

    init {
        MainApplication.component().inject(this)
    }

    override fun onReceive(context: Context, intent: Intent) = screenProvider.showDownload()
}
