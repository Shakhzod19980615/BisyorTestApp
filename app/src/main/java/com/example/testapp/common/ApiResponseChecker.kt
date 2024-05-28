import androidx.appcompat.app.AlertDialog
import android.content.Context
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.testapp.R
import com.example.testapp.domain.model.basicResponseModel.BasicResponseModel

object ApiResponseChecker {
    private const val TAG = "ApiResponseChecker"
    fun checkApiResponse(response: BasicResponseModel, context: Context): Boolean {
        Log.d(TAG, "API Response: $response")
        Log.d(TAG, "Response Status: ${response.status}")
        Log.d(TAG, "Response Name: ${response.name}")
        Log.d(TAG, "Response Message: ${response.message}")
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
                Log.d("ApiResponseChecker", response.message)
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
        val alertDialog = AlertDialog.Builder(context)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
        alertDialog.show()
    }
}
