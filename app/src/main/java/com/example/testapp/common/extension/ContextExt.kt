package uz.bisyor.corelib.common.extension

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import com.example.testapp.common.Constants
import java.util.regex.Pattern

fun Fragment.emailMatches(email: String): Boolean {
    val pattern = Pattern.compile(Constants.EMAIL_REGEX)
    return pattern.matcher(email).matches()
}

fun Fragment.call(phone: String) {
    startActivity(Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phone")
    })
}