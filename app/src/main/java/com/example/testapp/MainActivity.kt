package com.example.testapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.testapp.databinding.ActivityMainBinding
import com.example.testapp.presentation.announcementDetail.fragment.FragmentAnnouncementDetail
import com.example.testapp.presentation.home.fragment.FragmentHome
import com.example.testapp.presentation.profile.FragmentProfileContainer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.bottomNav.itemIconTintList = null
        setContentView(binding.root)
        binding.fragmentContainerViewTag
        if (savedInstanceState == null) {
            replaceFragment(FragmentHome())
        }
        binding.bottomNav.setOnItemSelectedListener {
            binding.bottomNav.itemIconTintList = null
            when (it.itemId) {
                R.id.home -> replaceFragment(FragmentHome())
                R.id.search -> replaceFragment(FragmentAnnouncementDetail())
                R.id.create_item -> replaceFragment(FragmentAnnouncementDetail())
                R.id.chat -> replaceFragment(FragmentHome())
                R.id.profile -> replaceFragment(FragmentProfileContainer())
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

