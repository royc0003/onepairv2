package com.iff.onepairv2;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageViewHolder extends  RecyclerView.ViewHolder{

    public TextView messageText;


    public MessageViewHolder(@NonNull View view) {
        super(view);
        messageText = (TextView) view.findViewById(R.id.message_text_layout);
    }
}
