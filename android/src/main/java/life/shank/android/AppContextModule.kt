package life.shank.android

import android.content.Context
import life.shank.ShankModule
import life.shank.single

object AppContextModule : ShankModule {
    internal lateinit var context: Context
    val appContext = single { -> context }
}