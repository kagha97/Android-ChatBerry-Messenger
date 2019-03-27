package com.bowfletchers.chatberry.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bowfletchers.chatberry.R;

import java.util.ArrayList;

public class AvailableUsersInfoAdapter extends RecyclerView.Adapter<AvailableUsersInfoAdapter.ViewHolder> {
    private ArrayList<String> mavailableUsers = new ArrayList<>();

    public AvailableUsersInfoAdapter(ArrayList<String> availableUsers)
    {
        this.mavailableUsers = availableUsers;
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
        viewHolder.userName.setText(mavailableUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return mavailableUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView userName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.available_user_name);
        }
    }
}
