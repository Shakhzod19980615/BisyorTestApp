import androidx.appcompat.app.AlertDialog
import android.content.Context
import android.widget.Button
import android.widget.TextView
import com.example.testapp.R
import com.example.testapp.domain.model.basicResponseModel.BasicResponseModel

object ApiResponseChecker {

    fun checkApiResponse(response: BasicResponseModel, context: Context): Boolean {
        return when (response.status) {
            200 -> {
                if (response.name == "Ok") {
                    // Successful response
                    true
                } else {
                    // Handle unexpected successful status
                    showAlert(response.message,context)
                    false
                }
            }
            400, 422 -> {
                // Handle client errors
                showAlert(response.message,context)
                false
            }
            else -> {
                // Handle other status codes if needed
                showAlert("An unexpected error occurred", context)
                false
            }
        }
    }

    private fun showAlert(message: String, context: Context) {
        /*val dialogView = layoutInflater.inflate(R.layout.dialog_universal_messaging, null)
        val alertDialog  = AlertDialog.Builder(context)
            .setView(dialogView)
            ?.setCancelable(true)
            ?.create()
        val btnDialogOk: Button = dialogView.findViewById(R.id.btn_ok)
        val dialogMessage : TextView = dialogView.findViewById(R.id.text_title)*/
        val alertDialog = AlertDialog.Builder(context)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
        alertDialog.show()
    }
}
