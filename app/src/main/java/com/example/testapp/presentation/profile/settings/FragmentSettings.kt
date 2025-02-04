package com.example.testapp.presentation.profile.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.testapp.R
import com.example.testapp.databinding.WindowSettingsBinding
import kotlin.properties.Delegates

class FragmentSettings: Fragment(R.layout.window_settings) {
    private  var binding: WindowSettingsBinding by Delegates.notNull()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WindowSettingsBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
    }
}