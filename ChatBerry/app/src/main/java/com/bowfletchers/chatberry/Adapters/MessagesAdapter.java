package com.bowfletchers.chatberry.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bowfletchers.chatberry.ClassLibrary.Message;
import com.bowfletchers.chatberry.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

//Adapter for the recycler view
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    private ArrayList<Message> MessageData;
    private Context Context;
    private ImageView ProfileImage;

    //Setting the fields with data
    public MessagesAdapter (Context context, ArrayList<Message> messageData) {
        this.MessageData = messageData;
        this.Context = context;
    }

    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int id) {
        return new ViewHolder(LayoutInflater.from(Context).inflate(R.layout.message_item, parent, false));
    }

    @Override
    //get the game object for each card with index
    public void onBindViewHolder(MessagesAdapter.ViewHolder holder, int pos){
        Message currentMessage = MessageData.get(pos);

        holder.bindTo(currentMessage);
    }

    public int getItemCount() {
        return MessageData.size();
    }

    //View holder class, implements onClickListener
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;
        private TextView message;
        private Message currentMessage;


        //set views for the card
        ViewHolder(View view) {
            super(view);
            name = itemView.findViewById(R.id.name);
            message = itemView.findViewById(R.id.message);
            ProfileImage = itemView.findViewById(R.id.profilePicture);
            //set the onClickListener so app knows when card is clicked
            view.setOnClickListener(this);
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
        void bindTo (Message message) {
            this.currentMessage = message;
            this.name.setText(message.getMember());
            this.message.setText(message.getMessage());
            Glide.with(Context).load(message.getImageURL()).placeholder(R.drawable.ic_person).into(ProfileImage);
        }
    }
}
