package com.example.gym_mobile

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.gym_mobile.Entities.Exercise
import com.example.gym_mobile.Entities.Friend
import com.example.gym_mobile.Model.User
import com.example.gym_mobile.Repository.ApiConnector
import com.example.gym_mobile.Repository.FriendsRepo
import com.example.gym_mobile.databinding.FragmentFriendsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendsFragment : Fragment() {
    private lateinit var listFriends: ListView;
    private lateinit var binding: FragmentFriendsBinding
    var _friendRepo = ApiConnector.getInstance().create(FriendsRepo::class.java)
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

        _friendRepo.getFriendsByEmail(email).enqueue(object:
            Callback<List<Friend>> {
            override fun onResponse(
                call: Call<List<Friend>>,
                response: Response<List<Friend>>
            ) {
                val adapter = FriendsFragment.FriendAdapter(
                    context as Context,
                    response.body() as MutableList<Friend>
                )
                listFriends.adapter = adapter
            }

            override fun onFailure(call: Call<List<Friend>>, t: Throwable) {
                listFriends.adapter = null
            }

        })
    }

    internal class FriendAdapter(context: Context, private val friend:MutableList<Friend>) : ArrayAdapter<Friend>(context,0,friend){

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
            resView.setBackgroundColor(colours[position % colours.size])
            val e = friend[position]
            val nameView = resView.findViewById<TextView>(R.id.tv_exercise)
            nameView.text = e.receiverId
            return resView
        }
    }
}