package com.example.instagram.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(), ProfileAdapter.OnClickListener {
    private lateinit var binding: FragmentProfileBinding
    private val postViewModel: PostViewModel by viewModel()
    private lateinit var adapter: ProfileAdapter

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
            rvPosts.layoutManager = GridLayoutManager(requireContext(),3)
            rvPosts.addItemDecoration(GridSpacingItemDecoration(3, 4, false))
            tvUsername.text = user.username
            tvPostsCount.text = user.totalPost.toString()
            ivAvatar.load(user.avatar) {
                error(R.drawable.no_avatar)
            }
        }

        postViewModel.userPosts.observe(viewLifecycleOwner) {
            adapter.submitData(it)
        }

        postViewModel.getUserPosts("huycholl")

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
            username.toString(), "", avatar.toString(), "", Gender.MALE, "", totalPost
        )
    }

    override fun onClickItem(item: PostData) {
        val detailFragment = DetailFragment.newInstance(item._id)

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fcv_main, detailFragment)
            .addToBackStack(null).commit()
    }
}