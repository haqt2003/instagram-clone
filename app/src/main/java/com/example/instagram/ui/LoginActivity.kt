package com.example.instagram.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.instagram.R
import com.example.instagram.databinding.ActivityLoginBinding
import com.example.instagram.viewmodels.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val authViewModel: AuthViewModel by viewModel()
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        authViewModel.user.observe(this) {
            if (it != null) {
                val sharedPreferences = getSharedPreferences("instagram", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor
                    .putString("username", it.username)
                    .putString("id", it._id)
                    .putString("name", it.name)
                    .apply()

                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
            }
            binding.btLogin.isEnabled = true
            binding.btLogin.text = "Đăng nhập"
        }

        authViewModel.authMsg.observe(this) {
            if (it != null) {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        with(binding) {
            btLogin.setOnClickListener {
                btLogin.isEnabled = false
                btLogin.text = "Đăng nhập..."
                authViewModel.login(etUsername.text.toString(), etPassword.text.toString())
            }

            tvRegister.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
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