package com.bowfletchers.chatberry.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bowfletchers.chatberry.ClassLibrary.FirebaseInstances;
import com.bowfletchers.chatberry.ClassLibrary.Member;
import com.bowfletchers.chatberry.ClassLibrary.MemberFriendList;
import com.bowfletchers.chatberry.R;
import com.bowfletchers.chatberry.ViewModel.AvailableUsersModel.AddFriends;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {
    private Context mcontenxt;
    private List<Member> memberList;
    private List<String> invitationList;
    private DatabaseReference users = FirebaseDatabase.getInstance().getReference("users");
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference invitations = FirebaseDatabase.getInstance().getReference("invitations");

    public FriendRequestAdapter(Context context, List<Member> memberList, List<String> invitationList){
        this.mcontenxt = context;
        this.memberList = memberList;
        this.invitationList = invitationList;
    }
    @NonNull
    @Override
    public FriendRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_request_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestAdapter.ViewHolder viewHolder, final int position) {
       viewHolder.name.setText(memberList.get(position).getName());
       Glide.with(mcontenxt).load(memberList.get(position).profilePicture).into(viewHolder.imageView);
       viewHolder.confirmFriend.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               AddFriends addFriends = new AddFriends();
               addFriends.addingFriends(auth.getUid(), memberList.get(position).id);
               addFriends.removeInvitation(invitationList.get(position));
           }
       });
       viewHolder.cancelFriend.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               AddFriends addFriends = new AddFriends();
               addFriends.removeInvitation(invitationList.get(position));
           }
       });
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private Button confirmFriend;
        private Button cancelFriend;
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.requestee_user_name);
            confirmFriend = itemView.findViewById(R.id.add_friend_button);
            cancelFriend = itemView.findViewById(R.id.cancel_friend_button);
            imageView = itemView.findViewById(R.id.requestee_user_image);

        }
    }
}
