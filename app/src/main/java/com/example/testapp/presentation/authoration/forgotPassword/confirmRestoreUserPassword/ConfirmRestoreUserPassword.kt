package com.example.testapp.presentation.authoration.forgotPassword.confirmRestoreUserPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.testapp.R
import com.example.testapp.databinding.WindowConfrimationCodeBinding
import com.example.testapp.databinding.WindowUserRestoreUpdatePasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class ConfirmRestoreUserPassword : Fragment(R.layout.window_user_restore_update_password) {
    private var binding : WindowUserRestoreUpdatePasswordBinding by Delegates.notNull()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WindowUserRestoreUpdatePasswordBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}