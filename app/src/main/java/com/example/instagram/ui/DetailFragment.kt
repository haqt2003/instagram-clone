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
import com.example.instagram.viewmodels.UserViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class DetailFragment : Fragment(), HomeAdapter.OnClickListener {
    private lateinit var binding: FragmentDetailBinding
    private val postViewModel: PostViewModel by lazy {
        requireActivity().getViewModel<PostViewModel>()
    }
    private val userViewModel: UserViewModel by lazy {
        requireActivity().getViewModel<UserViewModel>()
    }
    private lateinit var adapter: HomeAdapter
    private var postId: String? = null
    private var username: String? = null
    private var currentPage = 1
    private var hasMoreDataUser = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            postId = it.getString(ARG_ID)
            username = it.getString(ARG_USERNAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val sharedPreferences = requireActivity().getSharedPreferences("instagram", 0)
        val usernamePrefs = sharedPreferences.getString("username", "")

        binding = FragmentDetailBinding.inflate(inflater, container, false)

        adapter = HomeAdapter(this)
        binding.rvPosts.adapter = adapter
        binding.rvPosts.layoutManager = LinearLayoutManager(requireContext())

        binding.tvUsername.text = username

        postViewModel.msg.observe(viewLifecycleOwner) {
            postViewModel.getUserPosts(username.toString(), currentPage, 10)
        }

        postViewModel.userPosts.observe(viewLifecycleOwner) {
            adapter.submitData(it)
            binding.pbLoading.visibility = View.GONE
            if (it.isEmpty() && username == usernamePrefs) {
                val profileFragment = ProfileFragment.newInstance()

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fcv_main, profileFragment)
                    .commit()
            }

            postId?.let { id ->
                val position = it.indexOfFirst { it._id == id }
                if (position != -1) {
                    binding.rvPosts.post {
                        binding.rvPosts.scrollToPosition(position)
                    }
                }
            }
        }

        postViewModel.hasMoreDataUser.observe(viewLifecycleOwner) {
            hasMoreDataUser = it
        }

        postViewModel.currentPageUserPost.observe(viewLifecycleOwner) {
            currentPage = it
        }

        binding.ivBack.setOnClickListener {
            userViewModel.getUser(username.toString())
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.rvPosts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollVertically(1)) {
                    if (hasMoreDataUser) {
                        binding.pbLoading.visibility = View.VISIBLE
                        loadNextPage()
                    } else {
                        binding.pbLoading.visibility = View.GONE
                    }
                }
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        postViewModel.getUserPosts(username.toString(), currentPage, 10)
        userViewModel.getUser(username.toString())
    }

    companion object {
        private const val ARG_ID = "post_id"
        private const val ARG_USERNAME = "username"

        @JvmStatic
        fun newInstance(postId: String, username: String) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ID, postId)
                    putString(ARG_USERNAME, username)
                }
            }
    }

    override fun onLikeClicked(item: PostData) {
        val sharedPreferences = requireActivity().getSharedPreferences("instagram", 0)
        val userId = sharedPreferences.getString("id", "")
        val username = sharedPreferences.getString("username", "")

        if (userId != null) {
            if (username != null) {
                postId = item._id
                postViewModel.likePost(
                    userId,
                    item._id,
                    item.listLike.any { it.username == username },
                    true
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
                postId = item._id
                postViewModel.likePost(
                    userId,
                    item._id,
                    item.listLike.any { it.username == username },
                    true
                )
            }
        }
    }

    override fun onSeeLikeClicked(item: PostData) {
        val listLikeFragment = ListLikeFragment.newInstance()
        val bundle = Bundle()
        bundle.putSerializable("postData", item)

        listLikeFragment.arguments = bundle

        listLikeFragment.show(childFragmentManager, "list_like")
    }

    override fun onMoreClicked(item: PostData) {
        val sharedPreferences = requireActivity().getSharedPreferences("instagram", 0)
        val username = sharedPreferences.getString("username", "")
        val userId = sharedPreferences.getString("id", "")
        if (username == item.author.username) {
            val moreBottomSheetFragment = MoreBottomSheetFragment.newInstance(
                userId.toString(),
                item._id,
                username.toString()
            )
            moreBottomSheetFragment.show(childFragmentManager, "more_bottom_sheet")
        } else {
            Toast.makeText(requireContext(), "Bạn không phải chủ sở hữu!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onGoToUser(item: PostData) {
        val sharedPreferences = requireActivity().getSharedPreferences("instagram", 0)
        val username = sharedPreferences.getString("username", "")

        if (item.author.username == username) {
            val profileFragment = ProfileFragment.newInstance()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fcv_main, profileFragment)
                .addToBackStack(null)
                .commit()
        } else {
            val otherUserFragment = OtherUserFragment.newInstance(item.author.username)
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.fcv_main, otherUserFragment)
                .addToBackStack(null).commit()
        }

    }

    private fun loadNextPage() {
        val sharedPreferences = requireActivity().getSharedPreferences("instagram", 0)
        val username = sharedPreferences.getString("username", "")
        if (hasMoreDataUser) {
            currentPage++
            postViewModel.getUserPosts(username.toString(), currentPage, 10)
        }
    }
}