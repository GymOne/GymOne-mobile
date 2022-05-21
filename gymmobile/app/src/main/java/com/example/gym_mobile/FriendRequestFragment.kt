package com.example.gym_mobile

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.example.gym_mobile.Dto.LoginDto
import com.example.gym_mobile.Dto.SubmitFriendRequestDto
import com.example.gym_mobile.Entities.Workout.WorkoutSession
import com.example.gym_mobile.Model.User
import com.example.gym_mobile.RecyclerAdapter.FriendRequestRecyclerAdapter
import com.example.gym_mobile.Repository.ApiConnector
import com.example.gym_mobile.Repository.AuthRepo
import com.example.gym_mobile.Repository.FriendsRepo
import com.example.gym_mobile.Repository.WorkoutRepo
import com.example.gym_mobile.databinding.FragmentFriendRequestBinding
import com.example.gym_mobile.databinding.FragmentFriendsBinding
import com.example.gym_mobile.databinding.FragmentTrackingBinding
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.create_new_exercise_layout.*
import kotlinx.android.synthetic.main.fragment_exercises.*
import kotlinx.android.synthetic.main.fragment_friend_request.*
import kotlinx.android.synthetic.main.send_friend_request_layout.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendRequestFragment : Fragment() {

    private lateinit var binding: FragmentFriendRequestBinding
    lateinit var friendRequestAdapter : FriendRequestRecyclerAdapter

    lateinit var email: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFriendRequestBinding.inflate(inflater, container, false)

        var myInputField = view?.findViewById<TextInputEditText>(R.id.emailInput)
        if (myInputField != null) {
            myInputField.doOnTextChanged { text, start, before, count ->
            }
        }

        return binding.root
    }

    private fun loadFriendRequests(){
        val friendRepo = ApiConnector.getInstance().create(FriendsRepo::class.java)

        friendRepo.getFriendsByEmail(User.getUserEmail()).enqueue(object:
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGoToFriends.setOnClickListener {
            findNavController().navigate(R.id.action_friendRequestFragment_to_friendsFragment)
        }
        exerciseNameInput
        btnCreateFriendRequest.setOnClickListener(){
            val newFragment = onCreateDialog(view,savedInstanceState)
            newFragment.show()
        }

    }

    private fun onCreateDialog(view: View, savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;

            val DialogView = inflater.inflate(R.layout.send_friend_request_layout, null)

            email = DialogView.findViewById(R.id.emailInput)
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(DialogView)

            builder.setPositiveButton("Send") { dialog, which ->

                var submitFriendRequestDto: SubmitFriendRequestDto = User.getUserEmail()
                    ?.let { it1 -> SubmitFriendRequestDto(it1, email.text.toString(), false) }!!

                    val requestAPi = ApiConnector.getInstance().create(FriendsRepo::class.java)

                    GlobalScope.launch {
                        val result = requestAPi.submitFriendRequest(submitFriendRequestDto)
                        if(result.isSuccessful){
                        }
                    }

                Toast.makeText(binding.root.context, email.text.toString() + " invited" , Toast.LENGTH_SHORT).show()

            }

            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
                Toast.makeText(binding.root.context, "Canceled", Toast.LENGTH_SHORT).show()
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}