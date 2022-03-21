package com.example.attendance;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.*;
import android.util.Log;
import android.view.*;
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

public class StudentLogin extends AppCompatActivity {
    EditText regno, pwd;
    TextView attemptspwd;
    Button login;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    int i = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        regno = (EditText) findViewById(R.id.RegNO);
        pwd = (EditText) findViewById(R.id.StudentPassword);
        attemptspwd = findViewById(R.id.attemptspwd);
         login =(Button)findViewById(R.id.student);
        mAuth = FirebaseAuth.getInstance();
                login.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        login.setText("Loading...");
                        login.setEnabled(false);
                        if(regno.getText().toString().equals("")|| pwd.getText().toString().equals(""))
                        {
                            login.setText("LOGIN");
                            login.setEnabled(true);
                            Toast toast = Toast.makeText(StudentLogin.this,"Empty fields not allowed",Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.TOP,0,250);
                            toast.show();
                        }
                        else
                            login(regno.getText().toString(),pwd.getText().toString());


                    }
                });



    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(StudentLogin.this,MainActivity.class));
        finish();
    }


    private void login(String usr,String pwd)
    {
        mAuth.signInWithEmailAndPassword(usr,pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        final boolean[] flag = {false};
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("firebase", "user:" + mAuth.getCurrentUser().getUid());
                            FirebaseUser user = mAuth.getCurrentUser();
                            login.setText("Loading...");
                            login.setEnabled(false);
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("Student").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChild(mAuth.getCurrentUser().getUid())){
                                        updateUI(user);

                                    }
                                    else {
                                        Toast.makeText(StudentLogin.this, "Student id not accepted here", Toast.LENGTH_SHORT).show();
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
                        }
                                else {
                            Log.w("firebase", "signInWithEmail:failure", task.getException());
                            Toast.makeText(StudentLogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser account) {
        if(account != null){

            Toast.makeText(this,"You Signed In successfully",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,tt.class));
            attemptspwd.setVisibility(View.INVISIBLE);

        }else
            onfail();
    }

    public void onfail(){
        attemptspwd.setVisibility(View.VISIBLE);
        login.setText("LOGIN");
        login.setEnabled(true);
        regno.requestFocus();
        if ( i>1) {

            regno.setText("");
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
            Intent forgotpwd = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sjc.linways.com/student/forgotpassword/forgotpassword.php"));
            startActivity(forgotpwd);
            System.exit(1);
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

}



