package com.example.myfooddelivery.ui.screens.updateprofile

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.myfooddelivery.R
import com.example.myfooddelivery.data.datastore.LoggedInAccountDataStoreHelper
import com.example.myfooddelivery.databinding.FragmentUpdateProfileBinding
import com.example.myfooddelivery.constants.Screens
import com.example.myfooddelivery.constants.CustomerAccountFields
import com.example.myfooddelivery.ui.helper.DatabaseProvider
import com.example.myfooddelivery.ui.screens.BaseActivityViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class UpdateProfileFragment: Fragment() {
    private val binding: FragmentUpdateProfileBinding by lazy { FragmentUpdateProfileBinding.inflate(layoutInflater) }
    private val viewModel: UpdateProfileViewModel by viewModels {
        UpdateProfileViewModelFactory(LoggedInAccountDataStoreHelper(requireContext()),
            DatabaseProvider(requireContext())
        )
    }

    private fun updateUser() {
        lifecycleScope.launch {
            if(viewModel.updateUser()){
                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun loadAccountDetails() {
        viewModel.loadAccountDetails()
        binding.etName.setText(viewModel.name)
        binding.etNo.setText(viewModel.no)
        binding.etStreet.setText(viewModel.street)
        binding.etArea.setText(viewModel.area)
        binding.etState.setText(viewModel.state)
        binding.etPinCode.setText(viewModel.pinCode)
    }

    private fun focusErrorField() {
        if (viewModel.errorFields.isEmpty()) {
            updateUser()
        }
        else {
            when (viewModel.errorFields.first()) {
                CustomerAccountFields.NAME -> binding.etName.requestFocus()
                CustomerAccountFields.NO -> binding.etNo.requestFocus()
                CustomerAccountFields.STREET -> binding.etStreet.requestFocus()
                CustomerAccountFields.AREA -> binding.etArea.requestFocus()
                CustomerAccountFields.STATE -> binding.etState.requestFocus()
                CustomerAccountFields.PIN_CODE -> binding.etPinCode.requestFocus()
                else -> {}
            }
        }
    }

    private fun setUpdateButtonListener() {
        binding.btnUpdate.setOnClickListener {
            viewModel.validateName(binding.tilName)
            viewModel.validateNo(binding.tilNo)
            viewModel.validateStreet(binding.tilStreet)
            viewModel.validateArea(binding.tilArea)
            viewModel.validateState(binding.tilState)
            viewModel.validatePinCode(binding.tilPinCode)
            focusErrorField()
        }
    }

    private fun setInputFieldsListener() {
        binding.etName.doOnTextChanged { text, _, _, _ ->
            viewModel.name = text.toString()
            viewModel.validateName(binding.tilName)
        }
        binding.etNo.doOnTextChanged { text, _, _, _ ->
            viewModel.no = text.toString()
            viewModel.validateNo(binding.tilNo)
        }
        binding.etStreet.doOnTextChanged { text, _, _, _ ->
            viewModel.street = text.toString()
            viewModel.validateStreet(binding.tilStreet)
        }
        binding.etArea.doOnTextChanged { text, _, _, _ ->
            viewModel.area = text.toString()
            viewModel.validateArea(binding.tilArea)
        }
        binding.etState.doOnTextChanged { text, _, _, _ ->
            viewModel.state = text.toString()
            viewModel.validateState(binding.tilState)
        }
        binding.etPinCode.doOnTextChanged { text, _, _, _ ->
            viewModel.pinCode = text.toString()
            viewModel.validatePinCode(binding.tilPinCode)
        }
    }

    private fun updateNightModeChanges() {
        if (context?.resources?.configuration?.isNightModeActive!!){
            binding.clUpdateProfile.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black))
            binding.ivName.setColorFilter(ContextCompat.getColor(requireContext(), R.color.pale_white))
            binding.ivAddress.setColorFilter(ContextCompat.getColor(requireContext(), R.color.pale_white))
            binding.etName.setTextColor(ContextCompat.getColor(requireContext(), R.color.pale_white))
            binding.etNo.setTextColor(ContextCompat.getColor(requireContext(), R.color.pale_white))
            binding.etStreet.setTextColor(ContextCompat.getColor(requireContext(), R.color.pale_white))
            binding.etArea.setTextColor(ContextCompat.getColor(requireContext(), R.color.pale_white))
            binding.etState.setTextColor(ContextCompat.getColor(requireContext(), R.color.pale_white))
            binding.etPinCode.setTextColor(ContextCompat.getColor(requireContext(), R.color.pale_white))
        }
        else {
            binding.clUpdateProfile.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
            binding.ivName.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))
            binding.ivAddress.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))
            binding.etName.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.etNo.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.etStreet.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.etArea.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.etState.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.etPinCode.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }
    }

    private fun hideBottomNavBar() {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav_view).visibility = View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideBottomNavBar()
        val activityViewModel: BaseActivityViewModel by activityViewModels()
        activityViewModel.selectedMenu = Screens.PROFILE_UPDATE_SCREEN
        updateNightModeChanges()
        loadAccountDetails()
        setInputFieldsListener()
        setUpdateButtonListener()
    }
}