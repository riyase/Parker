package com.keepqueue.sparepark.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.keepqueue.sparepark.data.Prefs
import com.keepqueue.sparepark.data.Util.isValidEmail
import com.keepqueue.sparepark.data.Util.isValidPassword
import com.keepqueue.sparepark.data.Util.isValidPhone
import com.keepqueue.sparepark.databinding.ActivityRegisterBinding

class RegisterActivity: AppCompatActivity() {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(LayoutInflater.from(this))
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        setContentView(binding.root)

        binding.bRegister.setOnClickListener {
            if (binding.etUsername.text.toString().isEmpty()) {
                Toast.makeText(this, "Enter Name!", Toast.LENGTH_SHORT).show()
            } else if (binding.etEmail.text.toString().isEmpty()) {
                Toast.makeText(this, "Enter Email!", Toast.LENGTH_SHORT).show()
            } else if (binding.etPhone.text.toString().isEmpty()) {
                Toast.makeText(this, "Enter Phone!", Toast.LENGTH_SHORT).show()
            } else if (binding.etPassword.text.toString().isEmpty()) {
                Toast.makeText(this, "Enter Password!", Toast.LENGTH_SHORT).show()
            } else if (!binding.etEmail.text.isValidEmail()) {
                Toast.makeText(this, "Enter a valid Email!", Toast.LENGTH_SHORT).show()
            } else if (binding.etPhone.text.isValidPhone()) {
                Toast.makeText(this, "Phone number should be at least 11 numbers!", Toast.LENGTH_SHORT).show()
            } else if (binding.etPassword.text.isValidPassword()) {
                Toast.makeText(this, "Password should be at least 6 characters!", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.register(binding.etUsername.text.toString(),
                    binding.etEmail.text.toString(),
                    binding.etPhone.text.toString(),
                    binding.etPassword.text.toString(),
                )
            }
        }

        binding.bLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        viewModel.registerResult.observe(this) {result ->
            when(result) {
                is com.keepqueue.sparepark.data.response.Result.Success -> {
                    Prefs.setLoggedIn(this, true, result.data.userId, result.data.userName)
                    finish()
                }
                is com.keepqueue.sparepark.data.response.Result.Loading -> {

                }
                is com.keepqueue.sparepark.data.response.Result.Error -> {
                    Toast.makeText(this, "Error:${result.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}