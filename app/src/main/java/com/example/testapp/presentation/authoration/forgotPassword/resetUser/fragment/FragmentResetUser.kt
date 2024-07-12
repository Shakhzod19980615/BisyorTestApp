package com.example.testapp.presentation.authoration.forgotPassword.resetUser.fragment

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
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.testapp.R
import com.example.testapp.common.Resource
import com.example.testapp.databinding.WindowResetUserBinding
import com.example.testapp.presentation.authoration.forgotPassword.confirmUserRestore.fragment.FragmentConfirmUserRestore
import com.example.testapp.presentation.authoration.forgotPassword.resetUser.viewModel.ResetUserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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

    @SuppressLint("ResourceAsColor")
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
            val login  = binding.loginEd.text.toString()
            if (login.startsWith("998")&& login.length == 12) {
                resetUserViewModel.resetUser("+$login")
                lifecycleScope.launch {
                    resetUserViewModel.resetUser.collect{result->
                        when(result){
                            is Resource.Success -> {
                                activity?.supportFragmentManager?.commit {
                                    replace<FragmentConfirmUserRestore>(
                                        containerViewId= R.id.fragment_container_view_tag,
                                        args = bundleOf("login" to "+$login")
                                    ).addToBackStack("replacement")
                                }
                            }
                            is Resource.Error ->{
                                showAlertDialog(result.message)
                            }
                            is Resource.Loading->{
                                simulateLoading()
                            }
                        }
                    }
                }
            }else{
                binding.loginTI.error = getString(R.string.enter_phone_or_email)
                binding.loginTI.boxStrokeColor = R.color.red
                binding.loginTI.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.red)
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

                binding.submitButton.text = spannableString
                binding.submitButton.isEnabled = false
            }
        } else {
            binding.submitButton.text = "Submit"
            binding.submitButton.isEnabled = true
        }
    }
}