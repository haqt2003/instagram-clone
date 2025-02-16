package com.example.instagram.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagram.R
import com.example.instagram.adapters.AddPostAdapter
import com.example.instagram.databinding.ActivityAddPostBinding

class AddPostActivity : AppCompatActivity() {
    private val binding: ActivityAddPostBinding by lazy {
        ActivityAddPostBinding.inflate(layoutInflater)
    }
    private lateinit var adapter: AddPostAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_add)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val uris = intent.getStringArrayListExtra("uris")
        adapter = AddPostAdapter()
        binding.rvPosts.adapter = adapter
        binding.rvPosts.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter.submitData(uris ?: emptyList())

        binding.btPost.setOnClickListener {
            finish()
        }

        binding.btCancel.setOnClickListener {
            finish()
        }

        binding.ivBack.setOnClickListener {
            finish()
        }
    }
}