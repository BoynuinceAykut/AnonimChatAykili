package com.aykutboynuince.aykilisoft.anonimchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private TextView buttonLogout;
    private FirebaseAuth mAuth;

    private ListView listViewTopic;
    private ArrayList<String> subjectLists = new ArrayList<>();
    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private ArrayAdapter<String> adapter;

    public void onBackPressed()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        buttonLogout = (TextView) findViewById(R.id.buttonLogout);
        mAuth = FirebaseAuth.getInstance();

        listViewTopic = (ListView)findViewById(R.id.listviewTopic);

        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("ChatSubjects");

        adapter = new ArrayAdapter<String>(HomeActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1,subjectLists);
        listViewTopic.setAdapter(adapter);


        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
                progressDialog.setMessage((getString(R.string.LoadingMessage)));
                progressDialog.show();


                subjectLists.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    subjectLists.add(ds.getKey());
                    Log.d("LOGVALUE",ds.getKey());
                }

                adapter.notifyDataSetChanged();
                progressDialog.hide();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext()," "+ databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        listViewTopic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
                progressDialog.setMessage(getString(R.string.LoadingMessage));
                progressDialog.show();
                Intent intent = new Intent(HomeActivity.this,ChatActivity.class);
                intent.putExtra("Subject",subjectLists.get(position));
                startActivity(intent);
                progressDialog.hide();
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
                progressDialog.setMessage(getString(R.string.LogoutMessage));
                progressDialog.show();

                mAuth.signOut();
                progressDialog.hide();

                Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

}
