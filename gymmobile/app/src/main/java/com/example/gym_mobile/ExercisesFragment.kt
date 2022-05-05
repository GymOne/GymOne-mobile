package com.example.gym_mobile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import androidx.navigation.fragment.findNavController
import com.example.gym_mobile.databinding.FragmentExercisesBinding

class ExercisesFragment : Fragment() {

    private lateinit var binding: FragmentExercisesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentExercisesBinding.inflate(inflater,container,false)
        return binding.root
    }

    private lateinit var listView: ListView;
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGoBackFromExercises.setOnClickListener {
            findNavController().navigate(R.id.action_exercisesFragment_to_trackingFragment)
        }
        binding.btnGoToCreateNewExercise.setOnClickListener {
            findNavController().navigate(R.id.action_exercisesFragment_to_createNewExerciseFragment)
        }
        listView = view.findViewById(R.id.exerciseList);

        val user = arrayOf("Squat","Leg press","Lunge","Deadlift","Leg extension","Leg curl","Standing calf raise","Seated calf raise","Hip adductor","Bench press",
            "Chest fly","Push-up","Pull-down","Pull-up","Bent-over row","Shoulder press","Lateral raise","Biceps curl","Triceps extension")

        val userAdapter  = ArrayAdapter(
            context as Context,
            android.R.layout.simple_list_item_1,
            user
        )

        listView.adapter = userAdapter;


        binding.searchView.setOnQueryTextListener(object  : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                if (user.contains(query)){

                    userAdapter.filter.filter(query)

                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                userAdapter.filter.filter(newText)
                return false
            }
        })

    }
}