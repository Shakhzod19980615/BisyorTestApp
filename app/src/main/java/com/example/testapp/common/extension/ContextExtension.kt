package uz.bisyor.corelib.common.extension

import android.content.Context
import android.util.DisplayMetrics
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer


fun Context.dpToPx(dp: Int): Int {
    val displayMetrics = resources.displayMetrics
    return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
}

inline fun <T> LiveData<T>.subscribe(
    owner: LifecycleOwner,
    crossinline onDataReceived: (T) -> Unit
) =
    observe(owner, { onDataReceived(it) })

object FieldExtensions {

    fun Int?.mIsNullOrEmpty(): Boolean =
        this == null || this == -1

}