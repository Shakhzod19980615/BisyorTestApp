package com.example.testapp.presentation.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testapp.BaseFragment
import com.example.testapp.databinding.WindowOwnFavouritesBinding
import com.example.testapp.databinding.WindowSearchContainerBinding
import kotlin.properties.Delegates

class FragmentFavourites :BaseFragment() {
    private var binding: WindowOwnFavouritesBinding by Delegates.notNull()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WindowOwnFavouritesBinding.inflate(inflater)
        return binding.root

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}