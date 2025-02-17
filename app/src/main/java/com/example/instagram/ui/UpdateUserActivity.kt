package com.example.instagram.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.MimeTypeMap
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
import com.example.instagram.databinding.ActivityUpdateUserBinding
import com.example.instagram.viewmodels.UserViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class UpdateUserActivity : AppCompatActivity() {
    private val binding: ActivityUpdateUserBinding by lazy {
        ActivityUpdateUserBinding.inflate(layoutInflater)
    }
    private val userViewModel: UserViewModel by viewModel()
    private val gender = arrayOf(Gender.MALE.value, Gender.FEMALE.value, Gender.OTHER.value)

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                binding.ivAvatar.setImageURI(uri)
                binding.ivAvatar.tag = uri
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
                val userId = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    sharedPreferences.getString("id", "").toString()
                )
                val name =
                    RequestBody.create("text/plain".toMediaTypeOrNull(), etName.text.toString())
                val address =
                    RequestBody.create("text/plain".toMediaTypeOrNull(), etAddress.text.toString())
                val introduce = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    etIntroduce.text.toString()
                )
                val gender = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    spGender.selectedItem.toString()
                )
                val avatarUri = binding.ivAvatar.tag as? Uri
                val avatarPart = prepareImageFilePart("avatar", avatarUri)

                userViewModel.updateUser(
                    userId,
                    null,
                    null,
                    name,
                    avatarPart,
                    gender,
                    address,
                    introduce
                )

                finish()
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

    private fun Context.prepareImageFilePart(partName: String, fileUri: Uri?): MultipartBody.Part? {
        if (fileUri == null) return null

        val contentResolver = contentResolver

        val fileExtension = MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(fileUri)) ?: "jpg"

        val mimeType = "image/$fileExtension".toMediaTypeOrNull()

        val inputStream: InputStream? = contentResolver.openInputStream(fileUri)
        val file = File(cacheDir, "avatar_${System.currentTimeMillis()}.$fileExtension")

        inputStream?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }

        val requestBody = RequestBody.create(mimeType, file)
        return MultipartBody.Part.createFormData(partName, file.name, requestBody)
    }


}