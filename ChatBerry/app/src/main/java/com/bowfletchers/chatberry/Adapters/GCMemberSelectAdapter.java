package com.bowfletchers.chatberry.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bowfletchers.chatberry.ClassLibrary.GCMember;
import com.bowfletchers.chatberry.ClassLibrary.Message;
import com.bowfletchers.chatberry.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

//Adapter for the recycler view
public class GCMemberSelectAdapter extends RecyclerView.Adapter<GCMemberSelectAdapter.ViewHolder> {

    private ArrayList<GCMember> MemberData;
    private Context Context;
    private ImageView ProfileImage;
    private CheckBox check;

    //Setting the fields with data
    public GCMemberSelectAdapter (Context context, ArrayList<GCMember> memberData) {
        this.MemberData = memberData;
        this.Context = context;
    }

    @Override
    public GCMemberSelectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int id) {
        return new ViewHolder(LayoutInflater.from(Context).inflate(R.layout.gc_member_item, parent, false));
    }

    @Override
    //get the game object for each card with index
    public void onBindViewHolder(GCMemberSelectAdapter.ViewHolder holder, int pos){
        GCMember currentMember = MemberData.get(pos);

        holder.bindTo(currentMember);
    }

    public int getItemCount() {
        return MemberData.size();
    }

    //View holder class, implements onClickListener
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;
        private TextView status;
        private CheckBox check;
        private GCMember currentMember;


        //set views for the card
        ViewHolder(View view) {
            super(view);
            name = itemView.findViewById(R.id.name);
            status = itemView.findViewById(R.id.status);
            check = itemView.findViewById(R.id.checkBox);
            ProfileImage = itemView.findViewById(R.id.profilePicture);
            //set the onClickListener so app knows when card is clicked
            view.setOnClickListener(this);

            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    currentMember.Check(isChecked);
                    displayToast(String.valueOf(currentMember.getAdd()));
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
        void bindTo (GCMember member) {
            this.currentMember = member;
            this.name.setText(member.getName());
            this.status.setText(member.getStatus());
            Glide.with(Context).load(member.getProfilePicture()).placeholder(R.drawable.ic_person).into(ProfileImage);
        }
    }
}
