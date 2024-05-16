package com.example.testapp.presentation.authoration.registration

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.testapp.R
import com.example.testapp.databinding.WindowRegistrationBinding
import com.example.testapp.presentation.home.viewModel.authoration.SignUpViewModel
import kotlin.properties.Delegates
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


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

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.submitButton.setOnClickListener {
            val login = binding.edPhone.text.toString()
            val password = binding.passwordEd.text.toString()
            val confirmPassword = binding.passwordConfirmEd.text.toString()

            if (password != confirmPassword) {
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                signUpViewModel.phoneNumberValidation.collect { isValid ->
                    if (isValid == false) {
                        // Display error message to the user
                        Toast.makeText(context, "Invalid phone number", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            signUpViewModel.signUp(login, password)
        }
    }
}