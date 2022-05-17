package com.example.gym_mobile

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.gym_mobile.Entities.Exercise
import com.example.gym_mobile.Model.User
import com.example.gym_mobile.Repository.ApiConnector
import com.example.gym_mobile.Repository.ExercisesRepo
import com.example.gym_mobile.databinding.FragmentExercisesBinding
import com.example.gym_mobile.Model.DataStor;
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

    fun test(){

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGoBackFromExercises.setOnClickListener {
            findNavController().navigate(R.id.action_exercisesFragment_to_trackingFragment)

            // Use the Kotlin extension in the fragment-ktx artifact
        }
        binding.btnGoToCreateNewExercise.setOnClickListener {
            findNavController().navigate(R.id.action_exercisesFragment_to_createNewExerciseFragment)
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
                            Pair("cause", cause),))
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
                view.findNavController().navigate(R.id.action_exercisesFragment_to_trackingFragment)
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

}