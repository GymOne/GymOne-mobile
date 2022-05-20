package com.example.gym_mobile

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.navigation.findNavController
import com.example.gym_mobile.Dto.CreateExerciseDto
import com.example.gym_mobile.Dto.CreateWorkoutExerciseDto
import com.example.gym_mobile.Dto.CreateWorkoutExerciseSetDto
import com.example.gym_mobile.Dto.CreateWorkoutSessionDto
import com.example.gym_mobile.Entities.Exercise
import com.example.gym_mobile.Entities.Workout.WorkoutExercise
import com.example.gym_mobile.Entities.Workout.WorkoutSession
import com.example.gym_mobile.Model.DataStor
import com.example.gym_mobile.Model.User
import com.example.gym_mobile.Repository.ApiConnector
import com.example.gym_mobile.Repository.ExercisesRepo
import com.example.gym_mobile.Repository.WorkoutRepo
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.activity_exercise.btnGoBackFromExercises
import kotlinx.android.synthetic.main.activity_exercise.btnGoToCreateNewExercise
import kotlinx.android.synthetic.main.create_new_exercise_layout.*
import kotlinx.android.synthetic.main.fragment_exercises.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.logging.Logger

class ExerciseActivity : AppCompatActivity() {

    lateinit var exerciseName: TextInputEditText
    lateinit var selectedDate:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        selectedDate = intent.getStringExtra("date").toString()


        var input = ""
        var myInputField = findViewById<TextInputEditText>(R.id.exerciseNameInput)
        if (myInputField != null) {
            myInputField.doOnTextChanged { text, start, before, count ->
            }
        }

        loadExercises()


        btnGoBackFromExercises.setOnClickListener{
            this.finish()
        }

        btnGoToCreateNewExercise.setOnClickListener(){

            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.create_new_exercise_layout)
            val createBtn = dialog.findViewById<Button>(R.id.btn_create)
            val cancelBtn = dialog.findViewById<Button>(R.id.btn_cancel)
            val exerciseName = dialog.findViewById<TextView>(R.id.exerciseNameInput)

            createBtn.setOnClickListener{
                val name = exerciseName.text.toString()
                val userId = User.getUserId() ?: return@setOnClickListener
                if(name.isNotEmpty()) {
                    val service = ApiConnector.getInstance().create(ExercisesRepo::class.java)
                    val exerciseToCreate = CreateExerciseDto(userId, name)
                    CoroutineScope(Dispatchers.IO).launch {
                        val response = service.createExercise(exerciseToCreate)
                        if (response.isSuccessful) {
                            loadExercises()
                            dialog.dismiss()
                        }
                    }
                }
            }

            cancelBtn.setOnClickListener{
                dialog.dismiss()
            }

            dialog.show()
        }


    }

    fun loadExercises(){
        val exercisesRepo = ApiConnector.getInstance().create(ExercisesRepo::class.java)
        exercisesRepo.getExercisesByUserId(User.getUserId()).enqueue(object:
            Callback<List<Exercise>> {
            override fun onResponse(
                call: Call<List<Exercise>>,
                response: Response<List<Exercise>>
            ) {
                val adapter = ExerciseAdapter(this@ExerciseActivity, selectedDate, response.body() as MutableList<Exercise>)
                lv_exercise.adapter = adapter
            }
            override fun onFailure(call: Call<List<Exercise>>, t: Throwable) {
                lv_exercise.adapter = null
            }

        })
    }

    class ExerciseAdapter(
        context: Context,
        val selectedDate:String,
        private var exercise: MutableList<Exercise>,
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

            val resView: View = v1!!
            //resView.setBackgroundColor(colours[position % colours.size])
            val btnAdd = resView.findViewById<Button>(R.id.btn_add)
            val btnRemove = resView.findViewById<Button>(R.id.btn_remove)
            val nameView = resView.findViewById<TextView>(R.id.tv_exercise)

            val e = exercise[position]

            nameView.text = e.name
            btnRemove.setOnClickListener{
                val service = ApiConnector.getInstance().create(ExercisesRepo::class.java)

                CoroutineScope(Dispatchers.IO).launch {
                    val response = service.deleteExerciseById(e._id)
                    if(response.isSuccessful){
                        val activity = context as Activity

                        activity.runOnUiThread(Runnable {
                            exercise.removeAt(position)
                            notifyDataSetChanged()
                       })
                    }
                }

            }

            btnAdd.setOnClickListener{
                val workoutRepo = ApiConnector.getInstance().create(WorkoutRepo::class.java)

                workoutRepo.getWorkoutSession(User.getUserId(),selectedDate).enqueue(object:
                    Callback<WorkoutSession> {
                    override fun onResponse(
                        call: Call<WorkoutSession>,
                        response: Response<WorkoutSession>

                    ) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val workoutSession = response.body() ?: return@launch
                            val createWorkoutExerciseDto = CreateWorkoutExerciseDto(workoutSessionId = workoutSession._id,e._id)
                            workoutRepo.createWorkoutExercise(createWorkoutExerciseDto)
                        }
                    }

                    override fun onFailure(call: Call<WorkoutSession>, t: Throwable) {
                        val userId = User.getUserId() ?: return
                        val createWorkoutSessionDto = CreateWorkoutSessionDto(userId,selectedDate)
                        CoroutineScope(Dispatchers.IO).launch {
                            val response = workoutRepo.createWorkoutSession(createWorkoutSessionDto)
                            if(response.isSuccessful){
                                workoutRepo.getWorkoutSession(User.getUserId(),selectedDate).enqueue(object:
                                    Callback<WorkoutSession> {
                                    override fun onResponse(
                                        call: Call<WorkoutSession>,
                                        response: Response<WorkoutSession>
                                    ) {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            val workoutSession = response.body() ?: return@launch
                                            val createWorkoutExerciseDto = CreateWorkoutExerciseDto(workoutSessionId = workoutSession._id,e._id)
                                            workoutRepo.createWorkoutExercise(createWorkoutExerciseDto)
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<WorkoutSession>,
                                        t: Throwable
                                    ) {

                                    }
                                })
                            }
                        }
                    }
                })
            }
            return resView
        }
    }

    fun onCreateDialog(): Dialog {

        return let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater

            val dialogView = R.layout.create_new_exercise_layout

            builder.setView(dialogView)

            val textView = findViewById<TextView>(R.id.exerciseNameInput)

            builder.setPositiveButton("Save") { dialog, which ->
                Toast.makeText(this,  textView.text , Toast.LENGTH_SHORT).show()

            }

            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show()
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}
