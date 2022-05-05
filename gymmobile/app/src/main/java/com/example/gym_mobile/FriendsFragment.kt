package com.example.gym_mobile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.gym_mobile.databinding.FragmentFriendsBinding
import com.example.gym_mobile.databinding.FragmentTrackingBinding

class FriendsFragment : Fragment() {

    private lateinit var binding: FragmentFriendsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGoToFriendRequests.setOnClickListener {
            findNavController().navigate(R.id.action_friendsFragment_to_friendRequestFragment)
        }
    }
}