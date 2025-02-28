package com.example.instagram.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.instagram.R
import com.example.instagram.adapters.EditPostAdapter
import com.example.instagram.databinding.ActivityEditPostBinding
import com.example.instagram.viewmodels.PostViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class EditPostActivity : AppCompatActivity(), EditPostAdapter.OnClickListener {
    private val binding: ActivityEditPostBinding by lazy {
        ActivityEditPostBinding.inflate(layoutInflater)
    }
    private val postViewModel: PostViewModel by lazy {
        getViewModel<PostViewModel>()
    }
    private lateinit var adapter: EditPostAdapter
    private val images = mutableListOf<String>()
    private var currentPageUser = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val uid = intent.getStringExtra("userId")
        val pid = intent.getStringExtra("postId")
        val username = intent.getStringExtra("username")

        postViewModel.getAllUserPosts(username.toString())

        adapter = EditPostAdapter(this)
        binding.rvPosts.adapter = adapter
        binding.rvPosts.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        postViewModel.userPosts.observe(this) {
            val post = it.find { it._id == pid }
            if (post != null) {
                images.clear()
                images.addAll(post.images)
                adapter.submitData(images)
                binding.tvName.text = post.author.username
                binding.ivAvatar.load(post.author.avatar) {
                    error(R.drawable.no_avatar)
                }
                binding.etContent.setText(post.content)
            }
        }

        postViewModel.currentPageUserPost.observe(this) {
            currentPageUser = it
        }

        binding.tvCancel.setOnClickListener {
            finish()
        }

        binding.tvConfirm.setOnClickListener {
            val userId = RequestBody.create("text/plain".toMediaTypeOrNull(), uid.toString())
            val postId = RequestBody.create("text/plain".toMediaTypeOrNull(), pid.toString())
            val content = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                binding.etContent.text.toString()
            )

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val imagesPath = images.map { imageUrl ->
                        val url = URL(imageUrl)
                        val fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1)
                        val file = downloadImage(url)
                        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension) ?: "image/jpeg"
                        val requestBody = RequestBody.create(mimeType.toMediaTypeOrNull(), file)

                        MultipartBody.Part.createFormData("images", fileName, requestBody)
                    }

                    withContext(Dispatchers.Main) {
                        binding.tvConfirm.visibility = View.GONE
                        binding.pbLoading.visibility = View.VISIBLE
                        postViewModel.editPost(userId, postId, imagesPath, content)
                        postViewModel.msg.observe(this@EditPostActivity) { message ->
                            val intent = Intent(this@EditPostActivity, MainActivity::class.java)
                            if (message == "e_success") {
                                intent.putExtra("msg", getString(R.string.edit_post_success))
                            }
                            startActivity(intent)
                            finish()
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onItemClicked(item: String) {
        if (images.contains(item) && images.size > 1) {
            images.remove(item)
            adapter.submitData(images)
        } else if (images.size == 1) {
            Toast.makeText(this, getString(R.string.at_least_1_img), Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun downloadImage(url: URL): File {
        return withContext(Dispatchers.IO) {
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.doInput = true
            connection.connect()

            val inputStream = connection.inputStream
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(url.toString()) ?: "jpg"
            val file = File.createTempFile("temp_image", ".$fileExtension", cacheDir)

            inputStream.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            file
        }
    }

}