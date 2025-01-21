package uz.bisyor.corelib.common.extension

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.example.testapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder


fun Context.showAlertDialog(
    message: CharSequence,
    title: CharSequence? = null,
    buttonAction: DialogInterface.OnClickListener? = null,
    dismissListener: DialogInterface.OnDismissListener? = null,
    init: (MaterialAlertDialogBuilder.() -> Unit)? = null
): AlertDialog =
    MaterialAlertDialogBuilder(this).apply {
        this.setMessage(message)
        if (title != null)
            this.setTitle(title)

        setPositiveButton(resources.getString(R.string.accept)) { dialog, w ->
            dialog.dismiss()
            buttonAction?.onClick(dialog, w)
        }
        dismissListener?.let { setOnDismissListener(it) }


        if (init != null) init()
    }.show()


fun Context.showCancelableAlertDialog(
    message: CharSequence,
    title: CharSequence? = null,
    positiveActionListener: DialogInterface.OnClickListener? = null,
    negativeActionListener: DialogInterface.OnClickListener? = null,
    init: (MaterialAlertDialogBuilder.() -> Unit)? = null
): AlertDialog =
    MaterialAlertDialogBuilder(this).apply {
        this.setMessage(message)
        if (title != null) {
            this.setTitle(title)
        }

        //It is negative button
        setNegativeButton(resources.getString(R.string.decline)) { dialog, which ->
            dialog.dismiss()
            negativeActionListener?.onClick(dialog, which)
        }

        //It is positive button
        setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
            dialog.dismiss()
            positiveActionListener?.onClick(dialog, which)
        }


        if (init != null) init()
    }.show()


fun Context.showSelectorDialog(
    list: Array<String>,
    title: CharSequence? = null,
    itemSelectedListener: (DialogInterface, Int) -> Unit, // Correct type for listener
    init: MaterialAlertDialogBuilder.() -> Unit = {} // Default empty block for customization
): AlertDialog {
    return MaterialAlertDialogBuilder(this).apply {
        // Set the title if provided
        if (title != null) {
            setTitle(title)
        }

        // Set items for the dialog with the itemSelectedListener
        setItems(list) { dialog, which ->
            itemSelectedListener(dialog, which)  // Calling the listener when an item is selected
        }

        // Apply any additional customizations if provided in init
        init() // Applying the customization block
    }.show()
}





fun Context.showSingleItemPickerDialog(
    list: Array<String>,
    title: CharSequence? = null,
    checkedItemIndex: Int = 0,
    itemSelectedListener: DialogInterface.OnClickListener,
    init: (MaterialAlertDialogBuilder.() -> Unit)? = null
): AlertDialog =
    MaterialAlertDialogBuilder(this).apply {
        if (title != null) {
            this.setTitle(title)
        }

        setSingleChoiceItems(list, checkedItemIndex, itemSelectedListener)

        if (init != null) init()
    }.show()


fun Context.showMultiItemPickerDialog(
    list: Array<String>,
    title: CharSequence? = null,
    checkedItems: BooleanArray? = null,
    multiItemSelectedListener: DialogInterface.OnClickListener,
    init: (MaterialAlertDialogBuilder.() -> Unit)? = null
): AlertDialog =
    MaterialAlertDialogBuilder(this).apply {
        if (title != null) {
            this.setTitle(title)
        }

        setMultiChoiceItems(list, checkedItems, null)
        setPositiveButton(R.string.text_continue, multiItemSelectedListener)

        if (init != null) init()
    }.show()
