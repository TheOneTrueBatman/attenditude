package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class iot extends AppCompatActivity {
    int count=0;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    ListView listViewData;
    ArrayAdapter<String> adapter;
    String slott;
    String dt;
    String hour;
    String dow ;
    TextView time,load;
    String[] studentsList={"20MCS01","20MCS02","20MCS03","20MCS06","20MCS09",
            "20MCS12","20MCS13","20MCS14","20MCS22"};
    int size= studentsList.length;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tattendance);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextAppearance(this, R.style.startrek);

        Intent intent = getIntent();
        dt = intent.getStringExtra("Date");
        hour = intent.getStringExtra("Hour");
        dow = intent.getStringExtra("Day");
        slott= dt+"_"+hour+":00"+"-"+hour+":50";

        final String value = intent.getStringExtra("idkey");
        Toast toast = Toast.makeText(this, "Welcome ", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 250);
        toast.show();
        listViewData=findViewById(R.id.listviewdata);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,studentsList);
        listViewData.setAdapter(adapter);
        load= findViewById(R.id.loading);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        getData();
        get();
//        listViewData.setVisibility(View.VISIBLE);
//        load.setVisibility(View.GONE);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {get();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );


    }



    private  void getData(){
        String date;
        time = findViewById(R.id.hr);
        int hr = Integer.parseInt(hour);
        time.setText(hr+":00-"+hr+":50");
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date =dt;
            Toast.makeText(this, "  "+dow, Toast.LENGTH_SHORT).show();
            int day =Integer.parseInt(dow);
            if (day >= 6) {
                listViewData.setVisibility(View.GONE);
                load.setVisibility(View.VISIBLE);
                load.setText("Weekend");
            } else {
                load.setText("LOADING...");
                load.setVisibility(View.VISIBLE);
                mDatabase.child("Teacher").child(mAuth.getCurrentUser().getUid()).child("SYMCS").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snap) {
                        mDatabase.child("TimeTable").child("SYMCS").child(String.valueOf(day - 1)).child("sub").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                switch (hr) {
                                    case 9:
                                        if (snapshot.child("0").child("lect").getValue().toString().equals("ELECTIVE")) {
                                            listViewData.setVisibility(View.VISIBLE);
                                            load.setVisibility(View.GONE);
                                        } else {
                                            listViewData.setVisibility(View.GONE);
                                            load.setVisibility(View.VISIBLE);
                                            load.setText("No Lecture this hour");
                                        }break;
                                    case 10:
                                        if (snapshot.child("1").child("lect").getValue().toString().equals("ELECTIVE")) {
                                            listViewData.setVisibility(View.VISIBLE);
                                            load.setVisibility(View.GONE);
                                        } else {
                                            listViewData.setVisibility(View.GONE);
                                            load.setVisibility(View.VISIBLE);
                                            load.setText("No Lecture this hour");
                                        }break;
                                    case 11:
                                        if (snapshot.child("2").child("lect").getValue().toString().equals("ELECTIVE")) {
                                            listViewData.setVisibility(View.VISIBLE);
                                            load.setVisibility(View.GONE);
                                        } else {
                                            listViewData.setVisibility(View.GONE);
                                            load.setVisibility(View.VISIBLE);
                                            load.setText("No Lecture this hour");
                                        }break;
                                    case 12:
                                        if (snapshot.child("3").child("lect").getValue().toString().equals("ELECTIVE")) {
                                            listViewData.setVisibility(View.VISIBLE);
                                            load.setVisibility(View.GONE);
                                        } else {
                                            listViewData.setVisibility(View.GONE);
                                            load.setVisibility(View.VISIBLE);
                                            load.setText("No Lecture this hour");
                                        }break;
                                    default:  listViewData.setVisibility(View.GONE);
                                        load.setVisibility(View.VISIBLE);
                                        load.setText("No Lecture this hour");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }


        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.teacheract, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.all:
                for(int i=0;i<listViewData.getCount();i++){
                    listViewData.setItemChecked( i,true);
                }
                break;
            case R.id.clear:
                for(int i=0;i<listViewData.getCount();i++){
                    listViewData.setItemChecked( i,false);
                }
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent1= new Intent(iot.this,TeacherLogin.class);
                startActivity(intent1);
                return true;

            case R.id.submit:
                Attendance();
                String markedPresent="Marked present: \n";
                for(int i=0;i<listViewData.getCount();i++)
                {

                    if (listViewData.isItemChecked(i)) {
                        markedPresent += listViewData.getItemAtPosition(i) + "\n";
                        count++;
                    }
                }
                if(count==size)
                {
                    Toast.makeText(iot.this, "ALL PRESENT", Toast.LENGTH_SHORT).show();
                }
                else if(count==0)
                {
                    Toast.makeText(iot.this, "ALL ABSENT", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, markedPresent, Toast.LENGTH_LONG).show();
                }
                count=0;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    void Attendance()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        mDatabase.child("Teacher").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot teacher) {
                mDatabase.child("Attendance").child(teacher.child("SYMCS").getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(int i=0;i<listViewData.getCount();i++) {
                            if (listViewData.isItemChecked(i)) {
                                if (snapshot.child(slott).exists()) {
                                    if (snapshot.child(slott).child(listViewData.getItemAtPosition(i).toString()).getValue().toString().equals("absent")) {
                                        mDatabase.child("Attendance").child(teacher.child("SYMCS").getValue().toString())
                                                .child(slott)
                                                .child(listViewData.getItemAtPosition(i).toString()).setValue("present");
                                        Count("AtoP", i,teacher);
                                    }
                                }

                                else
                                {
                                    mDatabase.child("Attendance").child(teacher.child("SYMCS").getValue().toString())
                                            .child(slott)
                                            .child(listViewData.getItemAtPosition(i).toString()).setValue("present");
                                    Count("newP", i,teacher);
                                }
                            }


                            else {


                                if (snapshot.child(slott).exists()) {
                                    if (snapshot.child(slott).child(listViewData.getItemAtPosition(i).toString()).getValue().toString().equals("present")) {
                                        mDatabase.child("Attendance").child(teacher.child("SYMCS").getValue().toString())
                                                .child(slott)
                                                .child(listViewData.getItemAtPosition(i).toString()).setValue("absent");
                                        Count("PtoA", i, teacher );
                                    }
                                }
                                else
                                {mDatabase.child("Attendance").child(teacher.child("SYMCS").getValue().toString())
                                        .child(slott)
                                        .child(listViewData.getItemAtPosition(i).toString()).setValue("absent");
                                    Count("newA",i,teacher);}

                            }
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i= new Intent(getApplicationContext(),tclass.class);
        startActivity(i);
    }

    void Count(String x, int i, DataSnapshot teacher)
    {
        final int[] a = new int[1];
        final int[] p = new int[1];
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mDatabase.child("Attendance").child(teacher.child("SYMCS").getValue().toString())
                .child("count").child(listViewData.getItemAtPosition(i).toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                switch(x)
                { case "AtoP": a[0] = Integer.parseInt(snapshot.child("absent").getValue().toString())-1;
                    p[0] = Integer.parseInt(snapshot.child("present").getValue().toString())+1;
                    mDatabase.child("Attendance").child(teacher.child("SYMCS").getValue().toString())
                            .child("count").child(listViewData.getItemAtPosition(i).toString())
                            .child("absent").setValue(a[0]);
                    mDatabase.child("Attendance").child(teacher.child("SYMCS").getValue().toString())
                            .child("count").child(listViewData.getItemAtPosition(i).toString())
                            .child("present").setValue(p[0]);
                    break;

                    case "PtoA":
                        a[0] = Integer.parseInt(snapshot.child("absent").getValue().toString())+1;
                        p[0] = Integer.parseInt(snapshot.child("present").getValue().toString())-1;
                        mDatabase.child("Attendance").child(teacher.child("SYMCS").getValue().toString())
                                .child("count").child(listViewData.getItemAtPosition(i).toString())
                                .child("absent").setValue(a[0]);
                        mDatabase.child("Attendance").child(teacher.child("SYMCS").getValue().toString())
                                .child("count").child(listViewData.getItemAtPosition(i).toString())
                                .child("present").setValue(p[0]);
                        break;

                    case "newP":p[0] = Integer.parseInt(snapshot.child("present").getValue().toString())+1;
                        mDatabase.child("Attendance").child(teacher.child("SYMCS").getValue().toString())
                                .child("count").child(listViewData.getItemAtPosition(i).toString())
                                .child("present").setValue(p[0]);
                        break;

                    case "newA":  a[0] = Integer.parseInt(snapshot.child("absent").getValue().toString())+1;
                        mDatabase.child("Attendance").child(teacher.child("SYMCS").getValue().toString())
                                .child("count").child(listViewData.getItemAtPosition(i).toString())
                                .child("absent").setValue(a[0]);
                        break;
                    default:


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    void get()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mDatabase.child("Teacher").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot teacher) {
                mDatabase.child("Attendance").child(teacher.child("SYMCS").getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(slott).exists()) {
                            for (int i = 0; i < listViewData.getCount(); i++) {
                                if (snapshot.child(slott).child(listViewData.getItemAtPosition(i).toString()).getValue().toString().equals("present")) {
                                    listViewData.setItemChecked( i,true);
                                }
                                else
                                    listViewData.setItemChecked( i,false);
                            }
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}













