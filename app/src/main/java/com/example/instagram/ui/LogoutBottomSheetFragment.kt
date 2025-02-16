package com.example.instagram.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.instagram.databinding.FragmentLogoutBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LogoutBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentLogoutBottomSheetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogoutBottomSheetBinding.inflate(inflater, container, false)

        val sharedPreferences = requireActivity().getSharedPreferences("instagram", Context.MODE_PRIVATE)
        binding.btLogout.setOnClickListener {
            sharedPreferences.edit().clear().apply()
            dismiss()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LogoutBottomSheetFragment()
    }
}