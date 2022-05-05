package com.example.gym_mobile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.gym_mobile.databinding.FragmentCreateNewExerciseBinding
import com.example.gym_mobile.databinding.FragmentFriendsBinding

class CreateNewExerciseFragment : Fragment() {

    private lateinit var binding: FragmentCreateNewExerciseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateNewExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGoBackToAllExercises.setOnClickListener {
            findNavController().navigate(R.id.action_createNewExerciseFragment_to_exercisesFragment)
        }
    }
}