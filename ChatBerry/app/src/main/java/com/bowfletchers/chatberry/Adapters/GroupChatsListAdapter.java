package com.bowfletchers.chatberry.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bowfletchers.chatberry.R;

import java.util.ArrayList;
import java.util.List;

public class GroupChatsListAdapter extends RecyclerView.Adapter<GroupChatsListAdapter.ViewHolder> {
    private Context mcontext;
    private List<String> mGroupNames = new ArrayList<>();
    public GroupChatsListAdapter(Context context, List<String> groupNames){
        this.mcontext = context;
        this.mGroupNames = groupNames;
    }
    @NonNull
    @Override
    public GroupChatsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_chats_list_layout, parent, false);
        GroupChatsListAdapter.ViewHolder holder = new GroupChatsListAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupChatsListAdapter.ViewHolder viewHolder, int position) {
        viewHolder.groupName.setText(mGroupNames.get(position));
    }

    @Override
    public int getItemCount() {
        return mGroupNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView groupName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.group_chat_name);
        }
    }
}
