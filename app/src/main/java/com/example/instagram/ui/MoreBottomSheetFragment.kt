package com.example.instagram.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.instagram.databinding.CustomDialogBinding
import com.example.instagram.databinding.FragmentMoreBottomSheetBinding
import com.example.instagram.viewmodels.PostViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MoreBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentMoreBottomSheetBinding
    private var userId: String? = null
    private var postId: String? = null
    private var username: String? = null
    private val postViewModel: PostViewModel by lazy {
        requireActivity().getViewModel<PostViewModel>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString(ARG_USER_ID)
            postId = it.getString(ARG_POST_ID)
            username = it.getString(ARG_USERNAME)
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

        binding.clEdit.setOnClickListener {
            val intent = Intent(requireContext(), EditPostActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("postId", postId)
            intent.putExtra("username", username)
            startActivity(intent)
            dismiss()
        }

        binding.clDelete.setOnClickListener {
            showDialog {
                postViewModel.deletePost(userId.toString(),postId.toString())
                dismiss()
            }
        }
    }

    companion object {
        private const val ARG_USER_ID = "user_id"
        private const val ARG_POST_ID = "post_id"
        private const val ARG_USERNAME = "username"
        @JvmStatic
        fun newInstance(userId: String, postId: String, username: String) =
            MoreBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USER_ID, userId)
                    putString(ARG_POST_ID, postId)
                    putString(ARG_USERNAME, username)
                }
            }
    }

    private fun showDialog(onConfirm: () -> Unit) {
        val dialog = Dialog(requireContext())
        val dialogBinding = CustomDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBinding.clCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.clConfirm.setOnClickListener {
            onConfirm()
            dialog.dismiss()
        }

        dialog.show()
    }
}