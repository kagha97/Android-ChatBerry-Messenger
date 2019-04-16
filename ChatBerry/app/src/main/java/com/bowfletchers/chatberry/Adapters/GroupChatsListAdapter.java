package com.bowfletchers.chatberry.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bowfletchers.chatberry.Activities.GroupMessageViewer;
import com.bowfletchers.chatberry.R;

import java.util.ArrayList;
import java.util.List;

public class GroupChatsListAdapter extends RecyclerView.Adapter<GroupChatsListAdapter.ViewHolder> {
    private Context mcontext;
    private List<String> mGroupNames = new ArrayList<>();
    private List<String> mGroupId = new ArrayList<>();
    public GroupChatsListAdapter(Context context, List<String> groupNames, List<String> groupId){
        this.mcontext = context;
        this.mGroupNames = groupNames;
        this.mGroupId = groupId;
    }
    @NonNull
    @Override
    public GroupChatsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_chats_list_layout, parent, false);
        GroupChatsListAdapter.ViewHolder holder = new GroupChatsListAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final GroupChatsListAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.groupName.setText(mGroupNames.get(position));
        viewHolder.groupNameIcon.setText(mGroupNames.get(position).substring(0,1));
        viewHolder.groupChatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent groupChatIntent = new Intent(mcontext, GroupMessageViewer.class);
                groupChatIntent.putExtra("id", mGroupId.get(position));
                groupChatIntent.putExtra("name", mGroupNames.get(position));
                mcontext.startActivity(groupChatIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGroupNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView groupName;
        ConstraintLayout groupChatView;
        TextView groupNameIcon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.group_chat_name);
            groupChatView = itemView.findViewById(R.id.group_chat_layout);
            groupNameIcon = itemView.findViewById(R.id.group_chat_icon_text);
        }
    }
}
