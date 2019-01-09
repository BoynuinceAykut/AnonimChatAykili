package com.aykutboynuince.aykilisoft.anonimchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUserName;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonRegister;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private TextView buttonPasswordForgot;
    private String userName;
    private String password;

    private Users userDto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUserName = (EditText)findViewById(R.id.editTextUserName);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        buttonLogin = (Button)findViewById(R.id.buttonLogin);
        buttonRegister = (Button)findViewById(R.id.buttonRegister);
        buttonPasswordForgot = (TextView)findViewById(R.id.textViewPasswordForgot);


        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        if(firebaseUser != null)
        {
            Intent intent = new Intent(MainActivity.this,HomeActivity.class);

            startActivity(intent);
            finish();
        }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage(getString(R.string.LoadingLogin));
                progressDialog.show();


                userName = editTextUserName.getText().toString();
                password = editTextPassword.getText().toString();

                if (userName.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.PleaseFillInput),Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signInWithEmailAndPassword(userName,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                String uid = mAuth.getUid();
                                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users/"+uid);

                                dbRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Map gelenDeger = (Map)dataSnapshot.getValue();

                                        userDto = new Users();

                                        userDto.displayName = gelenDeger.get("displayName").toString();
                                        userDto.userId = gelenDeger.get("userId").toString();
                                        userDto.recordDate = gelenDeger.get("recordDate").toString();
                                        userDto.userName = gelenDeger.get("userName").toString();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                progressDialog.hide();
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                progressDialog.hide();
                                Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
               // finish();
            }
        });

        buttonPasswordForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}
