package com.tecwec.connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Signup extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore database;


    EditText emailBox;
    EditText nameBox;
    EditText passwordBox;
    Button loginButton;
    Button signupButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameBox=findViewById(R.id.nameBoxID);
        emailBox=findViewById(R.id.emailBoxID);
        passwordBox=findViewById(R.id.passwordBoxID);
        loginButton=findViewById(R.id.logInButtonID);
        signupButton=findViewById(R.id.createAccountButtonID);

        auth=FirebaseAuth.getInstance();
        database=FirebaseFirestore.getInstance();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=nameBox.getText().toString();
                String email=emailBox.getText().toString();
                String password=passwordBox.getText().toString();

                User user=new User();
                user.setEmail(email);
                user.setName(name);
                user.setPassword(password);

                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            database.collection("Users")
                                    .document().set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    startActivity(new Intent(Signup.this,LogIn.class));
                                }
                            });
                            Toast.makeText(Signup.this, "Account Created", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Signup.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }
}