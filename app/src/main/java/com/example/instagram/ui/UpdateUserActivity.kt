package com.example.instagram.ui

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.load
import com.example.instagram.R
import com.example.instagram.data.enums.Gender
import com.example.instagram.data.models.request.UpdateUserRequest
import com.example.instagram.databinding.ActivityUpdateUserBinding
import com.example.instagram.viewmodels.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream

class UpdateUserActivity : AppCompatActivity() {
    private val binding: ActivityUpdateUserBinding by lazy {
        ActivityUpdateUserBinding.inflate(layoutInflater)
    }
    private val userViewModel: UserViewModel by viewModel()
    private val gender = arrayOf(Gender.MALE.value, Gender.FEMALE.value, Gender.OTHER.value)

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            binding.ivAvatar.setImageURI(uri)
            binding.ivAvatar.tag = uri
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_update_user)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val genderAdapter = ArrayAdapter(this, R.layout.spinner_item, gender)
        val sharedPreferences = getSharedPreferences("instagram", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")

        userViewModel.updateMsg.observe(this) {
            if (it != null) {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show()
            }
        }

        with(binding) {
            spGender.adapter = genderAdapter


            ivBack.setOnClickListener {
                finish()
            }

            btChangePassword.setOnClickListener {
                val intent = Intent(this@UpdateUserActivity, ChangePasswordActivity::class.java)
                startActivity(intent)
            }

            btSave.setOnClickListener {
                val name = etName.text.toString()
                val address = etAddress.text.toString()
                val introduce = etIntroduce.text.toString()
                val gender = spGender.selectedItem.toString()
                val id = sharedPreferences.getString("id", "")
                val avatarUri = binding.ivAvatar.tag as? Uri
                val avatarFile: File? = avatarUri?.let { uriToFile(this@UpdateUserActivity, it) }
                val avatarBase64: String? = avatarFile?.let { fileToBase64(it) }

                val updateUserRequest = UpdateUserRequest(
                    old_password = null,
                    new_password = null,
                    name = name,
                    avatar = avatarBase64,
                    gender = gender,
                    address = address,
                    introduce = introduce,
                    userId = id.toString()
                )

                userViewModel.updateUser(updateUserRequest)
                Log.d("UpdateUserActivity", "avatarBase64: $avatarBase64")
            }

            tvEditAvatar.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }

        userViewModel.user.observe(this) {
            if (it != null) {
                binding.ivAvatar.load(it.avatar) {
                    error(R.drawable.no_avatar)
                }
                binding.etName.setText(it.name)
                binding.etAddress.setText(it.address)
                binding.etIntroduce.setText(it.introduce)
                binding.tvUsername.text = it.username
                val genderIndex = it.gender.let { gender ->
                    when (gender) {
                        Gender.MALE.value -> 0
                        Gender.FEMALE.value -> 1
                        else -> 2
                    }
                }
                binding.spGender.setSelection(genderIndex)
            }
        }

        if (username != null) {
            userViewModel.getUser(username)
        }
    }

    private fun uriToFile(context: Context, uri: Uri): File {
        val contentResolver: ContentResolver = context.contentResolver
        val fileName = getFileName(contentResolver, uri)
        val tempFile = File(context.cacheDir, fileName)

        try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(tempFile)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return tempFile
    }

    private fun getFileName(contentResolver: ContentResolver, uri: Uri): String {
        var name = "temp_file"
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                name = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
            }
        }
        return name
    }

    private fun fileToBase64(file: File): String {
        val bytes = FileInputStream(file).readBytes()
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }
}