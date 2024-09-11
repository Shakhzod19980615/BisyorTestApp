
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import com.example.testapp.R

class AlertDialogHelper {

    companion object {
        fun showAlertDialog(context: Context, message: String) {
            // Inflate the custom dialog layout
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_universal_messaging, null)

            // Create an AlertDialog and set the custom view
            val alertDialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .setCancelable(true)
                .create()

            // Find the dialog's UI elements
            val btnDialogOk: Button = dialogView.findViewById(R.id.btn_ok)
            val dialogMessage: TextView = dialogView.findViewById(R.id.text_title)

            // Set the message
            dialogMessage.text = message

            // Handle the OK button click
            btnDialogOk.setOnClickListener {
                alertDialog.dismiss()  // Dismiss the dialog on button click
            }

            // Show the AlertDialog
            alertDialog.show()
        }
    }
}
