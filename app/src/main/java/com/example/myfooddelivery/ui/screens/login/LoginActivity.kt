package com.example.myfooddelivery.ui.screens.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.myfooddelivery.R
import com.example.myfooddelivery.ui.screens.BaseActivity
import com.example.myfooddelivery.data.datastore.LoggedInAccountDataStoreHelper
import com.example.myfooddelivery.data.sample.SampleData
import com.example.myfooddelivery.databinding.ActivityLoginBinding
import com.example.myfooddelivery.databinding.IncorrectPasswordDialogBoxBinding
import com.example.myfooddelivery.ui.helper.DatabaseProvider
import com.example.myfooddelivery.ui.screens.register.RegisterActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel: LoginViewModel by viewModels{
        LoginViewModelFactory(
            DatabaseProvider(this),
            LoggedInAccountDataStoreHelper(this)
        )
    }

    private fun displayPasswordIncorrectDialogBox() {
            val style = if (this.resources.configuration.isNightModeActive)
                R.style.GeneralAlertDialogLight
            else
                R.style.GeneralAlertDialogDark
            val dialogBoxBinding = IncorrectPasswordDialogBoxBinding.inflate(layoutInflater)
            val alertDialog = AlertDialog.Builder(this, style)
                .setView(dialogBoxBinding.root)
                .create()
            dialogBoxBinding.btnOk.setOnClickListener {
                lifecycleScope.launch {
                    delay(250)
                    alertDialog.dismiss()
                    binding.btnLogin.isClickable = true
                }
            }
            alertDialog.setCanceledOnTouchOutside(true)
            alertDialog.setOnCancelListener { lifecycleScope.launch {
                delay(250)
                alertDialog.dismiss()
                binding.btnLogin.isClickable = true
            }}
            alertDialog.show()
    }

    private fun setEvenListeners() {
        binding.etMobileNo.doOnTextChanged { text, _, _, _ ->
            viewModel.mobileNo = text.toString()
            if (!viewModel.isConfigChanged)
                viewModel.validateMobileNo(binding.tilMobileNo)
            else
                viewModel.isConfigChanged = false
        }
        binding.etPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.password = text.toString()
        }
        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.btnLogin.setOnClickListener {
            viewModel.validateMobileNo(binding.tilMobileNo)
            if (binding.tilMobileNo.error == null) {
                lifecycleScope.launch {
                    if(viewModel.authenticateUser(this@LoginActivity)){
                        val intent = Intent(this@LoginActivity, BaseActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else {
                        binding.btnLogin.isClickable = false
                        displayPasswordIncorrectDialogBox()
                    }
                }
            }
        }
    }

    private fun createSampleData() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                if (DatabaseProvider(this@LoginActivity).customerAccountRepository.getAllAccounts().isEmpty()) {
                    SampleData.createSampleData(DatabaseProvider(this@LoginActivity))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createSampleData()
        lifecycleScope.launch {
            if(viewModel.isUserLoggedIn()){
                val intent = Intent(this@LoginActivity, BaseActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                setContentView(binding.root)
                setEvenListeners()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.isConfigChanged = true
    }
}