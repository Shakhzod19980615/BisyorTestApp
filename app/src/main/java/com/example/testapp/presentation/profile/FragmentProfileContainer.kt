package com.example.testapp.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.testapp.R
import com.example.testapp.databinding.WindowProfileContainerBinding
import kotlin.properties.Delegates

class FragmentProfileContainer : Fragment(R.layout.window_profile_container) {
    private var binding : WindowProfileContainerBinding by Delegates.notNull()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WindowProfileContainerBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            binding.isLogined = false
    }
}