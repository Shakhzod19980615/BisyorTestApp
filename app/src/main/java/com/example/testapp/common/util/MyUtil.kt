package com.example.testapp.common.util

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.*

object MyUtil {

   /* fun formatDateAfter(month: Int): String? {

        //making date of 1 month after today 00:00 time
        val calendar: Calendar = GregorianCalendar()
        calendar.add(Calendar.MONTH, month)
        val thatDate = calendar.time
        val months = getLocalizedArray(App.application, R.array.months)
        return (SimpleDateFormat("d ").format(thatDate)
                + months[calendar[Calendar.MONTH]]
                + SimpleDateFormat(" yyyy ").format(thatDate)
                + getLocalizedString(App.application, R.string.text_year)
                + SimpleDateFormat(", HH:mm").format(thatDate))
    }*/

    fun showSoftKeyboard(context: Context, editText: EditText?) {
        Handler().postDelayed({
            val inputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED)
        }, 100)
    }

    fun closeSoftKeyboard(activity: Activity) {
        val currentFocusView: View? = activity.currentFocus
        if (currentFocusView != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocusView.windowToken, 0)
        }
    }

    fun dpToPx(dp: Int): Int {
        val metrics = Resources.getSystem().displayMetrics
        return (dp * metrics.density).toInt()
    }

    fun pxToDp(px: Int): Int {
        val metrics = Resources.getSystem().displayMetrics
        return (px / metrics.density).toInt()
    }


    fun pxToSp(px: Int): Int {
        val metrics: DisplayMetrics = Resources.getSystem().displayMetrics
        return px / metrics.scaledDensity.toInt()
    }

    fun getScreenWidth(activity: Activity): Int {
        val display: Display = activity.getWindowManager().getDefaultDisplay()
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        return outMetrics.widthPixels
    }

    fun getScreenHeight(activity: Activity): Int {
        val display: Display = activity.getWindowManager().getDefaultDisplay()
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        return outMetrics.heightPixels
    }

    fun setIconColor(iconHolder: ImageView, color: Int) {
        val wrappedDrawable: Drawable = DrawableCompat.wrap(iconHolder.drawable)
        DrawableCompat.setTint(wrappedDrawable, color)
        iconHolder.setImageDrawable(wrappedDrawable)
        iconHolder.invalidate()
    }


    fun removeGlobalLayoutObserver(
        view: View,
        layoutListener: ViewTreeObserver.OnGlobalLayoutListener?
    ) {
        view.viewTreeObserver.removeOnGlobalLayoutListener(layoutListener)
    }

    fun getHostActivity(context: Context): Activity? {
        var context = context
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context as Activity
            }
            context = (context as ContextWrapper).getBaseContext()
        }
        return null
    }

    //check file to exists before using this method
    fun readFileAsString(filePath: String?): String {
        try {
            FileInputStream(File(filePath)).use { fis ->
                var current: Char
                val builder = StringBuilder()
                while (fis.available() > 0) {
                    builder.append(fis.read() as Char)
                }
                return builder.toString()
            }
        } catch (e: Exception) {
            Log.d("TourGuide", e.toString())
        }
        return ""
    }

   /* fun getLocalizedString(context: Context?, resourceId: Int): String {
        val configuration = context!!.resources.configuration
        val language = App.getSettingStorage().language
        configuration.setLocale(Locale(language))
        return context.createConfigurationContext(configuration).resources.getString(resourceId)
    }*/

    /*fun getLocalizedArray(context: Context, resourceId: Int): Array<String> {
        val configuration = context.resources.configuration
        val language: String = App.getSettingStorage().language
        configuration.setLocale(Locale(language))
        return context.createConfigurationContext(configuration).resources.getStringArray(resourceId)
    }*/

    fun formatPhoneNumber(phone: String?): String {
        if (phone == null || phone.isEmpty()) return ""
        var temp: String = phone
        println(phone)
        if (!phone.startsWith("+")) {
            temp = if (!phone.startsWith("998")) "+998$temp" else "+$temp"
        }
        return (temp.substring(0, 4) + " "
                + temp.substring(4, 6) + " "
                + temp.substring(6, 9) + " "
                + temp.substring(9, 11) + " "
                + temp.substring(11, 13))
    }


    inline fun <reified T> pairListKeys(list: List<Pair<T, T>>): Array<T> {
        val result: MutableList<T> = ArrayList()
        for (listItem in list) {
            result.add(listItem.first!!)
        }
        return result.toTypedArray()
    }

    inline fun <reified T> pairListValues(list: List<Pair<T, T>>): Array<T> {
        val result: MutableList<T> = ArrayList()
        for (listItem in list) {
            result.add(listItem.second!!)
        }
        return result.toTypedArray()
    }

    /*fun formatDateDayMonth(date: Date): String {
        var context: Context? = null
        try {
            context = App.application
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

        //making date of 1 month after today 00:00 time
        val calendar: Calendar = GregorianCalendar()
        calendar.time = date
        val months = getLocalizedArray(context!!, R.array.months)
        return (calendar[Calendar.DAY_OF_MONTH]
            .toString() + " "
                + months[calendar[Calendar.MONTH]])
    }*/

    val socialItems: Pair<List<String>, List<String>>
        get() {
            val result: MutableList<String> = ArrayList()
            val result2: MutableList<String> = ArrayList()
            result.add("https://img.bisyor.ru/web/uploads/social_icons/facebook.png")
            result.add("https://img.bisyor.ru/web/uploads/social_icons/instagram.png")
            result.add("https://img.bisyor.ru/web/uploads/social_icons/vkontakte.png")
            result.add("https://img.bisyor.ru/web/uploads/social_icons/ok.png")
            result.add("https://img.bisyor.ru/web/uploads/social_icons/telegram.png")
            result.add("https://img.bisyor.ru/web/uploads/social_icons/google.png")
            result.add("https://img.bisyor.ru/web/uploads/social_icons/moymir.png")
            result.add("https://img.bisyor.ru/web/uploads/social_icons/yandeks.png")
            result.add("https://img.bisyor.ru/web/uploads/social_icons/openid.png")
            result.add("https://img.bisyor.ru/web/uploads/noimg.jpg")
            result.add("https://img.bisyor.ru/web/uploads/social_icons/aol.png")
            result.add("https://img.bisyor.ru/web/uploads/social_icons/twitter.png")
            result.add("https://img.bisyor.ru/web/uploads/social_icons/linkedin.png")
            result.add("https://img.bisyor.ru/web/uploads/social_icons/live.png")
            result.add("https://img.bisyor.ru/web/uploads/social_icons/foursquare.png")
            result2.add("Facebook")
            result2.add("Instagram")
            result2.add("Вконтакте")
            result2.add("Одноклассники")
            result2.add("Telegram")
            result2.add("Google")
            result2.add("Мой мир")
            result2.add("Яндекс")
            result2.add("OpenID")
            result2.add("Yahoo")
            result2.add("AOL")
            result2.add("Twitter")
            result2.add("Linkedin")
            result2.add("Live")
            result2.add("Foursquare")
            return Pair(result, result2)
        }

    //TODO: - getting Path from URI
    fun getPathFromUri(context: Context, uri: Uri): String? {
        val isKitKat = true

        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId: String = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                val id: String = DocumentsContract.getDocumentId(uri)
                val contentUri: Uri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId: String = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(
                    split[1]
                )
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {

            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                context,
                uri,
                null,
                null
            )
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            cursor = context.contentResolver.query(
                uri!!, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    fun hidePhone(phone: String?): String {
        var phone = phone ?: return ""
        phone = "+" + phone.replace("[^\\d.]".toRegex(), "")
        return phone.replaceFirst(
            "(\\d{3})(\\d{2})(\\d{3})(\\d{2})(\\d+)".toRegex(),
            "$1 ($2) ***-**-$5"
        )
    }

    fun <T> leftJoinToLists(first: MutableList<T>, second: List<T>): List<T> {
        for (x in second) {
            if (!first.contains(x)) first.add(x)
        }
        return first
    }

    fun colorSubSeq(text: String, whichWordColor: String, textView: TextView, color: Int) {
        val textUpper = text.toUpperCase()
        val whichWordColorUpper = whichWordColor.toUpperCase()
        val ss = SpannableString(text)
        var strar = 0

        while (textUpper.indexOf(whichWordColorUpper, strar) >= 0 && whichWordColor.isNotEmpty()) {
            ss.setSpan(BackgroundColorSpan(color), textUpper.indexOf(whichWordColorUpper, strar), textUpper.indexOf(whichWordColorUpper, strar) + whichWordColorUpper.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            strar = textUpper.indexOf(whichWordColorUpper, strar) + whichWordColorUpper.length
        }
        textView.text = ss
    }
}