package com.example.myfooddelivery.ui.screens

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import com.example.myfooddelivery.R
import com.example.myfooddelivery.databinding.ActivityBaseBinding
import com.example.myfooddelivery.constants.Screens
import com.example.myfooddelivery.constants.FragmentTags
import com.example.myfooddelivery.ui.screens.favorites.FavoritesFragment
import com.example.myfooddelivery.ui.screens.home.HomeFragment
import com.example.myfooddelivery.ui.screens.myorders.ViewOrdersFragment
import com.example.myfooddelivery.ui.screens.profile.ProfileFragment


class BaseActivity : AppCompatActivity() {

    private val binding: ActivityBaseBinding by lazy { ActivityBaseBinding.inflate(layoutInflater) }
    private val viewModel: BaseActivityViewModel by viewModels()
    private val permissionRequestCode = 112

    private fun loadHomeFragment() {
        if (viewModel.selectedMenu != Screens.HOME_SCREEN)
            supportFragmentManager.commit {
                setCustomAnimations(R.anim.pop_fade_in, R.anim.pop_fade_out)
                replace(binding.flBaseFragment.id, HomeFragment(), FragmentTags.HOME_FRAGMENT.name)
            }
    }

    private fun loadViewOrdersFragment() {
        if (viewModel.selectedMenu != Screens.ORDER_SCREEN)
            supportFragmentManager.commit {
                setCustomAnimations(R.anim.pop_fade_in, R.anim.pop_fade_out)
                replace(binding.flBaseFragment.id, ViewOrdersFragment(), FragmentTags.ORDER_FRAGMENT.name)
            }
    }

    private fun loadProfileFragment() {
        if (viewModel.selectedMenu != Screens.PROFILE_SCREEN)
            supportFragmentManager.commit {
                setCustomAnimations(R.anim.pop_fade_in, R.anim.pop_fade_out)
                replace(binding.flBaseFragment.id, ProfileFragment(), FragmentTags.PROFILE_FRAGMENT.name)
            }
    }

    private fun loadFavoritesFragment() {
        if (viewModel.selectedMenu != Screens.FAVORITES_SCREEN)
            supportFragmentManager.commit {
                setCustomAnimations(R.anim.pop_fade_in, R.anim.pop_fade_out)
                replace(binding.flBaseFragment.id, FavoritesFragment())
            }
    }

    private fun handleBackPress() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when (viewModel.selectedMenu) {
                    Screens.HOME_SCREEN -> finish()
                    Screens.NEW_ORDER_SCREEN -> {
                        if (supportFragmentManager.backStackEntryCount > 1)
                            viewModel.selectedMenu = Screens.SEARCH_SCREEN
                        else {
                            if (supportFragmentManager.findFragmentByTag(FragmentTags.HOME_FRAGMENT.name) != null)
                                viewModel.selectedMenu = Screens.HOME_SCREEN
                            else
                                viewModel.selectedMenu = Screens.FAVORITES_SCREEN
                        }
                        supportFragmentManager.popBackStack()
                        if (viewModel.selectedMenu != Screens.SEARCH_SCREEN)
                            binding.bottomNavView.visibility = View.VISIBLE
                    }
                    Screens.PROFILE_UPDATE_SCREEN -> {
                        supportFragmentManager.popBackStack()
                        viewModel.selectedMenu = Screens.PROFILE_SCREEN
                        binding.bottomNavView.visibility = View.VISIBLE
                    }
                    Screens.VIEW_PARTICULAR_ORDER_SCREEN -> {
                        supportFragmentManager.commit {
                            setCustomAnimations(R.anim.pop_fade_in, R.anim.pop_fade_out)
                            replace(binding.flBaseFragment.id, ViewOrdersFragment())
                        }
                    }
                    Screens.ORDER_SUMMARY_SCREEN -> {
                        if(supportFragmentManager.backStackEntryCount > 1) {
                            supportFragmentManager.popBackStack()
                            viewModel.selectedMenu = Screens.NEW_ORDER_SCREEN
                        }
                    }
                    Screens.SEARCH_SCREEN -> {
                        supportFragmentManager.popBackStack()
                        viewModel.selectedMenu = Screens.HOME_SCREEN
                        binding.bottomNavView.visibility = View.VISIBLE
                    }
                    Screens.ORDER_DISPATCHED_SCREEN -> {
                        supportFragmentManager.commit {
                            setCustomAnimations(R.anim.pop_fade_in, R.anim.pop_fade_out)
                            replace(binding.flBaseFragment.id, ViewOrdersFragment())
                        }
                    }
                    else -> {
                        binding.bottomNavView.selectedItemId = R.id.menu_home
                    }
                }
            }
        })
    }

    private fun setUpBottomTab() {
        binding.bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    loadHomeFragment()
                    true
                }

                R.id.menu_order -> {
                    loadViewOrdersFragment()
                    true
                }

                R.id.menu_favorites -> {
                    loadFavoritesFragment()
                    true
                }

                R.id.menu_profile -> {
                    loadProfileFragment()
                    true
                }

                else -> false
            }
        }
    }

    private fun getNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this, arrayOf(POST_NOTIFICATIONS), permissionRequestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setUpBottomTab()
        handleBackPress()
        if(savedInstanceState == null) {
            viewModel.selectedMenu = Screens.FAVORITES_SCREEN
            loadHomeFragment()
        }
        val isOrderScreenSelected = intent.getBooleanExtra(Screens.ORDER_SCREEN.name, false)
        if (isOrderScreenSelected) {
            intent.replaceExtras(Bundle())
            binding.bottomNavView.selectedItemId = R.id.menu_order
        }

        if (Build.VERSION.SDK_INT > 32) {
            if (!shouldShowRequestPermissionRationale(permissionRequestCode.toString())){
                getNotificationPermission()
            }
        }
    }
}






























//    private fun loadProfileUpdateFragment() {
//        supportFragmentManager.commit {
//            setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
//            replace(binding.flBaseFragment.id, UpdateProfileFragment())
//        }
//    }
//
//    private fun loadNewOrderFragment() {
//        supportFragmentManager.commit {
//            setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
//            replace(binding.flBaseFragment.id, NewOrderFragment())
//        }
//    }
//
//    private fun loadViewParticularOrderFragment() {
//        supportFragmentManager.commit {
//            setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
//            replace(binding.flBaseFragment.id, ViewParticularOrderFragment())
//        }
//    }


//    private fun loadOrderSummaryFragment() {
//        if (viewModel.selectedMenu == Screens.ORDER_SUMMARY_SCREEN) {
//            supportFragmentManager.commit {
//                add(binding.flBaseFragment.id, OrderSummaryFragment())
//            }
//        }
//    }
//
//    private fun loadSearchFragment() {
//        if (viewModel.selectedMenu == Screens.SEARCH_SCREEN) {
//            supportFragmentManager.commit {
//                add(binding.flBaseFragment.id, SearchViewPagerFragment())
//            }
//        }
//    }
//
//    private fun loadOrderDispatchedFragment() {
//        if (viewModel.selectedMenu == Screens.ORDER_DISPATCHED_SCREEN) {
//            supportFragmentManager.commit {
//                add(binding.flBaseFragment.id, OrderDispatchedFragment())
//            }
//        }
//    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String?>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }