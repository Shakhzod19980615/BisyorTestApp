package com.example.testapp.presentation.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.testapp.R
import com.example.testapp.common.MySettings
import com.example.testapp.databinding.WindowProfileContainerBinding
import com.example.testapp.presentation.authoration.login.fragment.FragmentLogin
import com.example.testapp.presentation.favourite.FragmentFavourites
import kotlin.properties.Delegates

class FragmentProfileContainer : Fragment(R.layout.window_profile_container) {
    private var binding : WindowProfileContainerBinding by Delegates.notNull()
    var isLogined by Delegates.notNull<Boolean>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WindowProfileContainerBinding.inflate(inflater)
        return binding.root

    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            isLogined = false
            binding.buttonLogin.setOnClickListener {
                activity?.supportFragmentManager?.commit {
                    replace(R.id.fragment_container_view_tag, FragmentLogin()).addToBackStack("goBack")
                }
            }
            if(MySettings.getToken()!!.isNotEmpty()){
                binding.avatarLl.visibility = View.GONE
                binding.balanceLl.visibility = View.VISIBLE
                binding.balanceView.visibility = View.VISIBLE
                binding.clickerBalance.visibility = View.VISIBLE
                binding.clickerNewStore.visibility = View.VISIBLE
                binding.btnLogoutLl.visibility = View.VISIBLE
                binding.userBalanceView.visibility = View.VISIBLE
                binding.clickerFillBalance.text = getString(R.string.add_balance)
                binding.logoutTv.text = getString(R.string.logout)
                binding.text.text = getString(R.string.your_balance)
            }else{
                binding.avatarLl.visibility = View.VISIBLE
                binding.balanceLl.visibility = View.GONE
                binding.balanceView.visibility = View.GONE
                binding.clickerBalance.visibility = View.GONE
                binding.clickerNewStore.visibility = View.GONE
                binding.btnLogoutLl.visibility = View.GONE
                binding.userBalanceView.visibility = View.GONE
                binding.text.text = getString(R.string.your_balance)

            }
            /*binding.text.text = getString(R.string.your_balance)
            binding.clickerFillBalance.text = getString(R.string.add_balance)*/
            binding.loginForFunctionsTv.text = getString(R.string.login_for_functions)
            binding.buttonLogin.text = getString(R.string.log_in)
            binding.profileTv.text = getString(R.string.profile)
            binding.userAnnouncementTv.text =getString(R.string.my_announcements)
            binding.userStoresTv.text = getString(R.string.my_stores)
            binding.userFavTv.text = getString(R.string.favourites)
            binding.userBlogTv.text = getString(R.string.blog)
            binding.userBalanceTv.text = getString(R.string.balance)
            binding.userServiceTv.text = getString(R.string.services)
            binding.openStoreTv.text = getString(R.string.open_store)
            binding.contactsTv.text =getString(R.string.contacts)
            binding.aboutAppTv.text = getString(R.string.about_app)

            binding.userFavTv.setOnClickListener {
                activity?.supportFragmentManager?.commit {
                    replace(R.id.fragment_container_view_tag, FragmentFavourites()).addToBackStack("goBack")
                }
            }
    }
}