package com.example.testapp.presentation.authoration.registration.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresExtension
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.testapp.R
import com.example.testapp.databinding.WindowRegistrationBinding
import com.example.testapp.presentation.authoration.registration.viewModel.SignUpViewModel
import kotlin.properties.Delegates
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentRegistration: Fragment(R.layout.window_registration){
    private  val signUpViewModel: SignUpViewModel by viewModels()
    private var binding : WindowRegistrationBinding by Delegates.notNull()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = WindowRegistrationBinding.inflate(inflater)
        return binding.root

    }

    @SuppressLint("SetTextI18n")
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dialogView = layoutInflater.inflate(R.layout.dialog_universal_messaging, null)
        val alertDialog  = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            ?.setCancelable(true)
            ?.create()
        val btnDialogOk: Button = dialogView.findViewById(R.id.btn_ok)
        val dialogMessage : TextView = dialogView.findViewById(R.id.text_title)
        binding.submitButton.setOnClickListener {
            val login = "+998"+ binding.edPhone.text.toString()
            val password = binding.passwordEd.text.toString()
            val confirmPassword = binding.passwordConfirmEd.text.toString()

            if (password != confirmPassword) {
                dialogMessage.text = "Passwords do not match"
                btnDialogOk.setOnClickListener {
                    // Handle button click
                    alertDialog?.dismiss()  // Dismiss the dialog on button click
                }
                alertDialog?.show()
                return@setOnClickListener
            }
            if(!binding.checkbox.isChecked){
                dialogMessage.text = getString(R.string.alert_confirm_policy)
                btnDialogOk.setOnClickListener {
                    // Handle button click
                    alertDialog?.dismiss()  // Dismiss the dialog on button click
                }
                alertDialog?.show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                signUpViewModel.phoneNumberValidation.collect { phoneValidationResult ->
                    val (isValid,message) = phoneValidationResult
                    if (!isValid ) {
                        // Display error message to the user
                        dialogMessage.text = message
                        btnDialogOk.setOnClickListener {
                            // Handle button click
                            alertDialog?.dismiss()  // Dismiss the dialog on button click
                        }
                        alertDialog?.show()
                        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }

            }
            lifecycleScope.launch {
                signUpViewModel.passwordValidation.collect { validationResult ->
                    val (isValid, message) = validationResult
                    if (!isValid) {
                        // Display error message to the user
                        dialogMessage.text = message
                        btnDialogOk.setOnClickListener {
                            // Handle button click
                            alertDialog?.dismiss()  // Dismiss the dialog on button click
                        }
                        alertDialog?.show()
                        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            signUpViewModel.signUp(login, password)
        }
    }
}