package com.example.testapp.presentation.authoration.forgotPassword.resetUser.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.testapp.R
import com.example.testapp.databinding.WindowResetUserBinding
import com.example.testapp.presentation.authoration.forgotPassword.resetUser.viewModel.ResetUserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class FragmentResetUser: Fragment(R.layout.window_reset_user) {
    private var binding : WindowResetUserBinding by Delegates.notNull()
    private val resetUserViewModel: ResetUserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WindowResetUserBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.iconBack.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        binding.logo.text = getString(R.string.restore_password)
        binding.reminderText.text = getString(R.string.enter_phone_or_email)
        binding.loginEd.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
        binding.loginTI.hint = getString(R.string.phone_or_email)
        if (binding.loginEd.text.toString().isEmpty()) {
            binding.submitButton.isEnabled = false
            binding.submitButton.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.grey)
        }
        binding.submitButton.background = ContextCompat.getDrawable(requireContext(), R.drawable.button_state_pressed)
        binding.loginEd.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                @SuppressLint("ResourceAsColor")
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    p0?.let {
                        if (it.isNotEmpty()) {
                            binding.loginTI.error = null
                            binding.loginTI.boxStrokeColor = R.color.ripple_color
                            binding.loginTI.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.colorSecondaryDark)
                            binding.submitButton.isEnabled = true
                            binding.submitButton.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.buttonColor)
                        } else {
                            binding.loginTI.error = getString(R.string.enter_login)
                            binding.loginTI.boxStrokeColor = R.color.red
                            binding.loginTI.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.red)
                            binding.submitButton.isEnabled = false
                            binding.submitButton.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.grey)
                        }
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            }
        )
        binding.submitButton.setOnClickListener {
        }
    }
}