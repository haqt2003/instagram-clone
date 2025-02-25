package com.example.instagram.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.instagram.R
import com.example.instagram.databinding.ActivityChangePasswordBinding
import com.example.instagram.viewmodels.UserViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.koin.androidx.viewmodel.ext.android.getViewModel

class ChangePasswordActivity : AppCompatActivity() {
    private val binding: ActivityChangePasswordBinding by lazy {
        ActivityChangePasswordBinding.inflate(layoutInflater)
    }
    private val userViewModel: UserViewModel by lazy {
        getViewModel<UserViewModel>()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_change_password)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userViewModel.updateMsg.observe(this) {
            if (it == "Cập nhật thành công!") {
                Toast.makeText(this, getString(R.string.update_success), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
            binding.etOldPassword.text?.clear()
            binding.etNewPassword.text?.clear()
            binding.btSave.text = getString(R.string.change_password)
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.btCancel.setOnClickListener {
            finish()
        }

        binding.btSave.setOnClickListener {
            val oldPassword = binding.etOldPassword.text.toString()
            val newPassword = binding.etNewPassword.text.toString()

            if (oldPassword.isNotEmpty() && newPassword.isNotEmpty()) {
                val sharedPreferences = getSharedPreferences("instagram", 0)
                val userId = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    sharedPreferences.getString("id", "").toString()
                )
                val old_password = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    oldPassword
                )
                val new_password = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    newPassword
                )

                binding.btSave.text = getString(R.string.change_password)
                userViewModel.updateUser(
                    userId,
                    old_password,
                    new_password,
                    null,
                    null,
                    null,
                    null,
                    null
                )
            } else {
                Toast.makeText(this, getString(R.string.enter_info), Toast.LENGTH_SHORT).show()
            }
        }
    }
}