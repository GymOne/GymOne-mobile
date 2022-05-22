package com.example.gym_mobile

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gym_mobile.Dto.LoginDto
import com.example.gym_mobile.Dto.SubmitFriendRequestDto
import com.example.gym_mobile.Entities.FriendRequest
import com.example.gym_mobile.Entities.Workout.WorkoutSession
import com.example.gym_mobile.Model.User
import com.example.gym_mobile.RecyclerAdapter.FriendRequestRecyclerAdapter
import com.example.gym_mobile.RecyclerAdapter.TopItemSpaceDecoration
import com.example.gym_mobile.RecyclerAdapter.WorkoutRecyclerAdapter
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
import kotlinx.android.synthetic.main.fragment_tracking.*
import kotlinx.android.synthetic.main.send_friend_request_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

        friendRepo.getFriendsReqWNames(User.getUserEmail()).enqueue(object:
            Callback<List<FriendRequest>> {
            override fun onResponse(
                call: Call<List<FriendRequest>>,
                response: Response<List<FriendRequest>>

            ) {
                val listReq = response.body() as List<FriendRequest>

                println(listReq)

                friendRequestAdapter.assignListFriends(listReq)
                friendRequestAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<FriendRequest>>, t: Throwable) {
                friendRequestAdapter.assignListFriends(emptyList())
                friendRequestAdapter.notifyDataSetChanged()
            }
        })

    }

    override fun onStart() {
        loadFriendRequests()
        super.onStart()
    }

    private fun initRecyclerView(){
        friend_req_recycler_view.recycledViewPool.clear()
        friend_req_recycler_view.layoutManager = LinearLayoutManager(context as Context)
        if(friend_req_recycler_view.itemDecorationCount == 0) {
            val topSpaceDecoration = TopItemSpaceDecoration(20)
            friend_req_recycler_view.addItemDecoration(topSpaceDecoration)
        }
        friendRequestAdapter = FriendRequestRecyclerAdapter()
        friend_req_recycler_view.swapAdapter(friendRequestAdapter,true)
        (friend_req_recycler_view.adapter as FriendRequestRecyclerAdapter).setOnAcceptListener(
            object : FriendRequestRecyclerAdapter.onAcceptListener{
                override fun onAcceptClick(position: Int) {
                    val friendRequest = friendRequestAdapter.friendRequests.get(position)
                    val repo = ApiConnector.getInstance().create(FriendsRepo::class.java)
                    CoroutineScope(Dispatchers.IO).launch {
                        val actionDto : SubmitFriendRequestDto? = User.getUserEmail()
                            ?.let { SubmitFriendRequestDto(friendRequest.email, it, true) }
                        val response = repo.actionUponRequest(actionDto)
                        if(response.isSuccessful){
                            loadFriendRequests()
                            //Toast.makeText(binding.root.context, "You are now friends with "+friendRequest.name , Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        (friend_req_recycler_view.adapter as FriendRequestRecyclerAdapter).setOnDeclineListener(
            object : FriendRequestRecyclerAdapter.onDeclineListener{
                override fun onDeclineClick(position: Int) {
                    val friendRequest = friendRequestAdapter.friendRequests.get(position)
                    val repo = ApiConnector.getInstance().create(FriendsRepo::class.java)
                    CoroutineScope(Dispatchers.IO).launch {
                        val actionDto : SubmitFriendRequestDto? = User.getUserEmail()
                            ?.let { SubmitFriendRequestDto(friendRequest.email, it, false) }
                        val response = repo.actionUponRequest(actionDto)
                        if(response.isSuccessful){
                            loadFriendRequests()
                            //Toast.makeText(binding.root.context, "Rejected request from "+friendRequest.name , Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGoToFriends.setOnClickListener {
            findNavController().navigate(R.id.action_friendRequestFragment_to_friendsFragment)
        }
        initRecyclerView()
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