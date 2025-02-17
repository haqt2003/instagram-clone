package com.example.instagram.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
//    private var currentPage = 1
//    private var hasMoreDataUser = true

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

        val sharedPreferences = requireActivity().getSharedPreferences("instagram", 0)
        val username = sharedPreferences.getString("username", "")

        adapter = HomeAdapter(this)
        binding.rvPosts.adapter = adapter
        binding.rvPosts.layoutManager = LinearLayoutManager(requireContext())

        binding.tvUsername.text = username

        postViewModel.msg.observe(viewLifecycleOwner) {
            if (it != null) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        postViewModel.userPosts.observe(viewLifecycleOwner) {
            adapter.submitData(it)
        }

//        postViewModel.hasMoreDataUser.observe(viewLifecycleOwner) {
//            hasMoreDataUser = it
//        }
//
//        postViewModel.currentPageUserPost.observe(viewLifecycleOwner) {
//            currentPage = it
//        }

        postViewModel.getUserPosts(username.toString())

        binding.ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

//        binding.rvPosts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//
//                if (!recyclerView.canScrollVertically(1)) {
//                    if (hasMoreDataUser) {
//                        loadNextPage()
//                    }
//                }
//            }
//        })

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
        val sharedPreferences = requireActivity().getSharedPreferences("instagram", 0)
        val userId = sharedPreferences.getString("id", "")
        val username = sharedPreferences.getString("username", "")

        val bottomSheetFragment = MoreBottomSheetFragment.newInstance(userId.toString(), item._id, username.toString())
        bottomSheetFragment.show(requireActivity().supportFragmentManager, bottomSheetFragment.tag)
    }

//    private fun loadNextPage() {
//        val sharedPreferences = requireActivity().getSharedPreferences("instagram", 0)
//        val username = sharedPreferences.getString("username", "")
//        if (hasMoreDataUser) {
//            currentPage++
//            postViewModel.getUserPosts(username.toString(), currentPage)
//        }
//    }
}