package com.example.instagram.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.instagram.R
import com.example.instagram.adapters.GridSpacingItemDecoration
import com.example.instagram.adapters.ProfileAdapter
import com.example.instagram.data.models.PostData
import com.example.instagram.databinding.FragmentOtherUserBinding
import com.example.instagram.viewmodels.PostViewModel
import com.example.instagram.viewmodels.UserViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class OtherUserFragment : Fragment(), ProfileAdapter.OnClickListener {
    private lateinit var binding: FragmentOtherUserBinding
    private val postViewModel: PostViewModel by lazy {
        requireActivity().getViewModel<PostViewModel>()
    }
    private val userViewModel: UserViewModel by lazy {
        requireActivity().getViewModel<UserViewModel>()
    }
    private lateinit var adapter: ProfileAdapter
    private var currentPage = 1
    private var hasMoreDataUser = true
    private var username: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(ARG_USERNAME)
        }
        postViewModel.getUserPosts(username.toString(), currentPage, 15)
        userViewModel.getUser(username.toString())
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOtherUserBinding.inflate(inflater, container, false)

        val preUsername = arguments?.getString("preUsername")

        adapter = ProfileAdapter(this)

        with(binding) {
            rvPosts.adapter = adapter
            rvPosts.layoutManager = GridLayoutManager(requireContext(), 3)
            rvPosts.addItemDecoration(GridSpacingItemDecoration(3, 4, false))

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

            ivBack.setOnClickListener {
                postViewModel.getUserPosts(preUsername.toString(), 1, 10)
                requireActivity().supportFragmentManager.popBackStack()
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

            postViewModel.userPosts.observe(viewLifecycleOwner) {
                if (it.isNullOrEmpty()) {
                    binding.tvNone.visibility = View.VISIBLE
                    adapter.submitData(emptyList())
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
        }

        userViewModel.getUser(username.toString())

        return binding.root
    }

    companion object {
        private const val ARG_USERNAME = "username"
        @JvmStatic
        fun newInstance(param1: String) =
            OtherUserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERNAME, param1)
                }
            }
    }

    override fun onClickItem(item: PostData) {
        val detailFragment = DetailFragment.newInstance(item._id, item.author.username)

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fcv_main, detailFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun loadNextPage() {
        if (hasMoreDataUser) {
            currentPage++
            postViewModel.getUserPosts(username.toString(), currentPage, 20)
        }
    }
}