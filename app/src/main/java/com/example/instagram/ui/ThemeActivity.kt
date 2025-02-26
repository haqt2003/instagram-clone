package com.example.instagram.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.instagram.R
import com.example.instagram.databinding.ActivityThemeBinding
import com.example.instagram.utils.changeLanguage

class ThemeActivity : AppCompatActivity() {
    private val binding: ActivityThemeBinding by lazy {
        ActivityThemeBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_theme)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val isDarkMode = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

        if (!isDarkMode) {
            binding.rbLight.isChecked = true
            binding.rbDark.isChecked = false
        } else {
            binding.rbLight.isChecked = false
            binding.rbDark.isChecked = true
        }

        with(binding) {
            ivBack.setOnClickListener {
                finish()
            }

            rbLight.setOnClickListener {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                rbLight.isChecked = true
                rbDark.isChecked = false
            }

            rbDark.setOnClickListener {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                rbLight.isChecked = false
                rbDark.isChecked = true
            }
        }
    }
}