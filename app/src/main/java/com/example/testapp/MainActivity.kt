package com.example.testapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.testapp.databinding.ActivityMainBinding
import com.example.testapp.presentation.home.fragment.HomeFragment
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
            replaceFragment(HomeFragment())
        }
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.search -> replaceFragment(HomeFragment())
                R.id.create_item -> replaceFragment(HomeFragment())
                R.id.chat -> replaceFragment(HomeFragment())
                R.id.profile -> replaceFragment(HomeFragment())
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

