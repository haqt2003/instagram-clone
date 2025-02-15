package com.example.instagram.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.instagram.databinding.FragmentMoreBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MoreBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentMoreBottomSheetBinding
    private var postId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            postId = it.getString(ARG_POST_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.buttonClose.setOnClickListener {
//            dismiss()
//        }
    }

    companion object {
        private const val ARG_POST_ID = "post_id"
        @JvmStatic
        fun newInstance(postId: String) =
            MoreBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_POST_ID, postId)
                }
            }
    }
}