package com.example.testapp.presentation.authoration.registration.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.example.testapp.R
import com.example.testapp.databinding.WindowRegistrationBinding
import kotlin.properties.Delegates
import androidx.lifecycle.lifecycleScope
import com.example.testapp.common.Resource
import com.example.testapp.presentation.authoration.registration.viewModel.SignUpViewModel
import com.example.testapp.presentation.authoration.verificationCode.fragment.FragmentVerificationCode
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
    ): View {
        binding = WindowRegistrationBinding.inflate(inflater)
        return binding.root

    }

    @SuppressLint("SetTextI18n")
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.iconBack.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        binding.submitButton.setOnClickListener {
            val login = "+998"+ binding.edPhone.text.toString()
            val password = binding.passwordEd.text.toString()

            if(isPasswordsMatch()&&isPhoneNumberValid()&&isLoginValid()&&isCheckBoxChecked()){
                signUpViewModel.signUp(login, password,requireContext())
                lifecycleScope.launch {
                    signUpViewModel.signUp.collect{signUpResult->
                        when(signUpResult){
                            is Resource.Success->{
                                activity?.supportFragmentManager?.commit {
                                    replace(R.id.fragment_container_view_tag,
                                        FragmentVerificationCode()
                                    ).addToBackStack("goBack")
                                }
                            }
                            is Resource.Error->{
                                showAlertDialog(signUpResult.message)
                            }

                            is Resource.Loading -> Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }


        }
    }
    private fun showAlertDialog(message: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_universal_messaging, null)
        val alertDialog  = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            ?.setCancelable(true)
            ?.create()
        val btnDialogOk: Button = dialogView.findViewById(R.id.btn_ok)
        val dialogMessage : TextView = dialogView.findViewById(R.id.text_title)
        dialogMessage.text = message
        btnDialogOk.setOnClickListener {
            // Handle button click
            alertDialog?.dismiss()  // Dismiss the dialog on button click
        }
        alertDialog?.show()
    }
    private fun isPasswordsMatch():Boolean{
        val password = binding.passwordEd.text.toString()
        val confirmPassword = binding.passwordConfirmEd.text.toString()
        return if(password == confirmPassword){
            true
        }else{
            showAlertDialog("Passwords do not match")
            false
        }
    }
    private fun isCheckBoxChecked():Boolean{
        return if(binding.checkbox.isChecked){
            true
        }else{
            showAlertDialog( getString(R.string.alert_confirm_policy))
            false
        }
    }
    private fun isPhoneNumberValid(): Boolean {
        val phoneNumber = binding.edPhone.text.toString()
        var isValid = false

        // Trigger validation in the ViewModel
        signUpViewModel.validatePhoneNumber(phoneNumber)

        // Collect the validation result
        lifecycleScope.launch {
            signUpViewModel.phoneNumberValidation.collect { phoneValidationResult ->
                isValid = if (!phoneValidationResult.first) {
                    // Display error message to the user
                    phoneValidationResult.second?.let { showAlertDialog(it) }
                    false
                } else {
                    true
                }
            }
        }

        return isValid
    }
    private fun isLoginValid():Boolean{
        val login = binding.passwordEd.text.toString()
        var isValid = false
        signUpViewModel.validatePassword(login)
        lifecycleScope.launch {
            signUpViewModel.passwordValidation.collect { loginValidationResult ->
                isValid = if (!loginValidationResult.first) {
                    // Display error message to the user
                    loginValidationResult.second?.let { showAlertDialog(it) }
                    false
                } else {
                    true
                }
            }
        }
        return isValid
    }
}