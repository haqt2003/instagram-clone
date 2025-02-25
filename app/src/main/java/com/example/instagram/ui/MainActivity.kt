package com.example.instagram.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import coil.load
import com.example.instagram.R
import com.example.instagram.databinding.ActivityMainBinding
import com.example.instagram.viewmodels.PostViewModel
import com.example.instagram.viewmodels.UserViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val homeFragment = HomeFragment.newInstance()
    private var profileFragment = ProfileFragment.newInstance()

    private val userViewModel: UserViewModel by lazy {
        getViewModel<UserViewModel>()
    }
    private val postViewModel: PostViewModel by lazy {
        getViewModel<PostViewModel>()
    }

    private val pickMultipleMedia =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
            if (uris.isNotEmpty()) {
                val intent = Intent(this, AddPostActivity::class.java)
                intent.putStringArrayListExtra("uris", ArrayList(uris.map { it.toString() }))
                startActivity(intent)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setLanguage()

        if (savedInstanceState == null) {
            replaceFragment(homeFragment)
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

        val msg = intent.getStringExtra("msg")
        if (!msg.isNullOrEmpty()) {
            postViewModel.getPosts(1)
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        userViewModel.user.observe(this) {
            if (it != null) {
                val editor = sharedPreferences.edit()
                editor
                    .putInt("totalPost", it.totalPost)
                    .apply()

                if (it.username == username) {
                    binding.ivProfile.load(it.avatar) {
                        error(R.drawable.no_avatar)
                    }
                }
            }
        }

        with(binding) {
            ivHome.setOnClickListener {
                ivHome.setImageResource(R.drawable.ic_home_active)
                ivRoundProfile.visibility = View.INVISIBLE
                if (supportFragmentManager.findFragmentById(R.id.fcv_main) !is HomeFragment) {
                    replaceFragment(homeFragment)
                } else {
                    postViewModel.setReloadHome(true)
                }
            }
            ivAdd.setOnClickListener {
                selectImages()
            }
            ivProfile.setOnClickListener {
                ivHome.setImageResource(R.drawable.ic_home)
                ivRoundProfile.visibility = View.VISIBLE
                profileFragment = ProfileFragment.newInstance()
                replaceFragment(profileFragment)
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fcv_main, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun selectImages() {
        pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun setLanguage() {
        val sharedPreferences = getSharedPreferences("instagram_config", Context.MODE_PRIVATE)
        val languageCode = sharedPreferences.getString("language", "vi") ?: "vi"

        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = resources.configuration
        config.setLocale(locale)

        resources.updateConfiguration(config, resources.displayMetrics)
    }
}