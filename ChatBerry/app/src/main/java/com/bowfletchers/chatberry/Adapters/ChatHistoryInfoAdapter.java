package com.bowfletchers.chatberry.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bowfletchers.chatberry.Activities.MessageViewer;
import com.bowfletchers.chatberry.ClassLibrary.Member;
import com.bowfletchers.chatberry.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ChatHistoryInfoAdapter extends RecyclerView.Adapter<ChatHistoryInfoAdapter.ViewHolder> {

    private Context mcontext;
    private List<Member> memberList = new ArrayList<>();
    private List<String> chatIdList = new ArrayList<>();
    public ChatHistoryInfoAdapter(Context context, List<Member> members, List<String> chatIdList)
    {
        this.mcontext = context;
        this.memberList = members;
        this.chatIdList = chatIdList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_history_layout,parent , false );
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.friendName.setText(memberList.get(position).name);
        //holder.lastMessage.setText("last message");
        Glide.with(mcontext).load(memberList.get(position).profilePicture).into(holder.friendImage);
        holder.recyclerViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, MessageViewer.class);
                intent.putExtra("chatMember", memberList.get(position));
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView friendName;
        TextView lastMessage;
        ImageView friendImage;
        ConstraintLayout recyclerViewLayout;

        public ViewHolder(View itemView)
        {
            super(itemView);
            friendName = itemView.findViewById(R.id.friend_name);
            lastMessage = itemView.findViewById(R.id.last_message);
            friendImage = itemView.findViewById(R.id.available_user_image);
            recyclerViewLayout = itemView.findViewById(R.id.recycler_view_layout);
        }
    }
}
