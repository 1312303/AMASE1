package com.example.jayr.amase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class loginActivity extends AppCompatActivity {
    private EditText txtuname,txtpwrd;
    private FirebaseAuth auth;
    private Button login;
    private FirebaseDatabase db;
    private ProgressDialog progDialog;
    public void getToken(){
        auth = FirebaseAuth.getInstance();
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("Hello","Token"+token );
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this. getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        progDialog = new ProgressDialog(loginActivity.this);
        progDialog.setMessage("Loading...");
        progDialog.setProgressStyle(progDialog.STYLE_SPINNER);
        txtuname = (EditText)findViewById(R.id.txtUsername);
        txtpwrd = (EditText)findViewById(R.id.txtPassword);
        login = (Button)findViewById(R.id.btnLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtuname.getText().toString();
                String pwd = txtpwrd.getText().toString();
                if (email.isEmpty() || pwd.isEmpty()){
                    Toast.makeText(loginActivity.this,"Please fill the following", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(email,pwd)
                            .addOnCompleteListener(loginActivity.this, new OnCompleteListener<AuthResult>(){
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progDialog.show();
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information

                                        FirebaseUser user = auth.getCurrentUser();
                                        String name = user.getEmail().toString();
                                        Intent mainIntent = new Intent(loginActivity.this,mainActivity.class);
                                        startActivity(mainIntent);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(loginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_LONG).show();
                                        txtpwrd.setText("");
                                        txtuname.setText("");
                                    }

                                    // ...
                                }
                            });
                }

            }
        });

    }

}
