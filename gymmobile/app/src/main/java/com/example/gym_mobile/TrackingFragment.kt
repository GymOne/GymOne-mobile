package com.example.gym_mobile

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
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
import java.text.SimpleDateFormat

class TrackingFragment : Fragment() {

    private lateinit var binding: FragmentTrackingBinding

    lateinit var workoutExerciseAdapter:WorkoutRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrackingBinding.inflate(inflater, container, false)
//----------Date Picker

//----------Date Picker
        setFragmentResultListener("requestKey") { requestKey, bundle ->

            // We use a String here, but any type that can be put in a Bundle is supported
            val result = bundle.getParcelable<Exercise>("bundleKey")
            val cause = bundle.getString("cause")
            if (result != null) {
                println(cause)
                println(result.name)
            }

            // Do something with the result
        }


        var input = ""
        var myInputField = view?.findViewById<TextInputEditText>(R.id.weightInput)
        if (myInputField != null) {
            myInputField.doOnTextChanged { text, start, before, count ->
            }
        }

        return binding.root
    }

    private fun loadWorkoutExercises(date:String){

                val workoutRepo = ApiConnector.getInstance().create(WorkoutRepo::class.java)
        var items: List<WorkoutExercise> = ArrayList()
            workoutRepo.getWorkoutSession(User.getUser()?.id,date).enqueue(object:
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
                val workoutExerciseClick = workoutExerciseAdapter.items.get(position).exercise.name;
                Toast.makeText(context as Context,"clicked on $workoutExerciseClick",Toast.LENGTH_SHORT).show()
            }

        })

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGoToExercises.setOnClickListener {
            findNavController().navigate(R.id.action_trackingFragment_to_exercisesFragment)
        }
//        addNewSet.setOnClickListener(){
//            val newFragment = onCreateDialog(view,savedInstanceState)
//            newFragment.show()
//        }
        initRecyclerView()

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

        datePicker.addOnPositiveButtonClickListener {
            val selectedDate = simpleDateFormat.format(datePicker.selection)
            selectDate.text = selectedDate
            loadWorkoutExercises(selectedDate);
        }
        val selectedDate = simpleDateFormat.format(datePicker.selection)
        selectDate.text = selectedDate
        loadWorkoutExercises(selectedDate);

        binding.selectDate.setOnClickListener{
            activity?.let { it1 -> datePicker.show(it1.supportFragmentManager, "Material Date Picker") }
        }
    }

    private fun onCreateDialog(view: View, savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;

            val DialogView = inflater.inflate(R.layout.selected_exercise_layout, null)

//            weights = DialogView.findViewById(R.id.weightInput)
//            reps = DialogView.findViewById(R.id.setInput)
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(DialogView)

//            builder.setPositiveButton("Save") { dialog, which ->
//
//                    println(weights.text)
//                    println(reps.text)
//                Toast.makeText(binding.root.context, weights.text.toString() + "" +reps.text.toString() , Toast.LENGTH_SHORT).show()
//
//            }
//
//            builder.setNegativeButton("Cancel") { dialog, which ->
//                dialog.dismiss()
//                Toast.makeText(binding.root.context, "Canceled", Toast.LENGTH_SHORT).show()
//            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}