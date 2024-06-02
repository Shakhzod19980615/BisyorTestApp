package com.example.testapp.presentation.authoration.verificationCode

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.testapp.R
import com.example.testapp.databinding.WindowConfrimationCodeBinding
import com.example.testapp.databinding.WindowRegistrationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class FragmentVerificationCode : Fragment(R.layout.window_confrimation_code){
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
}