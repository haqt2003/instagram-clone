package com.example.instagram.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.adapters.HomeAdapter
import com.example.instagram.data.enums.Gender
import com.example.instagram.data.models.AuthorData
import com.example.instagram.data.models.PostData
import com.example.instagram.databinding.FragmentHomeBinding
import com.example.instagram.viewmodels.PostViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class HomeFragment : Fragment(), HomeAdapter.OnClickListener {
    private lateinit var binding: FragmentHomeBinding
    private val postViewModel: PostViewModel by lazy {
        requireActivity().getViewModel<PostViewModel>()
    }
    private lateinit var adapter: HomeAdapter
    private var currentPage = 1
    private var hasMoreData = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postViewModel.getPosts(currentPage)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        adapter = HomeAdapter(this)
        binding.rvPosts.adapter = adapter
        binding.rvPosts.layoutManager = LinearLayoutManager(requireContext())

        postViewModel.posts.observe(viewLifecycleOwner) {
            adapter.submitData(it)
            binding.pbLoadingStart.visibility = View.GONE
            binding.pbLoading.visibility = View.GONE
        }

        postViewModel.msg.observe(viewLifecycleOwner) {
            currentPage = 1
            postViewModel.getPosts(currentPage)
        }

        postViewModel.hasMoreData.observe(viewLifecycleOwner) {
            hasMoreData = it
        }

        postViewModel.likePostUpdated.observe(viewLifecycleOwner) {
            if (it) {
                postViewModel.getPosts(currentPage)
            }
        }

        binding.rvPosts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollVertically(1)) {
                    if (hasMoreData) {
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

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
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
                    item.listLike.any { it.username == username },
                    false
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
                    item.listLike.any { it.username == username },
                    false
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
                .replace(com.example.instagram.R.id.fcv_main, profileFragment)
                .addToBackStack(null)
                .commit()
        } else {
            val otherUserFragment = OtherUserFragment.newInstance(item.author.username)
            requireActivity().supportFragmentManager.beginTransaction()
                .add(com.example.instagram.R.id.fcv_main, otherUserFragment)
                .addToBackStack(null).commit()
        }
    }

    private fun loadNextPage() {
        if (hasMoreData) {
            currentPage++
            postViewModel.getPosts(currentPage)
        }
    }
}
