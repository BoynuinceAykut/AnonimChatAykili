package com.aykutboynuince.aykilisoft.anonimchat;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private FirebaseUser firebaseUser;
    private ArrayList<Message> chatList = new ArrayList<>();
    private CustomAdapter customAdapter;
    private String subject;
    private ListView listView;
    private FloatingActionButton floatingActionButton;
    private EditText inputChat;

    private Users userDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        listView = (ListView)findViewById(R.id.chatListView);
        inputChat = (EditText)findViewById(R.id.inputChat);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);

        db = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        customAdapter = new CustomAdapter(getApplicationContext(),chatList,firebaseUser);
        listView.setAdapter(customAdapter);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            subject = bundle.getString("Subject");

            dbRef = db.getReference("ChatSubjects/"+subject+"/mesaj");
            setTitle(subject);
        }

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Message message = ds.getValue(Message.class);
                    chatList.add(message);
                }

                customAdapter.notifyDataSetChanged();
                listView.setSelection(customAdapter.getCount() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputChat.length() >= 1){
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users/"+firebaseUser.getUid());
                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            long msTime = System.currentTimeMillis();
                            Date curDateTime = new Date(msTime);
                            SimpleDateFormat formatter = new SimpleDateFormat("dd'/'MM'/'y hh:mm");
                            String dateTime = formatter.format(curDateTime);

                            Map returnUser = (Map) dataSnapshot.getValue();

                            userDto = new Users();
                            userDto.userName = returnUser.get("userName").toString();
                            userDto.userId = returnUser.get("userId").toString();
                            userDto.displayName = returnUser.get("displayName").toString();
                            userDto.recordDate = returnUser.get("recordDate").toString();

                            Message message = new Message(inputChat.getText().toString(),firebaseUser.getEmail(),dateTime,userDto.displayName);
                            dbRef.push().setValue(message);
                            inputChat.setText("");

                            listView.setSelection(customAdapter.getCount() - 1);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                }
                else
                {
                    Toast.makeText(getApplicationContext(), R.string.PleaseFillInput, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
