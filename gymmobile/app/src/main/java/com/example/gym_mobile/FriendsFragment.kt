package com.example.gym_mobile

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.gym_mobile.Dto.GetFriendsDto
import com.example.gym_mobile.Dto.SubmitFriendRequestDto
import com.example.gym_mobile.Entities.Exercise
import com.example.gym_mobile.Entities.Friend
import com.example.gym_mobile.Model.DataStor
import com.example.gym_mobile.Model.User
import com.example.gym_mobile.Repository.ApiConnector
import com.example.gym_mobile.Repository.FriendsRepo
import com.example.gym_mobile.databinding.FragmentFriendsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendsFragment : Fragment() {
    private lateinit var listFriends: ListView;
    private lateinit var binding: FragmentFriendsBinding
    private var _friendRepo = ApiConnector.getInstance().create(FriendsRepo::class.java)
    val email = "sender@g.com"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        println("Created friend fragment")
        super.onViewCreated(view, savedInstanceState)
        binding.btnGoToFriendRequests.setOnClickListener {
            findNavController().navigate(R.id.action_friendsFragment_to_friendRequestFragment)
        }
        listFriends = view.findViewById(R.id.listasViewas);

        _friendRepo.getFriendsByEmail(User.getUserEmail()).enqueue(object:
            Callback<List<GetFriendsDto>> {
            override fun onResponse(
                call: Call<List<GetFriendsDto>>,
                response: Response<List<GetFriendsDto>>
            ) {
                val adapter = FriendsFragment.FriendAdapter(
                    context as Context,
                    response.body() as MutableList<GetFriendsDto>
                )
                listFriends.adapter = adapter
            }

            override fun onFailure(call: Call<List<GetFriendsDto>>, t: Throwable) {
                listFriends.adapter = null
            }

        })
    }

    internal class FriendAdapter(context: Context, private val friend:MutableList<GetFriendsDto>) : ArrayAdapter<GetFriendsDto>(context,0,friend){

        private val colours = intArrayOf(
            Color.parseColor("#AAAAAA"),
            Color.parseColor("#CCCCCC")
        )

        override fun getView(position: Int, v: View?, parent: ViewGroup): View {
            var v1: View? = v
            if (v1 == null) {
                val mInflater = LayoutInflater.from(context)
                v1 = mInflater.inflate(R.layout.adapter_friends_layout, null)

            }
            val buttonAdd = v1?.findViewById<Button>(R.id.btn_unfriend)

            println("Buttonn:    "+buttonAdd);

            buttonAdd?.setOnClickListener { view ->
                // Needs some more massaging
                val friendRepo = ApiConnector.getInstance().create(FriendsRepo::class.java)
                CoroutineScope(Dispatchers.IO).launch {
                    val obj: SubmitFriendRequestDto? = User.getUserEmail()?.let { SubmitFriendRequestDto(it, friend[position].friendEmail, false ) }
                    val response = friendRepo.removeRequest(obj)
                    if (response.isSuccessful){
                        println("Successfully deleted")
                    }
                }
            }
            val resView: View = v1!!
            resView.setBackgroundColor(colours[position % colours.size])
            val e = friend[position]
            val nameView = resView.findViewById<TextView>(R.id.tv_friends)
            nameView.text = e.name
            return resView
        }
    }
}