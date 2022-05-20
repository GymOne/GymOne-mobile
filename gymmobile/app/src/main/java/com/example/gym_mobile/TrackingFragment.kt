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
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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


        setFragmentResultListener("requestKey") { requestKey, bundle ->
        }

        var myInputField = view?.findViewById<TextInputEditText>(R.id.exerciseNameInput)
        if (myInputField != null) {
            myInputField.doOnTextChanged { text, start, before, count ->
            }
        }
        return binding.root
    }

    private fun loadWorkoutExercises(){
                val workoutRepo = ApiConnector.getInstance().create(WorkoutRepo::class.java)
        val selDate = getSelectedDate()

        workoutRepo.getWorkoutSession(User.getUserId(),selDate).enqueue(object:
                Callback<WorkoutSession> {
                override fun onResponse(
                    call: Call<WorkoutSession>,
                    response: Response<WorkoutSession>

                ) {
                    val session = response.body() as WorkoutSession

                    workoutExerciseAdapter.submitList(session.workouts)
                    workoutExerciseAdapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<WorkoutSession>, t: Throwable) {
                    workoutExerciseAdapter.submitList(emptyList())
                    workoutExerciseAdapter.notifyDataSetChanged()
                }
            })

    }

    override fun onStart() {
        loadWorkoutExercises()
        super.onStart()
    }

    private fun initRecyclerView(){
        recycler_view.recycledViewPool.clear()
        recycler_view.layoutManager = LinearLayoutManager(context as Context)
        if(recycler_view.itemDecorationCount == 0) {
            val topSpaceDecoration = TopItemSpaceDecoration(20)
            recycler_view.addItemDecoration(topSpaceDecoration)
        }
            workoutExerciseAdapter = WorkoutRecyclerAdapter()
        recycler_view.swapAdapter(workoutExerciseAdapter,true)

        (recycler_view.adapter as WorkoutRecyclerAdapter).setOnButtonClickListener(object : WorkoutRecyclerAdapter.onButtonClickListener{
            override fun onButtonClick(position: Int) {
                val workoutExercise = workoutExerciseAdapter.items.get(position)

                val service = ApiConnector.getInstance().create(WorkoutRepo::class.java)

                CoroutineScope(Dispatchers.IO).launch {
                    val response = service.deleteWorkoutExercise(workoutExercise._id)
                    if(response.isSuccessful){
                        loadWorkoutExercises()
                    }
                }
            }
        })
        (recycler_view.adapter as WorkoutRecyclerAdapter).setOnItemClickListener(object : WorkoutRecyclerAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val workoutExercise = workoutExerciseAdapter.items.get(position);
                val intent = Intent(activity,UpdateSetActivity::class.java)
                intent.putExtra("workoutExercise",workoutExercise as Serializable)
                startActivity(intent)

            }

        })

    }
    public fun getSelectedDate():String{
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val selectedDate = simpleDateFormat.format(datePicker.selection)
        return selectedDate
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGoToExercises.setOnClickListener {

//            val intent = Intent (getActivity(), ExercisesFragment::class.java)
//            intent.putExtra("date",getSelectedDate());
//            activity?.startActivity(intent)
//
//            val exercisesFragment
//            activity?.getSupportFragmentManager()?.beginTransaction()
//                ?.replace(R.id.profileFragment, exercisesFragment, "fragmnetId")
//                ?.commit();

//            findNavController().navigate(R.id.action_trackingFragment_to_exercisesFragment)

            val intent = Intent(activity,ExerciseActivity::class.java)
            intent.putExtra("date",getSelectedDate())
            startActivity(intent)
        }
//        exerciseNameInput
//        btnGoToCreateNewExercise.setOnClickListener(){
//            val newFragment = onCreateDialog(view,savedInstanceState)
//            newFragment.show()
//        }
        initRecyclerView()
        selectDate.text = getSelectedDate()

        datePicker.addOnPositiveButtonClickListener {
            selectDate.text = getSelectedDate()
            loadWorkoutExercises()
        }

        binding.selectDate.setOnClickListener{
            activity?.let { it1 -> datePicker.show(it1.supportFragmentManager, "Material Date Picker") }
        }
    }

    private fun onCreateDialog(view: View, savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;

            val DialogView = inflater.inflate(R.layout.create_new_exercise_layout, null)

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