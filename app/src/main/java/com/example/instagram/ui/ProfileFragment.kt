package com.example.instagram.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import coil.load
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.instagram.R
import com.example.instagram.adapters.GridSpacingItemDecoration
import com.example.instagram.adapters.ProfileAdapter
import com.example.instagram.data.enums.Gender
import com.example.instagram.data.models.PostData
import com.example.instagram.data.models.response.GetUserResponse
import com.example.instagram.databinding.FragmentProfileBinding
import com.example.instagram.viewmodels.PostViewModel
import com.example.instagram.viewmodels.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(), ProfileAdapter.OnClickListener {
    private lateinit var binding: FragmentProfileBinding
    private val postViewModel: PostViewModel by viewModel()
    private val userViewModel: UserViewModel by viewModel()
    private lateinit var adapter: ProfileAdapter

    private val pickMultipleMedia =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
            if (uris.isNotEmpty()) {
                val intent = Intent(requireActivity(), AddPostActivity::class.java)
                intent.putStringArrayListExtra("uris", ArrayList(uris.map { it.toString() }))
                startActivity(intent)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        val user = getPrefs()

        adapter = ProfileAdapter(this)

        with(binding) {
            rvPosts.adapter = adapter
            rvPosts.layoutManager = GridLayoutManager(requireContext(), 3)
            rvPosts.addItemDecoration(GridSpacingItemDecoration(3, 4, false))

            ivAdd.setOnClickListener {
                pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            ivMenu.setOnClickListener {
                val settingFragment = SettingFragment.newInstance()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fcv_main, settingFragment)
                    .addToBackStack(null).commit()
            }
        }

        userViewModel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.tvPostsCount.text = it.totalPost.toString()
                binding.ivAvatar.load(it.avatar) {
                    error(R.drawable.no_avatar)
                }
                binding.tvUsername.text = it.username
            }
        }

        userViewModel.updateUser.observe(viewLifecycleOwner) {
            if (it != null) {
                userViewModel.getUser(it.username)
            }
        }

        postViewModel.userPosts.observe(viewLifecycleOwner) {
            adapter.submitData(it)
            if (it.isEmpty()) {
                binding.tvNone.visibility = View.VISIBLE
            } else {
                binding.tvNone.visibility = View.GONE
            }
        }

        postViewModel.getUserPosts("huycholl")

        userViewModel.getUser(user.username)

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }

    private fun getPrefs(): GetUserResponse {
        val sharedPreferences = requireActivity().getSharedPreferences("instagram", 0)
        val username = sharedPreferences.getString("username", "")
        val avatar = sharedPreferences.getString("avatar", "")
        val totalPost = sharedPreferences.getInt("totalPost", 0)

        return GetUserResponse(
            username.toString(), "", avatar.toString(), "", "", "", totalPost
        )
    }

    override fun onClickItem(item: PostData) {
        val detailFragment = DetailFragment.newInstance(item._id)

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fcv_main, detailFragment)
            .addToBackStack(null).commit()
    }
}