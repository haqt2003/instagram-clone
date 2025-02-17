package com.example.instagram.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagram.R
import com.example.instagram.adapters.AddPostAdapter
import com.example.instagram.databinding.ActivityAddPostBinding
import com.example.instagram.viewmodels.PostViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class AddPostActivity : AppCompatActivity() {
    private val binding: ActivityAddPostBinding by lazy {
        ActivityAddPostBinding.inflate(layoutInflater)
    }
    private val postViewModel: PostViewModel by viewModel()
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
        val sharedPreferences = getSharedPreferences("instagram", Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("id", "")
        val username = sharedPreferences.getString("username", "")

        val uris = intent.getStringArrayListExtra("uris")
        adapter = AddPostAdapter()
        binding.rvPosts.adapter = adapter
        binding.rvPosts.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter.submitData(uris ?: emptyList())

        postViewModel.msg.observe(this) {
            if (it != null) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Đăng bài thành công!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btPost.setOnClickListener {
            if (binding.etContent.text.toString().isNotEmpty()) {
                val userId = RequestBody.create("text/plain".toMediaTypeOrNull(), id.toString())
                val content = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    binding.etContent.text.toString()
                )
                val imagesUri =
                    intent.getStringArrayListExtra("uris")?.map { Uri.parse(it) } ?: emptyList()
                val imageParts = prepareImageParts(imagesUri)
                postViewModel.addPost(userId, imageParts, content)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Vui lòng nhập nội dung!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btCancel.setOnClickListener {
            finish()
        }

        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun Context.prepareImageParts(imageUris: List<Uri>): List<MultipartBody.Part> {
        val parts = mutableListOf<MultipartBody.Part>()
        val contentResolver = contentResolver

        imageUris.forEachIndexed { index, uri ->
            // Lấy phần mở rộng (jpg, png, jpeg)
            val fileExtension =
                MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri))
                    ?: "jpg"
            val mimeType = "image/$fileExtension".toMediaTypeOrNull()

            // Tạo file trong cache
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val file = File(cacheDir, "image_${System.currentTimeMillis()}_${index}.$fileExtension")

            inputStream?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }

            // Chuyển đổi file thành MultipartBody.Part
            val requestBody = RequestBody.create(mimeType, file)
            val part = MultipartBody.Part.createFormData("images", file.name, requestBody)

            parts.add(part)
        }

        return parts
    }

}