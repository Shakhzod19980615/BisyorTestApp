package com.example.testapp.presentation.authoration.login.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.example.testapp.R
import com.example.testapp.databinding.WindowLoginBinding
import com.example.testapp.presentation.authoration.login.viewModel.LoginViewModel
import com.example.testapp.presentation.authoration.registration.fragment.FragmentRegistration
import kotlin.properties.Delegates

class FragmentLogin : Fragment(R.layout.window_login) {
    private var binding : WindowLoginBinding by Delegates.notNull()
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WindowLoginBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("ResourceAsColor", "RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
        binding.enterDesTv.text = getString(R.string.enter_descr)
        binding.loginTI.hint = getString(R.string.enter_login)
        binding.passwordTI.hint = getString(R.string.enter_password)
        binding.navigationReset.text = getString(R.string.forget_password)
        binding.haveNoAccountTV.text = getString(R.string.are_you_dont_have_account)
        binding.navigationSignup.text = getString(R.string.register)
        binding.submitButton.text = getString(R.string.log_in)
        binding.navigationSignup.setOnClickListener {
            activity?.supportFragmentManager?.commit {
                replace(R.id.fragment_container_view_tag, FragmentRegistration()).addToBackStack("goBack")
            }
        }

    }
    @SuppressLint("ResourceAsColor")
    private fun isLoginValid():Boolean{
       val login= binding.edLogin.text.toString()
        if(login.isBlank()){
            binding.loginTI.error = getString(R.string.warning_enter_phone_number)
            binding.loginTI.boxStrokeColor = R.color.red
            binding.loginTI.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.red)
            binding.loginTI.endIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.micon_profile_about)
            return false
        }
        return true
    }
}