package com.example.gym_mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import com.example.gym_mobile.Entities.Workout.WorkoutExercise
import kotlinx.android.synthetic.main.activity_update_set.*

class UpdateSetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_set)

        backToWorkout.setOnClickListener{
            this.finish()
        }

        val workoutExercise = intent.getSerializableExtra("workoutExercise") as WorkoutExercise
        tv_exerciseName.text = workoutExercise.exercise.name

        btnPlusWeight.setOnClickListener{
            onAddClick(txt_weight)
        }

        btnPlusReps.setOnClickListener{
            onAddClick(txt_reps)
        }
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
}