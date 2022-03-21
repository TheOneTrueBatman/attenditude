package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class fulltt extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    TextView sub1,sub2,sub3,sub4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fulltt);
        BottomNavigationView navigation = findViewById(R.id.bottomNavigationView);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextAppearance(this, R.style.startrek);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        TextView day = (TextView) findViewById(R.id.day2);
        day.setText("MONDAY");
        sub1 = (TextView) findViewById(R.id.sub1);
        sub2 = (TextView) findViewById(R.id.sub2);
        sub3 = (TextView) findViewById(R.id.sub3);
        sub4 = (TextView) findViewById(R.id.sub4);

        mDatabase.child("TimeTable").child("SYMCS").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sub1.setText(snapshot.child("0").child("sub").child("0").child("lect").getValue().toString());
                sub2.setText(snapshot.child("0").child("sub").child("1").child("lect").getValue().toString());
                sub3.setText(snapshot.child("0").child("sub").child("2").child("lect").getValue().toString());
                sub4.setText(snapshot.child("0").child("sub").child("3").child("lect").getValue().toString());
                navigation.setOnNavigationItemSelectedListener(
                        new BottomNavigationView.OnNavigationItemSelectedListener() {
                            @Override
                            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.monday:
                                        day.setText("MONDAY");
                                        sub1.setText(snapshot.child("0").child("sub").child("0").child("lect").getValue().toString());
                                        sub2.setText(snapshot.child("0").child("sub").child("1").child("lect").getValue().toString());
                                        sub3.setText(snapshot.child("0").child("sub").child("2").child("lect").getValue().toString());
                                        sub4.setText(snapshot.child("0").child("sub").child("3").child("lect").getValue().toString());
                                        item.setCheckable(true);
                                        return true;

                                    case R.id.tuesday:
                                        day.setText("TUESDAY");
                                        sub1.setText(snapshot.child("1").child("sub").child("0").child("lect").getValue().toString());
                                        sub2.setText(snapshot.child("1").child("sub").child("1").child("lect").getValue().toString());
                                        sub3.setText(snapshot.child("1").child("sub").child("2").child("lect").getValue().toString());
                                        sub4.setText(snapshot.child("1").child("sub").child("3").child("lect").getValue().toString());
                                        item.setCheckable(true);
                                        return true;

                                    case R.id.wednesday:
                                        day.setText("WEDNESDAY");
                                        sub1.setText(snapshot.child("2").child("sub").child("0").child("lect").getValue().toString());
                                        sub2.setText(snapshot.child("2").child("sub").child("1").child("lect").getValue().toString());
                                        sub3.setText(snapshot.child("2").child("sub").child("2").child("lect").getValue().toString());
                                        sub4.setText(snapshot.child("2").child("sub").child("3").child("lect").getValue().toString());
                                        item.setCheckable(true);
                                        return true;


                                    case R.id.thursday:
                                        day.setText("THURSDAY");
                                        sub1.setText(snapshot.child("3").child("sub").child("0").child("lect").getValue().toString());
                                        sub2.setText(snapshot.child("3").child("sub").child("1").child("lect").getValue().toString());
                                        sub3.setText(snapshot.child("3").child("sub").child("2").child("lect").getValue().toString());
                                        sub4.setText(snapshot.child("3").child("sub").child("3").child("lect").getValue().toString());
                                        item.setCheckable(true);
                                        return true;


                                    case R.id.friday:day.setText("FRIDAY");
                                        sub1.setText(snapshot.child("4").child("sub").child("0").child("lect").getValue().toString());
                                        sub2.setText(snapshot.child("4").child("sub").child("1").child("lect").getValue().toString());
                                        sub3.setText(snapshot.child("4").child("sub").child("2").child("lect").getValue().toString());
                                        sub4.setText(snapshot.child("4").child("sub").child("3").child("lect").getValue().toString());
                                        item.setCheckable(true);
                                        return true;

                                    default:
                                        return true;
                                }


                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(fulltt.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }











    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.studentactions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(fulltt.this,StudentLogin.class));
                return true;
            case R.id.over:startActivity(new Intent(getApplicationContext(),overall.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
