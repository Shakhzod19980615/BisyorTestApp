package com.example.testapp.presentation.searching

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.testapp.R
import com.example.testapp.databinding.WindowFilterBinding
import com.example.testapp.databinding.WindowUniversalListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class FragmentFilter(): Fragment(R.layout.window_filter) {
    private var binding : WindowFilterBinding by Delegates.notNull()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WindowFilterBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}