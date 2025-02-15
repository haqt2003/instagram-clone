package com.example.instagram.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagram.adapters.HomeAdapter
import com.example.instagram.data.enums.Gender
import com.example.instagram.data.models.AuthorData
import com.example.instagram.data.models.PostData
import com.example.instagram.databinding.FragmentHomeBinding
import com.example.instagram.viewmodels.PostViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), HomeAdapter.OnClickListener {
    private lateinit var binding: FragmentHomeBinding
    private val postViewModel: PostViewModel by viewModel()
    private lateinit var adapter: HomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        }

        postViewModel.getPosts()

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
        val moreBottomSheetFragment = MoreBottomSheetFragment.newInstance(item._id)
        moreBottomSheetFragment.show(childFragmentManager, "more_bottom_sheet")
    }
}
