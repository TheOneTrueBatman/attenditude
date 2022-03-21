package com.example.attendance;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.*;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;

public class TeacherLogin extends AppCompatActivity {
    EditText id, pwd;
    TextView attemptspwd;
    Button login;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    int i = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);
        id = (EditText) findViewById(R.id.TeacherID);
        pwd = (EditText) findViewById(R.id.TeacherPassword);
        attemptspwd = findViewById(R.id.attemptspwd);
        mAuth = FirebaseAuth.getInstance();
        login =(Button)findViewById(R.id.teacher);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login.setText("Loading...");
                login.setEnabled(false);
                if(id.getText().toString().equals("")|| pwd.getText().toString().equals(""))
                {
                    login.setText("LOGIN");
                    login.setEnabled(true);
                    Toast toast = Toast.makeText(TeacherLogin.this,"Empty fields not allowed",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP,0,250);
                    toast.show();
                }
                else {

                    login(id.getText().toString(), pwd.getText().toString());

                }
            }
        });

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(TeacherLogin.this,MainActivity.class));
        finish();
    }

    private void login(String usr,String pwd)
    {
        mAuth.signInWithEmailAndPassword(usr,pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("firebase", mAuth.toString());
                            FirebaseUser user = mAuth.getCurrentUser();
                            login.setText("Loading...");
                            login.setEnabled(false);
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("Teacher").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChild(mAuth.getCurrentUser().getUid())){
                                        updateUI(user);
                                    }
                                    else {
                                        Toast.makeText(TeacherLogin.this, "Student id not accepted here", Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                        login.setText("Login");
                                        login.setEnabled(true);
                                        FirebaseAuth.getInstance().signOut();
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("firebase", "signInWithEmail:failure", task.getException());
                            Toast.makeText(TeacherLogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser account) {
        if(account != null){
            Toast.makeText(this,"You Signed In successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,tclass.class));
            attemptspwd.setVisibility(View.INVISIBLE);

        }else {

            attemptspwd.setVisibility(View.VISIBLE);
            login.setText("LOGIN");
            login.setEnabled(true);
            id.requestFocus();
            if ( i>1) {

                id.setText("");
                pwd.setText("");
                //Toast.makeText(getApplicationContext(), "Invalid credentials\nAttempt no.: "+i, Toast.LENGTH_LONG).show();

                i--;
                if(i>1)
                    attemptspwd.setText("Attempts left: "+i);
                else if(i==1)
                    attemptspwd.setText("Final Attempt ");
                else
                    attemptspwd.setText("Exceeded the limit");

            }
            else if ( i==1)
            {

                Toast toast = Toast.makeText(this, "Reached 3 attempts", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 0, 250);
                toast.show();
                Intent forgotpwd = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sjc.linways.com/student/forgotpassword/forgotpassword.php"));
                startActivity(forgotpwd);
                System.exit(1);
            }



        }
    }

    @Override
    public void onStart(){
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            updateUI(currentUser);
        }
    }

    public void Failure(){

    }

}