package com.example.instagram.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.instagram.databinding.FragmentOtherUserBinding

class OtherUserFragment : Fragment() {
    private lateinit var binding: FragmentOtherUserBinding
    private var username: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(ARG_USERNAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOtherUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        private const val ARG_USERNAME = "username"
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OtherUserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERNAME, param1)
                }
            }
    }
}