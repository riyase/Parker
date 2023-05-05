package com.keepqueue.sparepark.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.keepqueue.sparepark.data.MyPreferences
import com.keepqueue.sparepark.databinding.ActivityMyLoginBinding

class LoginActivity: AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityMyLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyLoginBinding.inflate(LayoutInflater.from(this))
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        setContentView(binding.root)

        binding.login.setOnClickListener {
            if (binding.email.text.toString().isEmpty()) {
                Toast.makeText(this, "Enter Email!", Toast.LENGTH_SHORT).show()
            } else if (binding.password.text.toString().isEmpty()) {
                Toast.makeText(this, "Enter Password!", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.login(binding.email.text.toString(), binding.password.text.toString())
            }
        }
        binding.bRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        viewModel.loginResult.observe(this) {result ->
            when(result) {
                is com.keepqueue.sparepark.data.response.Result.Success -> {
                    MyPreferences.setLoggedIn(this, true, result.data.userId, result.data.userName)
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