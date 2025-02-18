package com.example.instagram.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.instagram.R
import com.example.instagram.databinding.ActivityRegisterBinding
import com.example.instagram.viewmodels.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {
    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    private val authViewModel: AuthViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_register)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        authViewModel.authMsg.observe(this) {
            if (it != null) {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                binding.etName.text?.clear()
                binding.etUsername.text?.clear()
                binding.etPassword.text?.clear()
            } else {
                Toast.makeText(this, "Có lỗi!", Toast.LENGTH_SHORT).show()
                binding.etName.text?.clear()
                binding.etUsername.text?.clear()
                binding.etPassword.text?.clear()
            }
            binding.btRegister.isEnabled = true
            binding.btRegister.text = "Đăng ký"
        }

        with(binding) {
            btRegister.setOnClickListener {
                val name = etName.text.toString()
                val username = etUsername.text.toString()
                val password = etPassword.text.toString()
                if (name.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()) {
                    authViewModel.signup(name, username, password)
                } else {
                    Toast.makeText(this@RegisterActivity, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                }
                btRegister.isEnabled = false
                btRegister.text = "Đăng ký..."
            }

            tvLogin.setOnClickListener {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }

            ivShowPassword.setOnClickListener {
                if (etPassword.inputType == 129) {
                    etPassword.inputType = 1
                    ivShowPassword.setImageResource(R.drawable.ic_show)
                } else {
                    etPassword.inputType = 129
                    ivShowPassword.setImageResource(R.drawable.ic_hide)
                }
            }
        }
    }
}