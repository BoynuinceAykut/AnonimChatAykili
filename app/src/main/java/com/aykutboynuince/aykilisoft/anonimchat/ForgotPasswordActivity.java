package com.aykutboynuince.aykilisoft.anonimchat;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private String userName;
    private EditText editTextUserName;
    private Button buttonForgotPasswordSend;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editTextUserName = (EditText)findViewById(R.id.editTextUserName);
        buttonForgotPasswordSend = (Button)findViewById(R.id.buttonResetPassword);
        mAuth = FirebaseAuth.getInstance();

        buttonForgotPasswordSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(ForgotPasswordActivity.this);
                progressDialog.setMessage(getString(R.string.LoadingMessage));
                progressDialog.show();

                userName = editTextUserName.getText().toString();

                if(!userName.isEmpty()) {
                    mAuth.sendPasswordResetEmail(userName).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.hide();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), R.string.ResetSendMail, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(),R.string.PleaseFillInput,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
