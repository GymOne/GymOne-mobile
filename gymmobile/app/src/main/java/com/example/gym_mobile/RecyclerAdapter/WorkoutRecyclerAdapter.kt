package com.example.gym_mobile.RecyclerAdapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gym_mobile.Entities.Workout.WorkoutExercise
import com.example.gym_mobile.R
import kotlinx.android.synthetic.main.adapter_workout_sets_layout.view.*
import kotlinx.android.synthetic.main.layout_workout_exercise_item.view.*

class WorkoutRecyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<WorkoutExercise> = ArrayList()
    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position:Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WorkoutExerciseViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_workout_exercise_item,parent,false),parent.context,mListener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false);
        when(holder){

            is WorkoutExerciseViewHolder -> {

                holder.bind(items.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(workoutExerciseList:List<WorkoutExercise>){
        items = workoutExerciseList

    }

    class WorkoutExerciseViewHolder constructor(
        itemView: View, context: Context, listener:onItemClickListener
    ) : RecyclerView.ViewHolder(itemView){
        val exerciseName = itemView.exercise_name
        val setsLayout = itemView.sets_layout
        val context = context

        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }

        fun bind(workoutExercise:WorkoutExercise){
            exerciseName.setText(workoutExercise.exercise.name)
            val layoutInflater:LayoutInflater = LayoutInflater.from(context)
            workoutExercise.sets.forEach(){
                val view = layoutInflater.inflate(R.layout.adapter_workout_sets_layout, null)
                view.tv_weight.setText(it.weight.toString());
                view.tv_reps.setText(it.reps.toString());
                setsLayout.addView(view)
            }
        }

    }
}