package com.example.testapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.testapp.databinding.ActivityMainBinding
import com.example.testapp.presentation.home.fragment.FragmentHome
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.fragmentContainerViewTag
        if (savedInstanceState == null) {
            replaceFragment(FragmentHome())
        }
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(FragmentHome())
                R.id.search -> replaceFragment(FragmentHome())
                R.id.create_item -> replaceFragment(FragmentHome())
                R.id.chat -> replaceFragment(FragmentHome())
                R.id.profile -> replaceFragment(FragmentHome())
                else -> Unit
            }
            true
        }
    }
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.commit {
            replace(R.id.fragment_container_view_tag, fragment)
        }
    }
}

