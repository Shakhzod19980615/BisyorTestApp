package com.example.testapp.presentation.authoration.forgotPassword.confirmRestoreUserPassword.fragment

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.testapp.BaseFragment
import com.example.testapp.R
import com.example.testapp.common.Resource
import com.example.testapp.databinding.WindowUserRestoreUpdatePasswordBinding
import com.example.testapp.presentation.authoration.forgotPassword.confirmRestoreUserPassword.viewModel.ConfirmRestorePasswordVM
import com.example.testapp.presentation.profile.FragmentProfileContainer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class ConfirmRestoreUserPassword : BaseFragment() {
    private var binding : WindowUserRestoreUpdatePasswordBinding by Delegates.notNull()
    private val confirmRestorePasswordVM: ConfirmRestorePasswordVM by viewModels()
    private val login : String? by lazy {
        arguments?.getString("login")
    }
    private val code : String? by lazy {
        arguments?.getString("code")
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WindowUserRestoreUpdatePasswordBinding.inflate(inflater)
        return binding.root

    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.passwordTI.hint = getString(R.string.new_password)
        binding.confirmPasswordTI.hint = getString(R.string.confirm_password)
        binding.passwordTV.text = getString(R.string.enter_new_password)
        binding.continueButton.text = getString(R.string.text_continue)
        binding.iconBack.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        binding.edPassword.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                @SuppressLint("ResourceAsColor")
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    p0?.let {
                        val password = binding.edPassword.text.toString()
                        if (password.length < 6 || password.isEmpty()) {
                            binding.passwordTI.error = getString(R.string.warning_enter_password)
                            binding.passwordTI.boxStrokeColor = R.color.red
                            binding.passwordTI.hintTextColor =
                                ContextCompat.getColorStateList(requireContext(), R.color.red)
                        } else {
                            binding.passwordTI.error = null
                            binding.passwordTI.boxStrokeColor = R.color.ripple_color
                            binding.passwordTI.hintTextColor = ContextCompat.getColorStateList(
                                requireContext(),
                                R.color.defaultTextColor
                            )
                        }
                    }
                }

                @SuppressLint("ResourceAsColor")
                override fun afterTextChanged(p0: Editable?) {
                    binding.passwordTI.error = null
                    binding.passwordTI.boxStrokeColor = R.color.ripple_color
                    binding.passwordTI.hintTextColor =
                        ContextCompat.getColorStateList(requireContext(), R.color.defaultTextColor)
                }
            }
        )
        binding.continueButton.setOnClickListener {
            val password = binding.edPassword.text.toString()
            if (isPasswordMatches() && isPasswordValid()) {
                confirmRestorePasswordVM.resetPassword(login!!, code!!, password)
                lifecycleScope.launch {
                    confirmRestorePasswordVM.resetPassword.collect { confirmPassword ->
                        when (confirmPassword) {
                            is Resource.Success -> {
                                activity?.supportFragmentManager?.commit {
                                    replace(
                                        R.id.fragment_container_view_tag,
                                        FragmentProfileContainer()
                                    ).addToBackStack("goBack")
                                }
                            }

                            is Resource.Error -> {
                                showAlertDialog(confirmPassword.message)
                            }
                            is Resource.Loading ->{
                                simulateLoading()
                            }
                        }
                    }
                }

            }
        }
    }
    private fun simulateLoading() {
        // Show loading indicator
        showLoadingInButton(true)

        // Simulate a delay to represent loading time
        Handler(Looper.getMainLooper()).postDelayed({
            // Hide loading indicator
            showLoadingInButton(false)
        }, 3000) // 3 seconds delay
    }
    private fun showLoadingInButton(isLoading: Boolean) {
        if (isLoading) {
            val progressBarDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.circular_progress)
            progressBarDrawable?.let { drawable ->
                val wrappedDrawable: Drawable = DrawableCompat.wrap(drawable)
                DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(requireContext(), R.color.white))
                wrappedDrawable.setBounds(0, 0, 60, 60)

                val spannableString = SpannableString("  Loading...")
                val imageSpan = ImageSpan(wrappedDrawable, ImageSpan.ALIGN_BOTTOM)
                spannableString.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                binding.continueButton.text = spannableString
                binding.continueButton.isEnabled = false
            }
        } else {
            //binding.continueButton.text = "Submit"
            binding.continueButton.isEnabled = true
        }
    }
    fun showAlertDialog(message: String) {
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
   private fun isPasswordMatches(): Boolean {
        val password = binding.edPassword.text.toString()
        val confirmPassword = binding.edConfirmPassword.text.toString()
        return if(password == confirmPassword){
            binding.confirmPasswordTI.error = null
            binding.confirmPasswordTI.boxStrokeColor = R.color.ripple_color
            binding.confirmPasswordTI.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.defaultTextColor)
            true
        }else{
            binding.confirmPasswordTI.error = getString(R.string.warning_mismatch_passwords)
            binding.confirmPasswordTI.boxStrokeColor = R.color.red
            binding.confirmPasswordTI.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.red)
            false
        }
    }
    @SuppressLint("ResourceAsColor")
    private fun isPasswordValid(): Boolean {
        val password = binding.edPassword.text.toString()
        return if (password.length < 6 || password.isEmpty()) {
            binding.passwordTI.error = getString(R.string.warning_enter_password)
            binding.passwordTI.boxStrokeColor = R.color.red
            binding.passwordTI.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.red)
            false
        } else {
            binding.passwordTI.error = null
            binding.passwordTI.boxStrokeColor = R.color.ripple_color
            binding.passwordTI.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.defaultTextColor)
            true
        }
    }
}
