package com.aykutboynuince.aykilisoft.anonimchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.google.firebase.auth.FirebaseUser;

public class CustomAdapter extends ArrayAdapter<Message> {

    private FirebaseUser firebaseUser;

    public CustomAdapter(Context context, ArrayList<Message> chatList,FirebaseUser firebaseUser) {
        super(context, 0, chatList);
        this.firebaseUser = firebaseUser;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Message message = getItem(position);

        if(firebaseUser.getEmail().equalsIgnoreCase(message.getGonderici())){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.right_item_layout,parent,false);

            TextView txtUser = (TextView)convertView.findViewById(R.id.txtUserRight);
            TextView txtMessage = (TextView)convertView.findViewById(R.id.txtMessageRight);
            TextView txtTime = (TextView)convertView.findViewById(R.id.txtTimeRight);

            txtUser.setText(message.getDisplayName());
            txtMessage.setText(message.getMesajText());
            txtTime.setText(message.getZaman());
        }
        else
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.left_item_layout,parent,false);

            TextView txtUser = (TextView)convertView.findViewById(R.id.txtUserLeft);
            TextView txtMessage = (TextView)convertView.findViewById(R.id.txtMessageLeft);
            TextView txtTime = (TextView)convertView.findViewById(R.id.txtTimeLeft);

            txtUser.setText(message.getDisplayName());
            txtMessage.setText(message.getMesajText());
            txtTime.setText(message.getZaman());
        }

        return convertView;
    }
}
