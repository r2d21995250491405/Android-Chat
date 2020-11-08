package com.example.firebasechat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.UserViewHolder> {
    private ArrayList<User> userArrayList;
    private onUserClickListener listener;


    public interface onUserClickListener {
        void onUserClick(int position);
    }

    public void setOnClickListener(onUserClickListener listener) {
        this.listener = listener;
    }


    public AdapterUser(ArrayList<User> userArrayList) {
        this.userArrayList = userArrayList;

    }

    @NonNull
    @Override
    public AdapterUser.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterUser.UserViewHolder holder, int position) {
        User user = userArrayList.get(position);
        holder.imageView.setImageResource(user.getImageResource());
        holder.textView.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public UserViewHolder(@NonNull View itemView, final onUserClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView3);
            textView = itemView.findViewById(R.id.textView3);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onUserClick(position);
                        }
                    }
                }
            });
        }
    }
}
