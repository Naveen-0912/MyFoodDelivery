package com.example.myfooddelivery.ui.screens.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.myfooddelivery.R
import com.example.myfooddelivery.databinding.ActivityRegisterBinding
import com.example.myfooddelivery.constants.CustomerAccountFields
import com.example.myfooddelivery.ui.helper.DatabaseProvider
import com.example.myfooddelivery.ui.screens.login.LoginActivity
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    val binding: ActivityRegisterBinding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private val viewModel: RegisterViewModel by viewModels{ RegisterViewModelFactory(
        DatabaseProvider(this)
    ) }


    private fun setInputFieldsListener() {
        binding.etName.doOnTextChanged { text, _, _, _ ->
            viewModel.name = text.toString()
            if (!viewModel.isConfigChanged)
                viewModel.validateName(binding.tilName)
        }
        binding.etNo.doOnTextChanged { text, _, _, _ ->
            viewModel.no = text.toString()
            if (!viewModel.isConfigChanged)
                viewModel.validateNo(binding.tilNo)
        }
        binding.etStreet.doOnTextChanged { text, _, _, _ ->
            viewModel.street = text.toString()
            if (!viewModel.isConfigChanged)
                viewModel.validateStreet(binding.tilStreet)
        }
        binding.etArea.doOnTextChanged { text, _, _, _ ->
            viewModel.area = text.toString()
            if (!viewModel.isConfigChanged)
                viewModel.validateArea(binding.tilArea)
        }
        binding.etState.doOnTextChanged { text, _, _, _ ->
            viewModel.state = text.toString()
            if (!viewModel.isConfigChanged)
                viewModel.validateState(binding.tilState)
        }
        binding.etPinCode.doOnTextChanged { text, _, _, _ ->
            viewModel.pinCode = text.toString()
            if (!viewModel.isConfigChanged)
                viewModel.validatePinCode(binding.tilPinCode)
        }
        binding.etMobileNo.doOnTextChanged { text, _, _, _ ->
            viewModel.mobileNo = text.toString()
            if (!viewModel.isConfigChanged)
                viewModel.validateMobileNo(binding.tilMobileNo)
        }
        binding.etPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.password = text.toString()
            if (!viewModel.isConfigChanged) {
                viewModel.validatePassword(binding.tilPassword)
                viewModel.validateConfirmPassword(binding.tilConfirmPassword)
            }
        }
        binding.etConfirmPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.confirmPassword = text.toString()
            if (!viewModel.isConfigChanged)
                viewModel.validateConfirmPassword(binding.tilConfirmPassword)
            else
                viewModel.isConfigChanged = false
        }
    }

    private fun registerUser() {
        lifecycleScope.launch {
            if(viewModel.registerUser()){
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK  //To clear the existing stack and create new task
                }
                startActivity(intent)
                Toast.makeText(this@RegisterActivity, "Registered successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            else{
                binding.tilMobileNo.error = resources.getString(R.string.mobile_no_already_exists_text)
                Toast.makeText(this@RegisterActivity, "Registration failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun focusErrorField() {
        if (viewModel.errorFields.isEmpty())
            registerUser()
        else {
            viewModel.validateName(binding.tilName)
            viewModel.validateNo(binding.tilNo)
            viewModel.validateStreet(binding.tilStreet)
            viewModel.validateArea(binding.tilArea)
            viewModel.validateState(binding.tilState)
            viewModel.validatePinCode(binding.tilPinCode)
            viewModel.validateMobileNo(binding.tilMobileNo)
            viewModel.validatePassword(binding.tilPassword)
            viewModel.validateConfirmPassword(binding.tilConfirmPassword)
            when(viewModel.errorFields.first()) {
                CustomerAccountFields.NAME -> binding.etName.requestFocus()
                CustomerAccountFields.NO -> binding.etNo.requestFocus()
                CustomerAccountFields.STREET -> binding.etStreet.requestFocus()
                CustomerAccountFields.AREA -> binding.etArea.requestFocus()
                CustomerAccountFields.STATE -> binding.etState.requestFocus()
                CustomerAccountFields.PIN_CODE -> binding.etPinCode.requestFocus()
                CustomerAccountFields.MOBILE_NO -> binding.etMobileNo.requestFocus()
                CustomerAccountFields.PASSWORD -> binding.etPassword.requestFocus()
                CustomerAccountFields.CONFIRM_PASSWORD -> binding.etConfirmPassword.requestFocus()
            }
        }
    }

    private fun setRegisterButtonListener() {
        binding.btnRegister.setOnClickListener {
            focusErrorField()
        }
    }

    private fun updateNightModeChanges() {
        if (resources.configuration.isNightModeActive){
            binding.ivName.setColorFilter(ContextCompat.getColor(this, R.color.pale_white))
            binding.ivAddress.setColorFilter(ContextCompat.getColor(this, R.color.pale_white))
            binding.ivMobile.setColorFilter(ContextCompat.getColor(this, R.color.pale_white))
            binding.ivPassword.setColorFilter(ContextCompat.getColor(this, R.color.pale_white))
        }
        else {
            binding.ivName.setColorFilter(ContextCompat.getColor(this, R.color.black))
            binding.ivAddress.setColorFilter(ContextCompat.getColor(this, R.color.black))
            binding.ivMobile.setColorFilter(ContextCompat.getColor(this, R.color.black))
            binding.ivPassword.setColorFilter(ContextCompat.getColor(this, R.color.black))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        updateNightModeChanges()
        setInputFieldsListener()
        setRegisterButtonListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.isConfigChanged = true
    }
}