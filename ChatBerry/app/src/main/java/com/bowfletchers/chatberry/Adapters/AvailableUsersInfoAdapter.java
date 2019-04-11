package com.bowfletchers.chatberry.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bowfletchers.chatberry.Activities.MessageViewer;
import com.bowfletchers.chatberry.ClassLibrary.Member;
import com.bowfletchers.chatberry.Helper.SendInvitations;
import com.bowfletchers.chatberry.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AvailableUsersInfoAdapter extends RecyclerView.Adapter<AvailableUsersInfoAdapter.ViewHolder> {
    private ArrayList<Member> mavailableUsers = new ArrayList<>();
    private Context mcontext;

    public AvailableUsersInfoAdapter(ArrayList<Member> availableUsers, Context context)
    {
        this.mavailableUsers = availableUsers;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.available_users_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.userName.setText(mavailableUsers.get(position).name);
        viewHolder.chatUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //     Toast.makeText(mcontext , mavailableUsers.get(position).id , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mcontext, MessageViewer.class);
                intent.putExtra("chatMember" , mavailableUsers.get(position));
                mcontext.startActivity(intent);
            }
        });
        Glide.with(mcontext).load(mavailableUsers.get(position).profilePicture).into(viewHolder.userImage);
        if(mavailableUsers.get(position).me) {
            viewHolder.addUser.setVisibility(View.GONE);
        }
        else {
            viewHolder.addUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mcontext, "Clicked" ,Toast.LENGTH_LONG).show();
                    SendInvitations sendInvitations = new SendInvitations();
                    sendInvitations.checkIfAlreadySent(mavailableUsers.get(position).id);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mavailableUsers.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView userName;
        Button chatUser;
        Button addUser;
        ImageView userImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.available_user_name);
            chatUser = itemView.findViewById(R.id.chat_button);
            addUser = itemView.findViewById(R.id.add_friend_button);
            userImage = itemView.findViewById(R.id.available_user_image);
        }
    }
}
