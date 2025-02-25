package com.example.instagram.ui

import android.annotation.SuppressLint
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
import com.example.instagram.databinding.FragmentSettingBinding
import com.example.instagram.viewmodels.UserViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
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
        val sharedPreferences = requireActivity().getSharedPreferences("instagram", 0)
        val username = sharedPreferences.getString("username", "")
        binding = FragmentSettingBinding.inflate(inflater, container, false)

        binding.tvLogout.setOnClickListener {
            showDialog()
        }

        binding.ivBack.setOnClickListener {
            userViewModel.getUser(username.toString())
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.tvEdit.setOnClickListener {
            val intent = Intent(requireContext(), UpdateUserActivity::class.java)
            startActivity(intent)
        }

        binding.tvLanguage.setOnClickListener {
            val intent = Intent(requireContext(), LanguageActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SettingFragment()
    }

    @SuppressLint("SetTextI18n")
    private fun showDialog() {
        val dialog = Dialog(requireContext())
        val dialogBinding = CustomDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBinding.tvTitle.text = getString(R.string.sign_out_title)
        dialogBinding.tvContent.visibility = View.GONE
        dialogBinding.tvConfirm.text = getString(R.string.sign_out)

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