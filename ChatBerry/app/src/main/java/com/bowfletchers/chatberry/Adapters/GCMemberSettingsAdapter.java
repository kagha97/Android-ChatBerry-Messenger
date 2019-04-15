package com.bowfletchers.chatberry.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bowfletchers.chatberry.ClassLibrary.Member;
import com.bowfletchers.chatberry.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class GCMemberSettingsAdapter extends RecyclerView.Adapter<GCMemberSettingsAdapter.ViewHolder>  {


    private List<Member> MemberData;
    private android.content.Context Context;
    private ImageView ProfileImage;
    private CheckBox check;
    private String ownerID;
    private String userID;

    //Setting the fields with data
    public GCMemberSettingsAdapter (Context context, List<Member> memberData, String ownerID, String userID) {
        this.MemberData = memberData;
        this.Context = context;
        this.ownerID = ownerID;
        this.userID = userID;
    }


    @Override
    public GCMemberSettingsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int id) {
        return new GCMemberSettingsAdapter.ViewHolder(LayoutInflater.from(Context).inflate(R.layout.gc_edit_member_item, parent, false));
    }

    @Override
    //get the game object for each card with index
    public void onBindViewHolder(GCMemberSettingsAdapter.ViewHolder holder, int pos){
        Member currentMember = MemberData.get(pos);

        holder.bindTo(currentMember);


    }

    public int getItemCount() {
        return MemberData.size();
    }

    //View holder class, implements onClickListener
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;
        private TextView status;
        private Button kick;
        private Member currentMember;


        //set views for the card
        ViewHolder(View view) {
            super(view);
            name = itemView.findViewById(R.id.name);
            status = itemView.findViewById(R.id.status);
            kick = itemView.findViewById(R.id.kick);
            ProfileImage = itemView.findViewById(R.id.profilePicture);
            //set the onClickListener so app knows when card is clicked
            view.setOnClickListener(this);



            kick.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayToast(currentMember.getId() + " | "  + ownerID);
                    if (currentMember.getId().equals(ownerID)) {
                       // MemberData.remove(currentMember);
                        //notifyDataSetChanged();
                        displayToast("Can't kick yourself.");
                    }
                    else if (userID.equals(ownerID)){
                        MemberData.remove(currentMember);
                        notifyDataSetChanged();
                        displayToast("User kicked.");
                    }

                    else {
                        displayToast("Only group Owner and Admins can kick.");
                    }

                }
            });

            }

        @Override
        //when card is clicked
        public void onClick(View view) {
            // displayToast(url);
        }

        public void displayToast(String message){
            Toast.makeText(Context, message,Toast.LENGTH_SHORT).show();
        }

        //binding the data from the game object to card
        void bindTo (Member member) {
            this.currentMember = member;
            this.name.setText(member.getName());
            this.status.setText(member.getStatusString());
            Glide.with(Context).load(member.getProfilePicture()).placeholder(R.drawable.ic_person).into(ProfileImage);
        }
    }
}
