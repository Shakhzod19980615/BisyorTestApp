/*
package uz.bisyor.corelib.common.extension

import android.graphics.Bitmap
import android.graphics.Canvas
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.view.drawToBitmap
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


fun View.toBitmap(): Bitmap {
    return this.drawToBitmap()
}

fun ImageView.loadImage(url: String) =
    Glide.with(context)
        .load(url)
        .into(this)

fun ImageView.setTint(color: Int) {
    this.drawable.colorFilter =
        BlendModeColorFilterCompat.createBlendModeColorFilterCompat(color, BlendModeCompat.SRC_ATOP)
}

var TextInputEditText.parentError: String
    get() = "someMessage"
    set(value) {
        (this.parent.parent as TextInputLayout).error = value
    }

fun View.enable() {
    this.isEnabled = true
}

fun View.disable() {
    this.isEnabled = false
}


fun EditText.isPhoneNumberValid(): Boolean {
    return Regex(PHONE_REGEX_UZ).matches("${text.replace("\\s+".toRegex(), "")}")
}

fun String.isStringMatchesPhoneNumber(): Boolean {
    return Regex(PHONE_REGEX_UZ).matches("${this.replace("\\s+".toRegex(), "")}")
}

fun takeScreenshot(decorView: View, fileName: String): File {
    val bitmap = getScreenBitmap(decorView)
    return saveBitmap(bitmap, fileName)
}

fun getScreenBitmap(decor: View): Bitmap {
    val v = decor
    val bitmap = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    v.draw(canvas)
    return bitmap
}

fun saveBitmap(bitmap: Bitmap, fileName: String): File {
    val imageFile = File(App.application.externalCacheDir.toString() + "/" + fileName + ".png")
    val fos: FileOutputStream
    try {
        fos = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
    } catch (e: FileNotFoundException) {
        Log.e("GREC", e.message, e)
    } catch (e: IOException) {
        Log.e("GREC", e.message, e)
    }
    return imageFile
}

fun getFirst2CharsFromFirst2Words(text: String?): String {
    //Removing extra spaces first
    if (text == null) return ""
    val temp = text.trim().replace("(", "").replace(")", "")

    //Only start generating chars when provided text is not empty
    if (temp.isEmpty().not()) {
        val splitName = temp.split(" ")

        val first2CharsFromFirst2Words : String

        //If text can be divided into 2 or more parts, that means text consists of 2 or more words.
        //In this case, get first letters of the first 2 split words.
        //Else (if text consists of only 1 word), just get the first letter of the word.
        first2CharsFromFirst2Words = if (splitName.size > 1 && splitName[1] != "")
            splitName[0][0] + "" + splitName[1][0]
        else
            splitName[0][0].toString()

        return first2CharsFromFirst2Words
    } else
        return ""
}

fun View.hide(){
    this.visibility = View.GONE
}
fun View.show(){
    this.visibility = View.VISIBLE
}
*/
