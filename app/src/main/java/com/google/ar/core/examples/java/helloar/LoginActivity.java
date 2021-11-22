package com.google.ar.core.examples.java.helloar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
    public TextView username,password;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        mAuth = FirebaseAuth.getInstance();
        login=(Button)findViewById(R.id.buttonlogin);
        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){
                username= (TextView) findViewById(R.id.editTextusername);
                password=(TextView) findViewById(R.id.editTextpassword);
                String email=username.getText().toString().trim();
                Log.d("email",email);
                String pass_word=password.getText().toString().trim();
                Log.d("pass_word",pass_word);
                signIn(email,pass_word);

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
    public void updateUI(FirebaseUser user){
        if(user==null){
            Toast.makeText(LoginActivity.this, "This is'nt in!",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            //String userName = user.getDisplayName();
            //這等HARVEY再修
            String userid=user.getUid();
            Toast.makeText(LoginActivity.this, "Hello"+userid,
                    Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(this,MenuActivity.class);
            intent.putExtra("bruh",user);
            startActivity(intent);
        }
    }
}