package com.example.instagram.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagram.adapters.LikeAdapter
import com.example.instagram.data.models.PostData
import androidx.core.widget.addTextChangedListener
import com.example.instagram.R
import com.example.instagram.data.models.AuthorData
import com.example.instagram.databinding.FragmentListLikeBinding
import com.example.instagram.viewmodels.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.getViewModel

class ListLikeFragment : BottomSheetDialogFragment(), LikeAdapter.OnClickListener {
    private lateinit var binding: FragmentListLikeBinding
    private lateinit var adapter: LikeAdapter
    private val userViewModel: UserViewModel by lazy {
        requireActivity().getViewModel<UserViewModel>()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListLikeBinding.inflate(inflater, container, false)

        val postData = arguments?.getSerializable("postData") as? PostData

        adapter = LikeAdapter(this)
        binding.rvLikes.adapter = adapter
        binding.rvLikes.layoutManager = LinearLayoutManager(requireContext())

        if (postData != null) {
            adapter.submitData(postData.listLike)
        }

        binding.etSearch.addTextChangedListener {
            val searchQuery = binding.etSearch.text.toString()

            if (searchQuery.isNotEmpty()) {
                val filteredList = postData?.listLike?.filter { like ->
                    like.username.contains(searchQuery, ignoreCase = true)
                            || like.name.contains(searchQuery, ignoreCase = true)
                }
                adapter.submitData(filteredList ?: emptyList())
            } else {
                adapter.submitData(postData?.listLike ?: emptyList())
            }
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = ListLikeFragment()
    }

    override fun onItemClicked(item: AuthorData) {
        val sharedPreferences = requireActivity().getSharedPreferences("instagram", 0)
        val username = sharedPreferences.getString("username", "")

        if (item.username == username) {
            val profileFragment = ProfileFragment.newInstance()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fcv_main, profileFragment)
                .addToBackStack(null)
                .commit()
        } else {
            val otherUserFragment = OtherUserFragment.newInstance(item.username)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fcv_main, otherUserFragment)
                .addToBackStack(null)
                .commit()
        }

        dismiss()
    }
}