package uz.bisyor.corelib.common.extension

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior

fun <T: View> BottomSheetBehavior<T>.collapsed() {
    state = BottomSheetBehavior.STATE_COLLAPSED
}

fun <T: View> BottomSheetBehavior<T>.expanded() {
    state = BottomSheetBehavior.STATE_EXPANDED
}

fun <T: View> BottomSheetBehavior<T>.hidden() {
    state = BottomSheetBehavior.STATE_HIDDEN
}

fun <T: View> BottomSheetBehavior<T>.isCollapsed() = state == BottomSheetBehavior.STATE_COLLAPSED

fun <T: View> BottomSheetBehavior<T>.isExpanded() = state == BottomSheetBehavior.STATE_EXPANDED

fun <T: View> BottomSheetBehavior<T>.isHidden() = state == BottomSheetBehavior.STATE_HIDDEN