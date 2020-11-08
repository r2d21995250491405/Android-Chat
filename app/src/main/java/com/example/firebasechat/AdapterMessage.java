package com.example.firebasechat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterMessage extends ArrayAdapter<Message> {
    private List<Message> messageList;
    private Activity activity;

    public AdapterMessage(Activity context, int resource, List<Message> messageList) {
        super(context, resource, messageList);
        this.messageList = messageList;
        this.activity = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        Message messages = getItem(position);
        int layoutResource = 0;
        int viewType = getItemViewType(position);

        if (viewType == 0) {
            layoutResource = R.layout.my_message_item;
        } else {
            layoutResource = R.layout.your_message_item;
        }

        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = layoutInflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        boolean isTex
                = messages.getIamgeUrl() == null;
        if (isTex) {
            viewHolder.photoImageView.setVisibility(View.GONE);
            viewHolder.messageTextView.setVisibility(View.VISIBLE);
            viewHolder.messageTextView.setText(messages.getText());
        } else {
            viewHolder.photoImageView.setVisibility(View.VISIBLE);
            viewHolder.messageTextView.setVisibility(View.GONE);
            Glide.with(viewHolder.photoImageView.getContext()).load(messages.getIamgeUrl()).into(viewHolder.photoImageView);
        }

//        if (convertView == null) {
//            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.message_item, parent, false);
//        }
//        ImageView photo = convertView.findViewById(R.id.imageView);
//        TextView textView = convertView.findViewById(R.id.textView);
//        TextView nameTextView = convertView.findViewById(R.id.nameText);
//
//        Message message = getItem(position);
//
//        boolean isText = message.getIamgeUrl() == null;
//
//        if (isText) {
//            textView.setVisibility(View.VISIBLE);
//            photo.setVisibility(View.GONE);
//            textView.setText(message.getText());
//        } else {
//            textView.setVisibility(View.GONE);
//            photo.setVisibility(View.VISIBLE);
//            Glide.with(photo.getContext()).load(message.getIamgeUrl()).into(photo);
//        }
//
//        nameTextView.setText(message.getName());
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        int flag;
        Message message = messageList.get(position);
        if (message.isMine()) {
            flag = 0;
        } else {
            flag = 1;
        }
        return flag;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    private class ViewHolder {

        private TextView messageTextView;
        private ImageView photoImageView;

        public ViewHolder(View view) {
            photoImageView = view.findViewById(R.id.photoImageView);
            messageTextView = view.findViewById(R.id.messageTextView);
        }
    }
}
