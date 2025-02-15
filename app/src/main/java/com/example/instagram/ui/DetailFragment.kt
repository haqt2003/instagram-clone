package com.example.instagram.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagram.R
import com.example.instagram.adapters.HomeAdapter
import com.example.instagram.data.enums.Gender
import com.example.instagram.data.models.AuthorData
import com.example.instagram.data.models.PostData
import com.example.instagram.databinding.FragmentDetailBinding
import com.example.instagram.viewmodels.PostViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : Fragment(), HomeAdapter.OnClickListener {
    private lateinit var binding: FragmentDetailBinding
    private val postViewModel: PostViewModel by viewModel()
    private lateinit var adapter: HomeAdapter
    private var postId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            postId = it.getString(ARG_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)

        adapter = HomeAdapter(this)
        binding.rvPosts.adapter = adapter
        binding.rvPosts.layoutManager = LinearLayoutManager(requireContext())

        postViewModel.userPosts.observe(viewLifecycleOwner) {
            adapter.submitData(it)
        }

        postViewModel.getUserPosts("huycholl")

        binding.ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return binding.root
    }

    companion object {
        private const val ARG_ID = "post_id"
        @JvmStatic
        fun newInstance(postId: String) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ID, postId)
                }
            }
    }

    override fun onLikeClicked(item: PostData) {
        val sharedPreferences = requireActivity().getSharedPreferences("instagram", 0)
        val userId = sharedPreferences.getString("id", "")
        val username = sharedPreferences.getString("username", "")

        if (userId != null) {
            if (username != null) {
                postViewModel.likePost(
                    userId,
                    item._id,
                    !item.listLike.any { it.username == username },
                    AuthorData(username, "", "", Gender.MALE, "", "")
                )
            }
        }
    }

    override fun onItemDoubleClicked(item: PostData) {
        val sharedPreferences = requireActivity().getSharedPreferences("instagram", 0)
        val userId = sharedPreferences.getString("id", "")
        val username = sharedPreferences.getString("username", "")

        if (userId != null) {
            if (username != null) {
                postViewModel.likePost(
                    userId,
                    item._id,
                    !item.listLike.any { it.username == username },
                    AuthorData(username, "", "", Gender.MALE, "", "")
                )
            }
        }
    }

    override fun onMoreClicked(item: PostData) {
        TODO("Not yet implemented")
    }
}