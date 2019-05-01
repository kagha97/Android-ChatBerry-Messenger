package com.bowfletchers.chatberry.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bowfletchers.chatberry.ClassLibrary.UserStory;
import com.bowfletchers.chatberry.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class FriendStoriesAdapter extends RecyclerView.Adapter<FriendStoriesAdapter.FriendStoriesViewHolder> {

    private List<UserStory> listFriendStories;

    public FriendStoriesAdapter(List<UserStory> listStories) {
        this.listFriendStories = listStories;
    }

    @NonNull
    @Override
    public FriendStoriesAdapter.FriendStoriesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new FriendStoriesViewHolder
                (LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.friend_stories_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FriendStoriesAdapter.FriendStoriesViewHolder friendStoriesViewHolder, int i) {
        UserStory currentStory = listFriendStories.get(i);
        friendStoriesViewHolder.bindTo(currentStory);
    }

    @Override
    public int getItemCount() {
        return listFriendStories.size();
    }

    public class FriendStoriesViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewStoryPhoto;
        private TextView textViewFriendName;
        private TextView textViewStatusMessage;

        public FriendStoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewStoryPhoto = itemView.findViewById(R.id.imageViewStoryPicture);
            textViewFriendName = itemView.findViewById(R.id.textViewFriendName);
            textViewStatusMessage = itemView.findViewById(R.id.textViewStatus);
        }

        public void bindTo(UserStory userStory) {
            Glide.with(itemView.getContext()).load(userStory.getPhotoStoryURL()).into(imageViewStoryPhoto);
            textViewFriendName.setText(userStory.getUserName());
            textViewStatusMessage.setText(userStory.getStatusMessage());
        }
    }
}
