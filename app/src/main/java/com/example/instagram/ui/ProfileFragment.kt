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
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.R
import com.example.instagram.adapters.GridSpacingItemDecoration
import com.example.instagram.adapters.ProfileAdapter
import com.example.instagram.data.models.PostData
import com.example.instagram.data.models.response.GetUserResponse
import com.example.instagram.databinding.FragmentProfileBinding
import com.example.instagram.viewmodels.PostViewModel
import com.example.instagram.viewmodels.UserViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class ProfileFragment : Fragment(), ProfileAdapter.OnClickListener {
    private lateinit var binding: FragmentProfileBinding
    private val postViewModel: PostViewModel by lazy {
        requireActivity().getViewModel<PostViewModel>()
    }
    private val userViewModel: UserViewModel by lazy {
        requireActivity().getViewModel<UserViewModel>()
    }
    private lateinit var adapter: ProfileAdapter
    private var currentPage = 1
    private var hasMoreDataUser = true

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

            rvPosts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (!recyclerView.canScrollVertically(1)) {
                        if (hasMoreDataUser) {
                            loadNextPage()
                        }
                    }
                }
            })
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
            if (it.isNullOrEmpty()) {
                binding.tvNone.visibility = View.VISIBLE
            } else {
                binding.tvNone.visibility = View.GONE
                adapter.submitData(it)
            }
        }

        postViewModel.hasMoreDataUser.observe(viewLifecycleOwner) {
            hasMoreDataUser = it
        }

        postViewModel.currentPageUserPost.observe(viewLifecycleOwner) {
            currentPage = it
        }

        postViewModel.getUserPosts(user.username, currentPage, 15)

        userViewModel.getUser(user.username)

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }

    override fun onClickItem(item: PostData) {
        val detailFragment = DetailFragment.newInstance(item._id)

        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.fcv_main, detailFragment)
            .addToBackStack(null).commit()
    }

    private fun loadNextPage() {
        val sharedPreferences = requireActivity().getSharedPreferences("instagram", 0)
        val username = sharedPreferences.getString("username", "")
        if (hasMoreDataUser) {
            currentPage++
            postViewModel.getUserPosts(username.toString(), currentPage, 20)
        }
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
}