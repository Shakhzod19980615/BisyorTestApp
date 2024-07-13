package com.example.testapp.presentation.authoration.registration.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.example.testapp.R
import com.example.testapp.databinding.WindowRegistrationBinding
import kotlin.properties.Delegates
import androidx.lifecycle.lifecycleScope
import com.example.testapp.R.color
import com.example.testapp.common.Resource
import com.example.testapp.presentation.authoration.forgotPassword.confirmUserRestore.fragment.FragmentConfirmUserRestore
import com.example.testapp.presentation.authoration.registration.viewModel.SignUpViewModel
import com.example.testapp.presentation.authoration.verificationCode.fragment.FragmentVerificationCode
import com.google.android.material.textfield.TextInputLayout
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
        binding.phoneTi.hint = getString(R.string.phone_number)
        binding.edPhone.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
        binding.passwordTI.hint = getString(R.string.password)
        binding.passwordEd.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
        binding.checkBoxText.text = getString(R.string.approve_privacy_policy)
        binding.passwordConfirmTi.hint = getString(R.string.confirm_password)
        binding.passwordConfirmEd.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
        binding.edPhone.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                @SuppressLint("ResourceAsColor")
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    s?.let {
                        if (it.length == 9) {
                            binding.phoneTi.error = null
                            binding.phoneTi.boxStrokeColor = R.color.ripple_color
                            binding.phoneTi.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.colorSecondaryDark)
                        }else{
                            binding.phoneTi.error = getString(R.string.warning_enter_phone_number)
                            binding.phoneTi.boxStrokeColor = R.color.red
                            binding.phoneTi.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.red)
                        }
                        if (it.isNotEmpty()) {
                            binding.phoneTi.error = getString(R.string.warning_enter_phone_number)
                            binding.phoneTi.boxStrokeColor = R.color.ripple_color
                            binding.phoneTi.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.colorSecondaryDark)
                        }else{
                            binding.phoneTi.boxStrokeColor = R.color.red
                            binding.phoneTi.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.red)
                        }

                    }
                }
                override fun afterTextChanged(s: Editable?) {
                    binding.phoneTi.error = null
                }
            }
        )
        binding.passwordEd.addTextChangedListener(

            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                @SuppressLint("ResourceAsColor")
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    s?.let {
                        val password = binding.passwordEd.text.toString()
                        if(password.length < 6) {
                            binding.passwordTI.error = getString(R.string.warning_enter_password)
                            binding.passwordTI.boxStrokeColor = R.color.red
                            binding.passwordTI.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.red)

                        }else{
                            binding.passwordTI.error = null
                            binding.passwordTI.boxStrokeColor = R.color.ripple_color
                            binding.passwordTI.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.colorSecondaryDark)
                            binding.passwordTI.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
                        }
                        if (it.isNotEmpty()) {
                            binding.passwordTI.error = null
                            binding.passwordTI.boxStrokeColor = R.color.ripple_color
                            binding.passwordTI.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.colorSecondaryDark)
                            binding.passwordTI.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
                        }
                        else{
                            binding.passwordTI.error = getString(R.string.warning_enter_password)
                            binding.passwordTI.boxStrokeColor = R.color.red
                            binding.passwordTI.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.red)
                        }
                    }
                }
                override fun afterTextChanged(s: Editable?) {
                    binding.passwordTI.error = null
                }
            }
        )
        binding.passwordConfirmEd.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                @SuppressLint("ResourceAsColor")
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    s?.let {
                        if (it.isNotEmpty()) {
                            binding.passwordConfirmTi.error = null
                            binding.passwordConfirmTi.boxStrokeColor = R.color.ripple_color
                            binding.passwordConfirmTi.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.colorSecondaryDark)
                            binding.passwordConfirmTi.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
                        }else{
                            binding.passwordConfirmTi.error = getString(R.string.warning_enter_password)
                            binding.passwordConfirmTi.boxStrokeColor = R.color.red
                            binding.passwordConfirmTi.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.red)
                        }
                    }
                }
                override fun afterTextChanged(s: Editable?) {
                    binding.passwordConfirmTi.error = null
                }
            }
        )

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
                                    setReorderingAllowed(true)
                                    addToBackStack(null)
                                    replace<FragmentVerificationCode>(
                                        containerViewId= R.id.fragment_container_view_tag,
                                        args = bundleOf("login" to login)
                                    ).addToBackStack("replacement")
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
    @SuppressLint("ResourceAsColor")
    private fun isPasswordsMatch():Boolean{
        val password = binding.passwordEd.text.toString()
        val confirmPassword = binding.passwordConfirmEd.text.toString()
        return if(password == confirmPassword){
            binding.passwordConfirmTi.error = null
            binding.passwordConfirmTi.boxStrokeColor = R.color.ripple_color
            binding.passwordConfirmTi.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.defaultTextColor)
            true
        }else{
            binding.passwordConfirmTi.error = getString(R.string.warning_mismatch_passwords)
            binding.passwordConfirmTi.boxStrokeColor = R.color.red
            binding.passwordConfirmTi.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.red)
           // showAlertDialog("Passwords do not match")
            false
        }
    }
    @SuppressLint("ResourceAsColor")
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
                    binding.phoneTi.error = getString(R.string.warning_enter_phone_number)
                    binding.phoneTi.boxStrokeColor = R.color.red
                    binding.phoneTi.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.red)
                   // phoneValidationResult.second?.let { showAlertDialog(it) }
                    false
                } else {
                    binding.phoneTi.error = null
                    binding.phoneTi.boxStrokeColor = R.color.ripple_color
                    binding.phoneTi.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.defaultTextColor)
                    true
                }
            }
        }

        return isValid
    }
    @SuppressLint("ResourceAsColor")
    private fun isLoginValid():Boolean{
        val login = binding.passwordEd.text.toString()
        var isValid = false
        signUpViewModel.validatePassword(login)
        lifecycleScope.launch {
            signUpViewModel.passwordValidation.collect { loginValidationResult ->
                isValid = if (!loginValidationResult.first&& login.length < 6) {
                    // Display error message to the user
                    //loginValidationResult.second?.let { showAlertDialog(it) }
                    binding.passwordTI.error = getString(R.string.warning_enter_password)
                    binding.passwordTI.boxStrokeColor = R.color.red
                    binding.passwordTI.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.red)
                    false
                } else {
                    binding.passwordTI.error = null
                    binding.passwordTI.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
                    binding.passwordTI.boxStrokeColor = R.color.ripple_color
                    binding.passwordTI.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.defaultTextColor)
                    true
                }
            }
        }
        return isValid
    }
    private fun isCheckBoxChecked():Boolean{
        return if(binding.checkbox.isChecked){
            true
        }else{
            showAlertDialog( getString(R.string.alert_confirm_policy))
            false
        }
    }
}