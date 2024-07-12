package com.example.testapp.presentation.authoration.verificationCode.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.testapp.R
import com.example.testapp.common.Resource
import com.example.testapp.data.request.verificationCode.VerificationCodeRequest
import com.example.testapp.databinding.WindowConfrimationCodeBinding
import com.example.testapp.presentation.authoration.registration.fragment.FragmentRegistration
import com.example.testapp.presentation.authoration.verificationCode.viewModel.VerifyCodeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class FragmentVerificationCode : Fragment(R.layout.window_confrimation_code){
    private  val verificationCodeViewModel: VerifyCodeViewModel by viewModels()
    private var binding : WindowConfrimationCodeBinding by Delegates.notNull()
    private val codeEditors: MutableList<EditText> = ArrayList()
    private var currentPosition = 0
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
            val login = "+998900278575"
            if (validateInput()){
                verificationCodeViewModel.verifyCode(VerificationCodeRequest(login, code))
                lifecycleScope.launch {
                    verificationCodeViewModel.verifyCode.collect{verifyCodeResult->
                        when(verifyCodeResult){
                            is Resource.Success->{
                                activity?.supportFragmentManager?.commit {
                                    setReorderingAllowed(true)
                                    addToBackStack(null)
                                    replace(R.id.fragment_container_view_tag, FragmentRegistration())
                                }
                            }
                            is Resource.Error->{
                                showAlertDialog(verifyCodeResult.message)
                            }

                            is Resource.Loading -> Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                activity?.supportFragmentManager?.popBackStack()
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
            codeEditors[i].addTextChangedListener(object:TextWatcher{
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