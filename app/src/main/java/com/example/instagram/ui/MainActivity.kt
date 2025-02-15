package com.example.instagram.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.instagram.R
import com.example.instagram.databinding.ActivityMainBinding
import com.example.instagram.viewmodels.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val homeFragment = HomeFragment.newInstance()
    private val profileFragment = ProfileFragment.newInstance()

    private val userViewModel: UserViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sharedPreferences = getSharedPreferences("instagram", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")

        if (username.isNullOrEmpty()) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            userViewModel.getUser(username)
        }

        userViewModel.user.observe(this) {
            if (it != null) {
                val editor = sharedPreferences.edit()
                editor
                    .putInt("totalPost", it.totalPost)
                    .apply()
            }
        }

        if (savedInstanceState == null) {
            replaceFragment(homeFragment)
        }

        binding.bnvMain.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.it_home -> {
                    it.setIcon(R.drawable.ic_home_active)
                    replaceFragment(homeFragment)
                }

                R.id.it_profile -> {
                    it.setIcon(R.drawable.ic_user)

                    binding.bnvMain.menu.findItem(R.id.it_home)
                        .setIcon(R.drawable.ic_home)

                    replaceFragment(profileFragment)
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fcv_main, fragment)
            .addToBackStack(null)
            .commit()
    }
}