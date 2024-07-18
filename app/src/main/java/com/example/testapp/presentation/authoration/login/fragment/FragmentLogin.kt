package com.example.testapp.presentation.authoration.login.fragment

import android.annotation.SuppressLint
import android.content.Intent
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
import com.example.testapp.R
import com.example.testapp.common.Resource
import com.example.testapp.databinding.WindowLoginBinding
import com.example.testapp.presentation.authoration.forgotPassword.resetUser.fragment.FragmentResetUser
import com.example.testapp.presentation.authoration.login.viewModel.LoginViewModel
import com.example.testapp.presentation.authoration.registration.fragment.FragmentRegistration
import com.example.testapp.presentation.home.fragment.FragmentHome
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.properties.Delegates
@AndroidEntryPoint
class FragmentLogin : Fragment(R.layout.window_login) {
    private var binding : WindowLoginBinding by Delegates.notNull()
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001
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
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        binding.enterDesTv.text = getString(R.string.enter_descr)
        binding.loginTI.hint = getString(R.string.enter_login)
        binding.passwordTI.hint = getString(R.string.enter_password)
        binding.navigationReset.text = getString(R.string.forget_password)
        binding.haveNoAccountTV.text = getString(R.string.are_you_dont_have_account)
        binding.navigationSignup.text = getString(R.string.register)
        binding.submitButton.text = getString(R.string.log_in)
        binding.loginTI.error = null
        binding.loginTI.boxStrokeColor = R.color.ripple_color
        binding.loginTI.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.colorSecondaryDark)
        binding.loginTI.endIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.vicon_user_avatar)
        binding.passwordTI.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
        binding.navigationReset.setOnClickListener {
            activity?.supportFragmentManager?.commit {
                replace(R.id.fragment_container_view_tag, FragmentResetUser()).addToBackStack("goBack")
            }
        }
        binding.navigationSignup.setOnClickListener {
            activity?.supportFragmentManager?.commit {
                replace(R.id.fragment_container_view_tag, FragmentRegistration()).addToBackStack("goBack")
            }
        }
        binding.edLogin.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    p0?.let {
                        if (it.isNotEmpty()) {
                            binding.loginTI.error = null
                            binding.loginTI.boxStrokeColor = R.color.ripple_color
                            binding.loginTI.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.colorSecondaryDark)
                            binding.loginTI.endIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.vicon_user_avatar)
                        }else{
                            binding.loginTI.error = getString(R.string.enter_login)
                            binding.loginTI.boxStrokeColor = R.color.red
                            binding.loginTI.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.red)
                            binding.loginTI.endIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.micon_profile_about)
                        }

                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            }
        )
        binding.edPassword.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    p0?.let {
                        if (it.isNotEmpty()) {
                            binding.passwordTI.error = null
                            binding.passwordTI.boxStrokeColor = R.color.ripple_color
                            binding.passwordTI.hintTextColor = ContextCompat.getColorStateList(
                                requireContext(),
                                R.color.colorSecondaryDark
                            )
                            binding.passwordTI.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
                           // binding.loginTI.endIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.vicon_user_avatar)

                        } else {
                            binding.passwordTI.error = getString(R.string.enter_password)
                            binding.passwordTI.boxStrokeColor = R.color.red
                            binding.passwordTI.hintTextColor =
                                ContextCompat.getColorStateList(requireContext(), R.color.red)
                            binding.passwordTI.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
                            /*binding.passwordTI.endIconDrawable = ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.micon_profile_about
                            )*/
                        }

                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
        binding.submitButton.setOnClickListener {
            val login = binding.edLogin.text.toString()
            val password = binding.edPassword.text.toString()
            if(isLoginValid()&&isPasswordValid()){
                loginViewModel.signIn(login, password,"ru")
                lifecycleScope.launch {
                    loginViewModel.signIn.collect{loginResult->
                        when(loginResult){
                            is Resource.Success->{
                                activity?.supportFragmentManager?.commit {
                                    replace(R.id.fragment_container_view_tag,
                                        FragmentHome()
                                    ).addToBackStack("goBack")
                                }
                            }
                            is Resource.Error->{
                                showAlertDialog(loginResult.message)
                            }

                            is Resource.Loading ->{
                                simulateLoading()
                            }
                        }
                    }
                }
            }
        }
        binding.clickerGoogle.setOnClickListener {
            //loginViewModel.registerWithSocial()
            signInWithGoogle()
        }

    }
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val id = account.id
            val name = account?.displayName
            val email = account?.email
            if (id != null) {
                sendUserDetailsToServer(id,name, email,"")
            }

        } catch (e: ApiException) {
            showAlertDialog("Sign-in failed: ${e.message}")
        }
    }
    private fun sendUserDetailsToServer(id: String, name: String?, email: String?,phone:String) {
        loginViewModel.registerWithSocial(id, name, email,phone)
        lifecycleScope.launch {
            loginViewModel.registerWithSocial.collect { loginWithGoogle ->
                when (loginWithGoogle) {
                    is Resource.Success -> {
                        activity?.supportFragmentManager?.commit {
                            replace(R.id.fragment_container_view_tag, FragmentHome()).addToBackStack("goBack")
                        }
                    }
                    is Resource.Error -> {
                        showAlertDialog(loginWithGoogle.message)
                    }
                    is Resource.Loading -> {
                        simulateLoading()
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
    @SuppressLint("ResourceAsColor")
    private fun isLoginValid():Boolean{
       val login= binding.edLogin.text.toString()
        var isValid = false
        loginViewModel.validatePhoneNumber(login,requireContext())
        lifecycleScope.launch {
            loginViewModel.phoneNumberValidation.collect{isLoginValid->
              isValid =  if(isLoginValid.first){
                    binding.loginTI.error = null
                    binding.loginTI.boxStrokeColor = R.color.ripple_color
                    binding.loginTI.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.colorSecondaryDark)
                    binding.loginTI.endIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.vicon_user_avatar)
                true
                }else{
                    binding.loginTI.error = getString(R.string.warning_enter_phone_number)
                    binding.loginTI.boxStrokeColor = R.color.red
                    binding.loginTI.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.red)
                    binding.loginTI.endIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.micon_profile_about)
                false
                }
            }
        }

        return isValid
    }
    @SuppressLint("ResourceAsColor")
    private fun isPasswordValid():Boolean{
        val password= binding.edPassword.text.toString()
        var isValid = false
        loginViewModel.validatePassword(password)
        lifecycleScope.launch {
            loginViewModel.passwordValidation.collect{isPasswordValid->
                isValid =  if(isPasswordValid.first){
                    binding.passwordTI.error = null
                    binding.passwordTI.boxStrokeColor = R.color.black
                    binding.passwordTI.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.black)
                    //binding.passwordTI.endIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.micon_profile_about)
                    true
                }else{
                    binding.passwordTI.error = getString(R.string.warning_enter_password)
                    binding.passwordTI.boxStrokeColor = R.color.red
                    binding.passwordTI.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.red)
                    //binding.passwordTI.endIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.micon_profile_about)
                    false
                }
            }
        }
        return isValid
    }
}