package com.bowfletchers.chatberry;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChatHistoryInfoAdapter extends RecyclerView.Adapter<ChatHistoryInfoAdapter.ViewHolder> {

    private Context mcontext;
    private ArrayList<String> mfriendsName = new ArrayList<>();
    public ChatHistoryInfoAdapter(Context context, ArrayList<String> friendsName)
    {
        this.mcontext = context;
        this.mfriendsName = friendsName;
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
        holder.friendName.setText(mfriendsName.get(position));
        holder.lastMessage.setText(mfriendsName.get(position));
    }

    @Override
    public int getItemCount() {
        return mfriendsName.size();
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
            friendImage = itemView.findViewById(R.id.friend_image);
            recyclerViewLayout = itemView.findViewById(R.id.recycler_view_layout);
        }
    }
}
