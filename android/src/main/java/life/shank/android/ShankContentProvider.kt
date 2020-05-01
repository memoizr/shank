package life.shank.android

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

class ShankContentProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        (context!!.applicationContext as Application).registerActivityLifecycleCallbacks(AutoScopedActivityLifecycleCallbacks)
        AppContextModule.apply { context = this@ShankContentProvider.context!!.applicationContext }
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? = null
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun getType(uri: Uri): String? = ""
}