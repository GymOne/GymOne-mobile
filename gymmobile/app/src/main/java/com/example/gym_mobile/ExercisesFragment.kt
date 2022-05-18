package com.example.gym_mobile

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.gym_mobile.Entities.Exercise
import com.example.gym_mobile.Model.DataStor
import com.example.gym_mobile.Model.User
import com.example.gym_mobile.Repository.ApiConnector
import com.example.gym_mobile.Repository.ExercisesRepo
import com.example.gym_mobile.Repository.WorkoutRepo
import com.example.gym_mobile.databinding.FragmentExercisesBinding
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.create_new_exercise_layout.*
import kotlinx.android.synthetic.main.fragment_exercises.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExercisesFragment : Fragment() {

    private lateinit var binding: FragmentExercisesBinding

    lateinit var exerciseName: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentExercisesBinding.inflate(inflater,container,false)

        var input = ""
        var myInputField = view?.findViewById<TextInputEditText>(R.id.exerciseNameInput)
        if (myInputField != null) {
            myInputField.doOnTextChanged { text, start, before, count ->
            }
        }


        return binding.root
    }

    private lateinit var listView: ListView;

    fun test(){

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGoBackFromExercises.setOnClickListener {
            findNavController().navigate(R.id.action_exercisesFragment_to_trackingFragment)
            // Use the Kotlin extension in the fragment-ktx artifact
        }
//        binding.btnGoToCreateNewExercise.setOnClickListener {
//            findNavController().navigate(R.id.action_exercisesFragment_to_createNewExerciseFragment)
//        }


        exerciseNameInput
        btnGoToCreateNewExercise.setOnClickListener(){
            val newFragment = onCreateDialog(view,savedInstanceState)
            newFragment.show()
        }

        listView = view.findViewById(R.id.exerciseList);


        val exercisesRepo = ApiConnector.getInstance().create(ExercisesRepo::class.java)

        exercisesRepo.getExercisesByUserId(User.getUser()?.id).enqueue(object:
            Callback<List<Exercise>>{
            override fun onResponse(
                call: Call<List<Exercise>>,
                response: Response<List<Exercise>>
            ) {
                val adapter = ExerciseAdapter(context as Context,
                    response.body() as MutableList<Exercise>,
                    fun(cause: String, excercise: Exercise){
                        setFragmentResult("requestKey", bundleOf(
                            Pair("bundleKey", excercise),
                            Pair("cause", cause),
                        ))
                        return
                    }
                )
                listView.adapter = adapter
            }

            override fun onFailure(call: Call<List<Exercise>>, t: Throwable) {
                listView.adapter = null
            }

        })



        binding.searchView.setOnQueryTextListener(object  : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                userAdapter.filter.filter(newText)
                return false
            }
        })

    }

     class ExerciseAdapter(
         context: Context,
         private val exercise: MutableList<Exercise>,
         private val setResults: (String,Exercise) -> Unit
     ) : ArrayAdapter<Exercise>(context,0,exercise){

        private val colours = intArrayOf(
            Color.parseColor("#AAAAAA"),
            Color.parseColor("#CCCCCC")
        )

        override fun getView(position: Int, v: View?, parent: ViewGroup): View {
            var v1: View? = v
            if (v1 == null) {
                val mInflater = LayoutInflater.from(context)
                v1 = mInflater.inflate(R.layout.adapter_exercise_layout, null)
            }
            val buttonAdd = v1?.findViewById<Button>(R.id.btn_add)

            buttonAdd?.setOnClickListener { view ->
                DataStor.addExcercise(exercise[position])
                setResults("AddExcercise",exercise[position])

//                var createWorkoutSessionDto = User.getUser()?.let { it1 -> CreateWorkoutSessionDto(it1.id, getSele) }
                val workoutApi = ApiConnector.getInstance().create(WorkoutRepo::class.java)

                GlobalScope.launch {
                    //val result = workoutApi.createWorkoutSession(createWorkoutSessionDto)
//                    if(result.isSuccessful){
//                        view.findNavController().navigate(R.id.action_exercisesFragment_to_trackingFragment)
//                    }
                }

//                view.findNavController().navigate(R.id.action_exercisesFragment_to_trackingFragment)
            }
            val buttonRemove = v1?.findViewById<Button>(R.id.btn_remove)
            buttonRemove?.setOnClickListener { view ->
                setResults("RemoveExcercise",exercise[position])
                DataStor.removeExcercise(exercise[position])
                view.findNavController().navigate(R.id.action_exercisesFragment_to_trackingFragment)
            }
            val resView: View = v1!!
            resView.setBackgroundColor(colours[position % colours.size])
            val e = exercise[position]
            val nameView = resView.findViewById<TextView>(R.id.tv_exercise)
            nameView.text = e.name
            return resView
        }
    }


    private fun onCreateDialog(view: View, savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;

            val DialogView = inflater.inflate(R.layout.create_new_exercise_layout, null)

            exerciseName = DialogView.findViewById(R.id.exerciseNameInput)
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(DialogView)

            builder.setPositiveButton("Save") { dialog, which ->

                    println(exerciseName.text)
                Toast.makeText(binding.root.context, exerciseName.text.toString() + " created" , Toast.LENGTH_SHORT).show()

            }

            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
                Toast.makeText(binding.root.context, "Canceled", Toast.LENGTH_SHORT).show()
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


}