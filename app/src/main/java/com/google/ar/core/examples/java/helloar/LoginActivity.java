package com.google.ar.core.examples.java.helloar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends Activity {
    private static final String TAG = "create_acc";
    private FirebaseAuth mAuth;
    public Button login;
    public Button subscribe;
    public TextView username,password;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        mAuth = FirebaseAuth.getInstance();
        username= (TextView) findViewById(R.id.editTextusername);
        username.setText("root@gmail.com");
        password=(TextView) findViewById(R.id.editTextpassword);
        password.setText("root1234");
        login=(Button)findViewById(R.id.buttonlogin);
        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){
                username= (TextView) findViewById(R.id.editTextusername);
                password=(TextView) findViewById(R.id.editTextpassword);
                String email=username.getText().toString().trim();
                String pass_word=password.getText().toString().trim();
                signIn(email,pass_word);
            }
        });
        subscribe=(Button) findViewById(R.id.buttonSubscribe);
        subscribe.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                username= (TextView) findViewById(R.id.editTextusername);
                password=(TextView) findViewById(R.id.editTextpassword);
                String email=username.getText().toString().trim();
                String pass_word=password.getText().toString().trim();
                createAccount(email,pass_word);
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }
    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END sign_in_with_email]
    }
    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //inflateName(user);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]

    }
    public void updateUI(FirebaseUser user){
        if(user==null){
            Toast.makeText(LoginActivity.this, "This is'nt in!",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            String userid=user.getUid();
            Toast.makeText(LoginActivity.this, "Hello"+userid,
                    Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(this,MenuActivity.class);
            intent.putExtra("bruh",user);
            startActivity(intent);
        }
    }
}