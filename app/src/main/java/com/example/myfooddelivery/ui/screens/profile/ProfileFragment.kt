package com.example.myfooddelivery.ui.screens.profile

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.myfooddelivery.R
import com.example.myfooddelivery.data.datastore.LoggedInAccountDataStoreHelper
import com.example.myfooddelivery.databinding.ActivityBaseBinding
import com.example.myfooddelivery.databinding.FragmentProfileBinding
import com.example.myfooddelivery.constants.Screens
import com.example.myfooddelivery.ui.helper.ProfileAnimationHelper
import com.example.myfooddelivery.ui.screens.BaseActivityViewModel
import com.example.myfooddelivery.ui.screens.login.LoginActivity
import com.example.myfooddelivery.ui.screens.updateprofile.UpdateProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class ProfileFragment: Fragment() {
    private val binding: FragmentProfileBinding by lazy { FragmentProfileBinding.inflate(layoutInflater) }
    private val viewModel: ProfileViewModel by viewModels { ProfileViewModelFactory(
        LoggedInAccountDataStoreHelper(requireContext())
    ) }

    private fun setUpdateButtonListener() {
        binding.btnUpdate.setOnClickListener {
            parentFragmentManager.commit {
                val baseActivityBinding = ActivityBaseBinding.inflate(layoutInflater)
                setCustomAnimations(R.anim.pop_fade_in, R.anim.pop_fade_out)
                replace(baseActivityBinding.flBaseFragment.id, UpdateProfileFragment())
                addToBackStack(null)
            }
        }
    }

    private fun setLogoutButtonListener() {
        binding.btnLogout.setOnClickListener {
            lifecycleScope.launch {
                viewModel.logOut()
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }
    }

    private fun loadAccountDetails() {
        lifecycleScope.launch {
            val loggedInAccount = viewModel.getLoggedInAccount()!!
            binding.tvDescriptionName.text = loggedInAccount.name
            binding.tvDescriptionMobileNo.text = loggedInAccount.mobileNo
            binding.tvDescriptionAddress.text = resources.getString(
                R.string.customer_address,
                loggedInAccount.address.no,
                loggedInAccount.address.street,
                loggedInAccount.address.area,
                loggedInAccount.address.state,
                loggedInAccount.address.pinCode
            )
        }
    }

    private fun updateNightModeChanges() {
        if (context?.resources?.configuration?.isNightModeActive!!) {
            binding.tvDescriptionName.setTextColor(ContextCompat.getColor(requireContext(), R.color.pale_white))
            binding.tvDescriptionMobileNo.setTextColor(ContextCompat.getColor(requireContext(), R.color.pale_white))
            binding.tvDescriptionAddress.setTextColor(ContextCompat.getColor(requireContext(), R.color.pale_white))
            binding.ivName.setColorFilter(ContextCompat.getColor(requireContext(), R.color.pale_white))
            binding.ivMobileNo.setColorFilter(ContextCompat.getColor(requireContext(), R.color.pale_white))
            binding.ivAddress.setColorFilter(ContextCompat.getColor(requireContext(), R.color.pale_white))
            binding.cvProfileScreen.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black))
        }
        else {
            binding.tvDescriptionName.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_light))
            binding.tvDescriptionMobileNo.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_light))
            binding.tvDescriptionAddress.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_light))
            binding.ivName.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black_light))
            binding.ivMobileNo.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black_light))
            binding.ivAddress.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black_light))
            binding.cvProfileScreen.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }

    private fun showBottomNavBar() {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav_view).visibility = View.VISIBLE
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

        showBottomNavBar()
        ProfileAnimationHelper.animateProfileScreen(binding)
        val activityViewModel: BaseActivityViewModel by activityViewModels()
        activityViewModel.selectedMenu = Screens.PROFILE_SCREEN
        updateNightModeChanges()
        setUpdateButtonListener()
        setLogoutButtonListener()
        loadAccountDetails()
    }
}