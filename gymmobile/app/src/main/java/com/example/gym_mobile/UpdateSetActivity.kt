package com.example.gym_mobile

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.gym_mobile.Dto.CreateWorkoutExerciseSetDto
import com.example.gym_mobile.Entities.Workout.WorkoutExercise
import com.example.gym_mobile.Entities.Workout.WorkoutSet
import com.example.gym_mobile.Repository.ApiConnector
import com.example.gym_mobile.Repository.WorkoutRepo
import kotlinx.android.synthetic.main.activity_update_set.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateSetActivity : AppCompatActivity() {
    lateinit var workoutExercise: WorkoutExercise

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_set)

        workoutExercise = intent.getSerializableExtra("workoutExercise") as WorkoutExercise

        tv_exerciseName.text = workoutExercise.exercise.name

        resetAdapter()

        btnGoBackFromExercises.setOnClickListener{
            this.finish()
        }

        btnPlusWeight.setOnClickListener{
            onAddClick(txt_weight)
        }

        btnPlusReps.setOnClickListener{
            onAddClick(txt_reps)
        }

        btnMinusWeight.setOnClickListener{
            onMinusClick(txt_weight)
        }
        btnMinusReps.setOnClickListener{
            onMinusClick(txt_reps)
        }
        btn_delete.setOnClickListener{
            if(lv_sets.checkedItemCount==1){
            val position = lv_sets.checkedItemPosition
            val item = lv_sets.getItemAtPosition(position) as WorkoutSet
            val service = ApiConnector.getInstance().create(WorkoutRepo::class.java)

            CoroutineScope(Dispatchers.IO).launch {
                val response = service.deleteWorkoutExerciseSet(item._id)
                if(response.isSuccessful){
                    val list = workoutExercise.sets.toMutableList()
                    list.removeAt(position)
                    workoutExercise.sets = list
                    this@UpdateSetActivity.runOnUiThread(java.lang.Runnable {
                        resetAdapter()
                })
            }
            }}
    }

        btn_addset.setOnClickListener{
            val weightString = txt_weight.text.toString()
            val repsString = txt_reps.text.toString()
            if(weightString.isNotEmpty() && repsString.isNotEmpty()){
                val service = ApiConnector.getInstance().create(WorkoutRepo::class.java)
                val workoutSetToCreate = CreateWorkoutExerciseSetDto(workoutExerciseId = workoutExercise._id,weight = weightString,reps = repsString)
                CoroutineScope(Dispatchers.IO).launch {
                    val response = service.createWorkoutExerciseSet(workoutSetToCreate)
                    if(response.isSuccessful){
                        refreshWorkoutExercise()
                    }
                }
            }
        }
    }

    fun resetAdapter(){
        val adapter = WorkoutSetsAdapter(this, workoutExercise.sets)
        lv_sets.adapter = adapter

        lv_sets.setOnItemClickListener { parent, view, position, id ->
            val set = adapter.getItem(position)
            txt_weight.setText(set?.weight.toString())
            txt_reps.setText(set?.reps.toString())
        }
    }

    fun refreshWorkoutExercise(){
        val service = ApiConnector.getInstance().create(WorkoutRepo::class.java)

        service.getWorkoutExercise(workoutExercise._id).enqueue(object:
            Callback<WorkoutExercise> {
            override fun onResponse(
                call: Call<WorkoutExercise>,
                response: Response<WorkoutExercise>
            ) {

                if (workoutExercise != null && response.body() != null){
                    workoutExercise = response.body() as WorkoutExercise
                }
                resetAdapter()
            }

            override fun onFailure(call: Call<WorkoutExercise>, t: Throwable) {
            }

        })
    }

    private fun onAddClick(editText:EditText){
        val countString = editText.text.toString()
        if(countString.length.equals(0)){
            editText.setText("0")
        }else{
            var count = countString.toInt()
            count++
            editText.setText(count.toString())
        }
    }

    private fun onMinusClick(editText:EditText){
        val countString = editText.text.toString()
        if(countString.length.equals(0)){
            editText.setText("0")
            return;
        }
        var count = countString.toInt()

        if(count<1){
            editText.setText("0")
            return;
        }

        if(count>0){
            count--
        }
        editText.setText(count.toString())
    }


    class WorkoutSetsAdapter(context: Context, private val set:List<WorkoutSet>) : ArrayAdapter<WorkoutSet>(context,0,set){

        override fun getView(position: Int, v: View?, parent: ViewGroup): View {
            var v1: View? = v
            if (v1 == null) {
                val mInflater = LayoutInflater.from(context)
                v1 = mInflater.inflate(R.layout.adapter_workout_sets_layout2, null)

            }
            val resView: View = v1!!
            val w = set[position]
            val numberPosition = position + 1;
            val repsView = resView.findViewById<TextView>(R.id.tv_reps)
            val weightView = resView.findViewById<TextView>(R.id.tv_weight)
            val numberView = resView.findViewById<TextView>(R.id.tv_number)
            numberView.text = numberPosition.toString()
            weightView.text = w.weight.toString()
            repsView.text = w.reps.toString()
            return resView
        }
    }

}