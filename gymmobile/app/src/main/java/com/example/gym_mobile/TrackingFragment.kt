package com.example.gym_mobile

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gym_mobile.Entities.Exercise
import com.example.gym_mobile.Entities.Workout.WorkoutExercise
import com.example.gym_mobile.Entities.Workout.WorkoutSession
import com.example.gym_mobile.Model.User
import com.example.gym_mobile.RecyclerAdapter.TopItemSpaceDecoration
import com.example.gym_mobile.RecyclerAdapter.WorkoutRecyclerAdapter
import com.example.gym_mobile.Repository.ApiConnector
import com.example.gym_mobile.Repository.WorkoutRepo
import com.example.gym_mobile.databinding.FragmentTrackingBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_tracking.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.text.SimpleDateFormat

class TrackingFragment : Fragment() {

    private lateinit var binding: FragmentTrackingBinding

    lateinit var workoutExerciseAdapter:WorkoutRecyclerAdapter

    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText("Select date")
        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        .build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrackingBinding.inflate(inflater, container, false)

        var myInputField = view?.findViewById<TextInputEditText>(R.id.weightInput)
        if (myInputField != null) {
            myInputField.doOnTextChanged { text, start, before, count ->
            }
        }

        return binding.root
    }

    private fun loadWorkoutExercises(){

                val workoutRepo = ApiConnector.getInstance().create(WorkoutRepo::class.java)
        var items: List<WorkoutExercise> = ArrayList()

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val selectedDate = simpleDateFormat.format(datePicker.selection)
        selectDate.text = selectedDate

            workoutRepo.getWorkoutSession(User.getUser()?.id,selectedDate).enqueue(object:
                Callback<WorkoutSession> {
                override fun onResponse(
                    call: Call<WorkoutSession>,
                    response: Response<WorkoutSession>

                ) {
                    val session = response.body() as WorkoutSession
                    items = session.workouts
                    workoutExerciseAdapter.submitList(items)

                    recycler_view.adapter?.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<WorkoutSession>, t: Throwable) {
                    workoutExerciseAdapter.submitList(items)
                    recycler_view.adapter?.notifyDataSetChanged()
                }

            })
    }

    private fun initRecyclerView(){

        recycler_view.layoutManager = LinearLayoutManager(context as Context)
            val topSpaceDecoration = TopItemSpaceDecoration(20)
        recycler_view.addItemDecoration(topSpaceDecoration)
            workoutExerciseAdapter = WorkoutRecyclerAdapter()
        recycler_view.adapter = workoutExerciseAdapter
        (recycler_view.adapter as WorkoutRecyclerAdapter).setOnItemClickListener(object : WorkoutRecyclerAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val workoutExercise = workoutExerciseAdapter.items.get(position);
                val intent = Intent(activity,UpdateSetActivity::class.java)
                intent.putExtra("workoutExercise",workoutExercise as Serializable)
                startActivity(intent)

            }

        })

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGoToExercises.setOnClickListener {
            findNavController().navigate(R.id.action_trackingFragment_to_exercisesFragment)
        }
        initRecyclerView()
        loadWorkoutExercises();

        datePicker.addOnPositiveButtonClickListener {
            loadWorkoutExercises();
        }

        binding.selectDate.setOnClickListener{
            activity?.let { it1 -> datePicker.show(it1.supportFragmentManager, "Material Date Picker") }
        }
    }
}