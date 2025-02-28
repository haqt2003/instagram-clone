package com.example.instagram.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.instagram.R
import com.example.instagram.databinding.ActivityLanguageBinding
import com.example.instagram.utils.changeLanguage

class LanguageActivity : AppCompatActivity() {
    private val binding: ActivityLanguageBinding by lazy {
        ActivityLanguageBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_language)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sharedPreferences = getSharedPreferences("instagram_config", Context.MODE_PRIVATE)
        val language = sharedPreferences.getString("language", "")
        if (language == "en") {
            binding.rbEnglish.isChecked = true
            binding.rbVietnamese.isChecked = false
        } else {
            binding.rbEnglish.isChecked = false
            binding.rbVietnamese.isChecked = true
        }

        with(binding) {
            ivBack.setOnClickListener {
                finish()
            }

            rbEnglish.setOnClickListener {
                rbEnglish.isChecked = true
                rbVietnamese.isChecked = false
            }

            rbVietnamese.setOnClickListener {
                rbEnglish.isChecked = false
                rbVietnamese.isChecked = true
            }

            btSave.setOnClickListener {
                if (rbEnglish.isChecked) {
                    changeLanguage(this@LanguageActivity, "en")
                } else {
                    changeLanguage(this@LanguageActivity, "vi")
                }
            }

            btCancel.setOnClickListener {
                finish()
            }

        }
    }

}