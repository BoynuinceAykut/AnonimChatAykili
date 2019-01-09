package com.aykutboynuince.aykilisoft.anonimchat;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextUserName;
    private EditText editTextPassword;
    private EditText editTextPasswordAgain;
    private Button buttonRegisterComplete;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;

    private String userName;
    private String password;
    private String passwordAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUserName = (EditText)findViewById(R.id.editTextUserName);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        editTextPasswordAgain = (EditText)findViewById(R.id.editTextPasswordAgain);
        buttonRegisterComplete = (Button)findViewById(R.id.buttonResetPassword);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();


        buttonRegisterComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                userName = editTextUserName.getText().toString();
                password = editTextPassword.getText().toString();
                passwordAgain = editTextPasswordAgain.getText().toString();

                if(userName.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),R.string.PleaseFillInput,Toast.LENGTH_SHORT).show();
                }
                else if(password.length() < 6)
                {
                    Toast.makeText(getApplicationContext(),R.string.PasswordMinLength,Toast.LENGTH_SHORT).show();
                }
                else if(!password.equals(passwordAgain))
                {
                    Toast.makeText(getApplicationContext(),R.string.PasswordsDoNotMatch,Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Register();
                }
            }
        });
    }

    private void Register()
    {
        mAuth.createUserWithEmailAndPassword(userName,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setMessage(getString(R.string.LoadingLogin));
                progressDialog.show();

                    if(task.isSuccessful())
                    {
                        progressDialog.hide();
                        Intent intent = new Intent(RegisterActivity.this,HomeActivity.class);
                        UserSave(mAuth.getCurrentUser().getUid(),mAuth.getCurrentUser().getEmail());
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

    private void UserSave(String userId, String userName)
    {
        dbRef = db.getReference("Users/");
        long msTime = System.currentTimeMillis();
        Date curDateTime = new Date(msTime);
        SimpleDateFormat formatter = new SimpleDateFormat("dd'/'MM'/'y hh:mm");
        String dateTime = formatter.format(curDateTime);

        Random rnd = new Random();

        String displayName = "Anonim - " +  rnd.nextInt();

        Users user = new Users(userName,dateTime,userId,displayName,"");
        dbRef.child(userId).setValue(user);
    }

}
