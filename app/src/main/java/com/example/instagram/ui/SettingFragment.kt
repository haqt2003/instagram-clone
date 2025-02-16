package com.example.instagram.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.instagram.R
import com.example.instagram.databinding.CustomDialogBinding
import com.example.instagram.databinding.FragmentLogoutBottomSheetBinding
import com.example.instagram.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)

        binding.tvLogout.setOnClickListener {
            showDialog()
        }

        binding.ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.tvEdit.setOnClickListener {
            val intent = Intent(requireContext(), UpdateUserActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SettingFragment()
    }

    private fun showDialog() {
        val dialog = Dialog(requireContext())
        val dialogBinding = CustomDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBinding.tvTitle.text = "Đăng xuất khỏi tài khoản của bạn?"
        dialogBinding.tvContent.visibility = View.GONE
        dialogBinding.tvConfirm.text = "Đăng xuất"

        dialogBinding.clCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.clConfirm.setOnClickListener {
            val sharedPreferences = requireActivity().getSharedPreferences("instagram", Context.MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()
            dialog.dismiss()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        dialog.show()
    }
}