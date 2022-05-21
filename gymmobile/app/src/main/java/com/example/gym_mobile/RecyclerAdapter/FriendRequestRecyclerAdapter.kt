package com.example.gym_mobile.RecyclerAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gym_mobile.Entities.FriendRequest
import com.example.gym_mobile.Friend.FriendDto
import com.example.gym_mobile.Model.User
import com.example.gym_mobile.R
import kotlinx.android.synthetic.main.adapter_friend_request_layout.view.*

class FriendRequestRecyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var friendRequests: List<FriendRequest> = ArrayList()
    private lateinit var acceptListener : onAcceptListener
    private lateinit var declineListener : onDeclineListener

    interface onAcceptListener {
        fun onAcceptClick(position: Int)
    }

    interface onDeclineListener {
        fun onDeclineClick(position: Int)
    }

    fun setOnAcceptListener(listener : onAcceptListener){
        acceptListener = listener
    }

    fun setOnDeclineListener(listener : onDeclineListener){
        declineListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FriendRequestViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_friend_request_layout,parent,false),parent.context, acceptListener, declineListener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false);
        when(holder){
            is FriendRequestViewHolder -> {
                holder.bind(friendRequests.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return friendRequests.size
    }

    fun assignListFriends(listRequests: List<FriendRequest>){
        friendRequests = listRequests
    }

    class FriendRequestViewHolder constructor(
        itemView: View, val context: Context, val acceptListener: onAcceptListener, val declineListener: onDeclineListener
    ) : RecyclerView.ViewHolder(itemView){
        val friendName = itemView.tv_friendRequest
        val acceptBtn = itemView.btn_acceptReq
        val declineBtn = itemView.btn_removeReq

        init {
            acceptBtn.setOnClickListener{
                acceptListener.onAcceptClick(adapterPosition)
            }
            declineBtn.setOnClickListener{
                declineListener.onDeclineClick(adapterPosition)
            }
        }

        fun bind(friendRequest: FriendRequest){
            if (friendRequest.senderId.equals(User.getUserEmail())){
                friendName.setText(friendRequest.receiverId)
            }
            friendName.setText(friendRequest.senderId)
            val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        }
    }
}