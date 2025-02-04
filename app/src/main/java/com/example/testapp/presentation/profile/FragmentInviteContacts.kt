package com.example.testapp.presentation.profile

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapp.MainActivity
import com.example.testapp.R
import com.example.testapp.databinding.FragmentInviteContactsBinding
import com.example.testapp.domain.model.ContactModel
import com.example.testapp.presentation.profile.viewModel.InviteContactsViewModel
import kotlinx.coroutines.launch

class FragmentInviteContacts: Fragment(R.layout.fragment_invite_contacts) {
    private lateinit var binding: FragmentInviteContactsBinding
    private val viewModel = InviteContactsViewModel()
    //private val contactsList = mutableListOf<ContactModel>()
    private lateinit var contactsAdapter: ContactsAdapter
    private var selectedContact: ContactModel? = null
    companion object {
        private const val REQUEST_CODE_READ_CONTACTS = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInviteContactsBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        (activity as? MainActivity)?.hideBottomNavBar()
        contactsAdapter = ContactsAdapter(layoutInflater){
                contact ->
            selectedContact = contact
        }
        binding.clickerSend.setOnClickListener {
            selectedContact?.let { contact ->
                sendSms(contact.phoneNumber) // Send SMS to the selected contact
            } ?: run {
                Toast.makeText(requireContext(), "No contact selected", Toast.LENGTH_SHORT).show()
            }
        }

        binding.rvContacts.adapter = contactsAdapter
        binding.rvContacts.layoutManager = LinearLayoutManager(requireContext())
        // Check and request permissions
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_CODE_READ_CONTACTS
            )
        } else {
            // Permission already granted, fetch contacts
           // fetchContacts()
            viewModel.fetchContacts(requireContext().contentResolver)
        }
         lifecycleScope.launch {
            viewModel.contacts.collect{
                contactsAdapter.setData(it.sortedBy { it.name })
            }
        }
        binding.edSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Filter the list based on the search query
                val query = s.toString().trim()
                viewModel.filterContacts(query)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

    }
    private fun sendSms(phoneNumber: String) {
        val smsIntent = Intent(Intent.ACTION_VIEW)
        smsIntent.data = Uri.parse("sms:$phoneNumber")
        smsIntent.putExtra("sms_body", "Check out this app: https://example.com") // Replace with your app link
        startActivity(smsIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.showBottomNavBar()
    }
}