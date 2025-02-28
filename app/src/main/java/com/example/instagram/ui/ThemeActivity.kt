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
import java.util.Locale

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

        val sharedPreferences = getSharedPreferences("instagram_config", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val isDarkMode = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

        if (!isDarkMode) {
            binding.rbLights.isChecked = true
            binding.rbDarks.isChecked = false
        } else {
            binding.rbLights.isChecked = false
            binding.rbDarks.isChecked = true
        }

        with(binding) {
            ivBack.setOnClickListener {
                finish()
            }

            rbLights.setOnClickListener {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                rbLights.isChecked = true
                rbDarks.isChecked = false
                editor.putString("theme", "light")
                editor.apply()
            }

            rbDarks.setOnClickListener {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                rbLights.isChecked = false
                rbDarks.isChecked = true
                editor.putString("theme", "dark")
                editor.apply()
            }
        }
    }
}