package com.bowfletchers.chatberry.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bowfletchers.chatberry.ClassLibrary.Member;
import com.bowfletchers.chatberry.R;

import java.util.List;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendListViewHolder> {

    private List<Member> friendList;

    public FriendListAdapter(List<Member> friendList) {
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public FriendListAdapter.FriendListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new FriendListViewHolder
                (LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.friend_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FriendListAdapter.FriendListViewHolder friendListViewHolder, int i) {
        Member currentMember = friendList.get(i);
        friendListViewHolder.bindTo(currentMember);
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    class FriendListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView friendName;

        public FriendListViewHolder(@NonNull View itemView) {
            super(itemView);
            friendName = itemView.findViewById(R.id.friend_item_name);
            itemView.setOnClickListener(this);
        }

        public void bindTo(Member memberData) {
            friendName.setText(memberData.getName());
        }

        @Override
        public void onClick(View v) {
            // Handle when friend is clicked
            // to start chat
            Toast.makeText(v.getContext(), "clicked", Toast.LENGTH_SHORT).show();
        }
    }
}
