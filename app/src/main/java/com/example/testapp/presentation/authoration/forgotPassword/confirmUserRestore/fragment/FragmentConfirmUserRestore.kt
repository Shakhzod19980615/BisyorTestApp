package com.example.testapp.presentation.authoration.forgotPassword.confirmUserRestore.fragment

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
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.testapp.R
import com.example.testapp.common.Resource
import com.example.testapp.data.request.resetUserConfirmRequest.ResetUserConfirmRequest
import com.example.testapp.data.request.verificationCode.VerificationCodeRequest
import com.example.testapp.databinding.WindowConfrimationCodeBinding
import com.example.testapp.presentation.authoration.forgotPassword.confirmRestoreUserPassword.ConfirmRestoreUserPassword
import com.example.testapp.presentation.authoration.forgotPassword.confirmUserRestore.viewModel.ConfirmUserRestoreVM
import com.example.testapp.presentation.authoration.registration.fragment.FragmentRegistration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.properties.Delegates
@AndroidEntryPoint
class FragmentConfirmUserRestore: Fragment(R.layout.window_confrimation_code) {
    private val confirmUserRestoreVM: ConfirmUserRestoreVM by viewModels()
    private var binding : WindowConfrimationCodeBinding by Delegates.notNull()
    private val codeEditors: MutableList<EditText> = ArrayList()
    private var currentPosition = 0
    private val login : String? by lazy {
        arguments?.getString("login")
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WindowConfrimationCodeBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.iconBack.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        binding.firstNumber.requestFocus()
        configureEditors()
        binding.submitButton.text = getString(R.string.enter)

        binding.submitButton.setOnClickListener {
            val code  = getCodeFromEditors()
            if (validateInput()){
                confirmUserRestoreVM.restoreUserConfirm(ResetUserConfirmRequest(login!!, code))
                lifecycleScope.launch {
                    confirmUserRestoreVM.confirmRestoreUser.collect{ verifyCodeResult->
                        when(verifyCodeResult){
                            is Resource.Success->{
                                if (verifyCodeResult.data){
                                    Toast.makeText(requireContext(), "${verifyCodeResult.data}", Toast.LENGTH_SHORT).show()
                                    activity?.supportFragmentManager?.commit {
                                        replace(R.id.fragment_container_view_tag, ConfirmRestoreUserPassword())
                                    }
                                }else{
                                    showAlertDialog(verifyCodeResult.data.toString())
                                }

                            }
                            is Resource.Error->{
                                showAlertDialog(verifyCodeResult.message)
                            }

                            is Resource.Loading ->
                                simulateLoading()
                        }
                    }
                }
               // activity?.supportFragmentManager?.popBackStack()
            }else{
                showAlertDialog(getString(R.string.alert_mismatch_code))
            }
        }
    }


    private fun configureEditors(){
        codeEditors.add(binding.firstNumber)
        codeEditors.add(binding.secondNumber)
        codeEditors.add(binding.thirdNumber)
        codeEditors.add(binding.forthNumber)
        codeEditors.add(binding.fifthNumber)

        for (i in codeEditors.indices){
            codeEditors[i].addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1 && i < codeEditors.size - 1) {
                        codeEditors[i + 1].requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
            codeEditors[i].setOnKeyListener { v, keyCode, event ->
                if (keyCode == android.view.KeyEvent.KEYCODE_DEL && event.action == android.view.KeyEvent.ACTION_DOWN) {
                    if (codeEditors[i].text.isEmpty() && i > 0) {
                        codeEditors[i - 1].requestFocus()
                    }
                }
                false
            }

        }
    }
    private fun validateInput():Boolean{
        for (i in codeEditors.indices){
            if (codeEditors[i].text.isEmpty()){
                return false
            }
        }
        return true
    }
    private fun getCodeFromEditors(): String {
        val codeBuilder = StringBuilder()
        for (editor in codeEditors) {
            codeBuilder.append(editor.text.toString())
        }
        return codeBuilder.toString()
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
}