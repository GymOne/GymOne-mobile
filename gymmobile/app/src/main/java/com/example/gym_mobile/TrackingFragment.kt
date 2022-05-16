package com.example.gym_mobile

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.example.gym_mobile.Entities.Workout.WorkoutExercise
import com.example.gym_mobile.Entities.Workout.WorkoutSession
import com.example.gym_mobile.Entities.Workout.WorkoutSet
import com.example.gym_mobile.Model.User
import com.example.gym_mobile.Repository.ApiConnector
import com.example.gym_mobile.Repository.WorkoutRepo
import com.example.gym_mobile.databinding.FragmentTrackingBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_add_set.*
import kotlinx.android.synthetic.main.fragment_tracking.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class TrackingFragment : Fragment() {

    private lateinit var binding: FragmentTrackingBinding

    lateinit var weights: TextInputEditText

    lateinit var reps: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrackingBinding.inflate(inflater, container, false)
//----------Date Picker
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePicker.addOnPositiveButtonClickListener {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            val selectedDate = simpleDateFormat.format(datePicker.selection)
            loadWorkout(selectedDate);
        }


        binding.selectDate.setOnClickListener{
            activity?.let { it1 -> datePicker.show(it1.supportFragmentManager, "Material Date Picker") }
        }
//----------Date Picker

        var input = ""
        var myInputField = view?.findViewById<TextInputEditText>(R.id.weightInput)
        if (myInputField != null) {
            myInputField.doOnTextChanged { text, start, before, count ->
            }
        }

        return binding.root
    }

    fun loadWorkout(date:String){
        val workoutRepo = ApiConnector.getInstance().create(WorkoutRepo::class.java)

            workoutRepo.getWorkoutSession(User.getUser()?.id,date).enqueue(object:
                Callback<WorkoutSession> {
                override fun onResponse(
                    call: Call<WorkoutSession>,
                    response: Response<WorkoutSession>

                ) {
                    val session = response.body() as WorkoutSession
                    val adapter = WorkoutExerciseAdapter(
                        context as Context,
                        session.workouts as MutableList<WorkoutExercise>
                    )
                    lv_workouts.adapter = adapter;

                }

                override fun onFailure(call: Call<WorkoutSession>, t: Throwable) {

                }

            })
    }

    internal class WorkoutExerciseAdapter(context: Context, private val workout:MutableList<WorkoutExercise>) : ArrayAdapter<WorkoutExercise>(context,0,workout){

        private val colours = intArrayOf(
            Color.parseColor("#AAAAAA"),
            Color.parseColor("#CCCCCC")
        )

        override fun getView(position: Int, v: View?, parent: ViewGroup): View {
            var v1: View? = v
            if (v1 == null) {
                val mInflater = LayoutInflater.from(context)
                v1 = mInflater.inflate(R.layout.adapter_workout_exercise_layout, null)

            }
            val resView: View = v1!!
            resView.setBackgroundColor(colours[position % colours.size])
            val e = workout[position]
            val nameView = resView.findViewById<TextView>(R.id.tv_workoutExerciseName)
            nameView.text = e.exercise.name
            val fakeSets = mutableListOf(WorkoutSet(_id = "sa",weight = 10,reps = 10),WorkoutSet(_id = "sa",weight = 10,reps = 10),WorkoutSet(_id = "sa",weight = 10,reps = 10))
            val setView = resView.findViewById<ListView>(R.id.lv_exerciseSet)
            val adapter = WorkoutExerciseSetAdapter(
                context,
                fakeSets
            )
            setView.adapter = adapter;


            return resView
        }
    }

    internal class WorkoutExerciseSetAdapter(context: Context, private val exerciseSet:MutableList<WorkoutSet>) : ArrayAdapter<WorkoutSet>(context,0,exerciseSet){

        private val colours = intArrayOf(
            Color.parseColor("#AAAAAA"),
            Color.parseColor("#CCCCCC")
        )

        override fun getView(position: Int, v: View?, parent: ViewGroup): View {
            var v1: View? = v
            if (v1 == null) {
                val mInflater = LayoutInflater.from(context)
                v1 = mInflater.inflate(R.layout.adapter_workout_sets_layout, null)

            }
            val resView: View = v1!!
            //resView.setBackgroundColor(colours[position % colours.size])
            val set = exerciseSet[position]
            val weightTextView = resView.findViewById<TextView>(R.id.tv_weight)
            val repsTextView = resView.findViewById<TextView>(R.id.tv_reps)
            weightTextView.text = set.weight.toString()
            repsTextView.text = set.reps.toString()
            return resView
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGoToExercises.setOnClickListener {
            findNavController().navigate(R.id.action_trackingFragment_to_exercisesFragment)
        }
        weightInput
        addNewSet.setOnClickListener(){
            val newFragment = onCreateDialog(view,savedInstanceState)
            newFragment.show()
        }
    }

    private fun onCreateDialog(view: View, savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;

            val DialogView = inflater.inflate(R.layout.fragment_add_set, null)

            weights = DialogView.findViewById(R.id.weightInput)
            reps = DialogView.findViewById(R.id.setInput)
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(DialogView)

            builder.setPositiveButton("Save") { dialog, which ->

                    println(weights.text)
                    println(reps.text)
                Toast.makeText(binding.root.context, weights.text.toString() + "" +reps.text.toString() , Toast.LENGTH_SHORT).show()

            }

            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
                Toast.makeText(binding.root.context, "Canceled", Toast.LENGTH_SHORT).show()
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}