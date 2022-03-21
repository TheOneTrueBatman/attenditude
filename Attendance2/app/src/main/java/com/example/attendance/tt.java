package com.example.attendance;

import static java.util.Calendar.MONDAY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class tt extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private static final IntentFilter s_intentFilter;
    View layout;
    TextView weekend;
    TextView sub1, sub2, sub3, sub4,tt1,tt2,tt3,tt4;
    ImageView m1, m2, m3, m4;
    TextView day;
    static {
        s_intentFilter = new IntentFilter();
        s_intentFilter.addAction(Intent.ACTION_TIME_TICK);
        s_intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        s_intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tt);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextAppearance(this, R.style.startrek);
        layout = findViewById(R.id.ttlayout);
        weekend = findViewById(R.id.weekend);

        m1 = findViewById(R.id.mk1);
        m2 = findViewById(R.id.mk2);
        m3 = findViewById(R.id.mk3);
        m4 = findViewById(R.id.mk4);

        tt1 = findViewById(R.id.t1);
        tt2 = findViewById(R.id.t2);
        tt3 = findViewById(R.id.t3);
        tt4 = findViewById(R.id.t4);
        time();
        sub1 = (TextView) findViewById(R.id.sub1);
        sub2 = (TextView) findViewById(R.id.sub2);
        sub3 = (TextView) findViewById(R.id.sub3);
        sub4 = (TextView) findViewById(R.id.sub4);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
         day = findViewById(R.id.day);
        FloatingActionButton ff = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        String weekDay = dayFormat.format(calendar.getTime());
        day.setText(weekDay);
        sub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"LOADING...",Toast.LENGTH_SHORT).show();
            }
        });
        sub3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"LOADING...",Toast.LENGTH_SHORT).show();
            }
        });
        sub4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"LOADING...",Toast.LENGTH_SHORT).show();
            }
        });
        sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"LOADING...",Toast.LENGTH_SHORT).show();
            }
        });
        registerReceiver(m_timeChangedReceiver, s_intentFilter);
        ff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(tt.this, fulltt.class));
            }
        });
//        sub1.setClickable(false);
//        sub2.setClickable(false);
//        sub3.setClickable(false);
//        sub4.setClickable(false);
        getTimetable();

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        time();
                        getTimetable();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.studentactions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(tt.this, StudentLogin.class));
                return true;

            case R.id.over:startActivity(new Intent(getApplicationContext(),overall.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(tt.this, MainActivity.class));
        finish();
    }


    private void getTimetable() {
        LocalDate date = null;
        if (android.os.Build.VERSION.SDK_INT >= VERSION_CODES.O) {
            date = LocalDate.now();
            DayOfWeek day = date.getDayOfWeek();
            if (day.getValue() >= 6) {
                layout.setVisibility(View.GONE);
                weekend.setVisibility(View.VISIBLE);

            } else {
                layout.setVisibility(View.VISIBLE);
                weekend.setVisibility(View.GONE);
                mDatabase.child("TimeTable").child("SYMCS").child(String.valueOf(day.getValue() - 1)).child("sub").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        sub1.setText(snapshot.child("0").child("lect").getValue().toString());
                        sub1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert(snapshot.child("0").child("lect").getValue().toString());
                            }
                        });
                        sub2.setClickable(true);
                        sub2.setText(snapshot.child("1").child("lect").getValue().toString());
                        sub2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert(snapshot.child("1").child("lect").getValue().toString());
                            }
                        });
                        sub2.setClickable(true);
                        sub3.setText(snapshot.child("2").child("lect").getValue().toString());
                        sub3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert(snapshot.child("2").child("lect").getValue().toString());
                            }
                        });
                        sub3.setClickable(true);
                        sub4.setText(snapshot.child("3").child("lect").getValue().toString());
                        sub4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert(snapshot.child("3").child("lect").getValue().toString());
                            }
                        });

                        test(snapshot);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(tt.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                    }
                });

            }

        }

    }


    private final BroadcastReceiver m_timeChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(Intent.ACTION_TIME_CHANGED) ||
                    action.equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
                String weekDay = dayFormat.format(calendar.getTime());
                TextView day = findViewById(R.id.day);
                day.setText(weekDay);
                getTimetable();
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(m_timeChangedReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTimetable();
    }

    public void time()
    {
        SimpleDateFormat date = new SimpleDateFormat("HH");
        String currentDate = date.format(new Date());
        switch(currentDate)
        {
            case "09": tt1.setTextColor(Color.BLACK);
                tt2.setTextColor(Color.WHITE);
                tt3.setTextColor(Color.WHITE);
                tt4.setTextColor(Color.WHITE);
                break;
            case "10": tt1.setTextColor(Color.WHITE);
                tt2.setTextColor(Color.BLACK);
                tt3.setTextColor(Color.WHITE);
                tt4.setTextColor(Color.WHITE);
                break;
            case "11": tt1.setTextColor(Color.WHITE);
                tt2.setTextColor(Color.WHITE);
                tt3.setTextColor(Color.BLACK);
                tt4.setTextColor(Color.WHITE);
                break;
            case "12": tt1.setTextColor(Color.WHITE);
                tt2.setTextColor(Color.WHITE);
                tt3.setTextColor(Color.WHITE);
                tt4.setTextColor(Color.BLACK);
                break;
            default:tt1.setTextColor(Color.WHITE);
                tt2.setTextColor(Color.WHITE);
                tt3.setTextColor(Color.WHITE);
                tt4.setTextColor(Color.WHITE);

        }
    }

    public void test(DataSnapshot tt) {
        String textView;
        SimpleDateFormat date = new SimpleDateFormat("dd-M-yyyy");
        String currentDate = date.format(new Date());
        textView = currentDate;
        mDatabase.child("Student").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot student) {

                mDatabase.child("Attendance").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot att) {
                        for(int i=9;i<=12;i++)
                        {String time;
                                time= i+":00-"+i+":50";

                            switch(i)
                            {
                                case 9:if(!tt.child("0").child("lect").getValue().toString().equals("FREE"))
                                            if(!tt.child("0").child("lect").getValue().toString().equals("ELECTIVE")) {
                                                if (att.child(tt.child("0").child("lect").getValue().toString()).child(textView + "_" + time).exists())
                                                    if (att.child(tt.child("0").child("lect").getValue().toString()).child(textView + "_" + time)
                                                            .child(student.child("REG_NO").getValue().toString()).getValue().toString().equals("present")) {
                                                        m1.setImageResource(R.drawable.green);
                                                        sub1.setTextColor(Color.GREEN);
                                                    } else {
                                                        m1.setImageResource(R.drawable.red);
                                                        sub1.setTextColor(Color.RED);
                                                    }
                                                else {
                                                    m1.setImageResource(R.drawable.gray);
                                                    sub1.setTextColor(Color.WHITE);
                                                }
                                            }
                                            else
                                                if(att.child(student.child("ELECTIVE").getValue().toString()).child(textView+"_"+time).exists())
                                                    if(att.child(student.child("ELECTIVE").getValue().toString()).child(textView+"_"+time)
                                                            .child(student.child("REG_NO").getValue().toString()).getValue().toString().equals("present")) {
                                                        m1.setImageResource(R.drawable.green);
                                                        sub1.setTextColor(Color.GREEN);
                                                    }
                                                    else {
                                                        m1.setImageResource(R.drawable.red);
                                                        sub1.setTextColor(Color.RED);
                                                    }
                                                else {
                                                    m1.setImageResource(R.drawable.gray);
                                                    sub1.setTextColor(Color.WHITE);
                                                }

                                                break;

                                case 10: if(!tt.child("1").child("lect").getValue().toString().equals("FREE"))
                                    if(!tt.child("1").child("lect").getValue().toString().equals("ELECTIVE"))
                                        if(att.child(tt.child("1").child("lect").getValue().toString()).child(textView+"_"+time).exists())
                                            if(att.child(tt.child("1").child("lect").getValue().toString()).child(textView+"_"+time)
                                                    .child(student.child("REG_NO").getValue().toString()).getValue().toString().equals("present")) {
                                                m2.setImageResource(R.drawable.green);
                                                sub2.setTextColor(Color.GREEN);
                                            }
                                            else {
                                                m2.setImageResource(R.drawable.red);
                                                sub2.setTextColor(Color.RED);
                                            }
                                        else {
                                            m2.setImageResource(R.drawable.gray);
                                            sub2.setTextColor(Color.WHITE);
                                        }
                                    else
                                    if(att.child(student.child("ELECTIVE").getValue().toString()).child(textView+"_"+time).exists())
                                        if(att.child(student.child("ELECTIVE").getValue().toString()).child(textView+"_"+time)
                                                .child(student.child("REG_NO").getValue().toString()).getValue().toString().equals("present")) {
                                            m2.setImageResource(R.drawable.green);
                                            sub2.setTextColor(Color.GREEN);
                                        }
                                        else {
                                            m2.setImageResource(R.drawable.red);
                                            sub2.setTextColor(Color.RED);
                                        }
                                    else {
                                        m2.setImageResource(R.drawable.gray);
                                        sub2.setTextColor(Color.WHITE);
                                    }
                                    break;

                                case 11:
                                    if(!tt.child("2").child("lect").getValue().toString().equals("FREE"))
                                    if(!tt.child("2").child("lect").getValue().toString().equals("ELECTIVE"))
                                        if(att.child(tt.child("2").child("lect").getValue().toString()).child(textView+"_"+time).exists())
                                            if(att.child(tt.child("2").child("lect").getValue().toString()).child(textView+"_"+time)
                                                    .child(student.child("REG_NO").getValue().toString()).getValue().toString().equals("present")) {
                                                m3.setImageResource(R.drawable.green);
                                                sub3.setTextColor(Color.GREEN);
                                            }
                                            else {
                                                m3.setImageResource(R.drawable.red);
                                                sub3.setTextColor(Color.RED);
                                            }
                                        else {
                                            m3.setImageResource(R.drawable.gray);
                                            sub3.setTextColor(Color.WHITE);
                                        }
                                    else
                                    if(att.child(student.child("ELECTIVE").getValue().toString()).child(textView+"_"+time).exists())
                                        if(att.child(student.child("ELECTIVE").getValue().toString()).child(textView+"_"+time)
                                                .child(student.child("REG_NO").getValue().toString()).getValue().toString().equals("present")) {
                                            m3.setImageResource(R.drawable.green);
                                            sub3.setTextColor(Color.GREEN);
                                        }

                                        else {
                                            m3.setImageResource(R.drawable.red);
                                            sub3.setTextColor(Color.RED);
                                        }
                                    else {
                                        m3.setImageResource(R.drawable.gray);
                                        sub3.setTextColor(Color.WHITE);
                                    }
                                    break;

                                case 12: if(!tt.child("3").child("lect").getValue().toString().equals("FREE"))
                                    if(!tt.child("3").child("lect").getValue().toString().equals("ELECTIVE"))
                                        if(att.child(tt.child("3").child("lect").getValue().toString()).child(textView+"_"+time).exists())
                                            if(att.child(tt.child("3").child("lect").getValue().toString()).child(textView+"_"+time)
                                                    .child(student.child("REG_NO").getValue().toString()).getValue().toString().equals("present")) {
                                                m4.setImageResource(R.drawable.green);
                                                sub4.setTextColor(Color.GREEN);
                                            }
                                            else {
                                                m4.setImageResource(R.drawable.red);
                                                sub4.setTextColor(Color.RED);
                                            }
                                        else {
                                            m4.setImageResource(R.drawable.gray);
                                            sub4.setTextColor(Color.WHITE);
                                        }
                                    else
                                    if(att.child(student.child("ELECTIVE").getValue().toString()).child(textView+"_"+time).exists())
                                        if(att.child(student.child("ELECTIVE").getValue().toString()).child(textView+"_"+time)
                                                .child(student.child("REG_NO").getValue().toString()).getValue().toString().equals("present")) {
                                            m4.setImageResource(R.drawable.green);
                                            sub4.setTextColor(Color.GREEN);
                                        }
                                        else {
                                            m4.setImageResource(R.drawable.red);
                                            sub4.setTextColor(Color.RED);
                                        }
                                    else {
                                        m4.setImageResource(R.drawable.gray);
                                        sub4.setTextColor(Color.WHITE);
                                    }


                                    break;
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
    void alert(String sub)
    {
        mDatabase.child("Student").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot student) {
                mDatabase.child("Attendance").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot attendance) {

                        int elect,dwdmt,andt;
                        float elec,dwdm,and;

                        int total=Integer.parseInt(attendance.child("DWDM").child("count")
                                .child(student.child("REG_NO").getValue().toString()).child("present").getValue().toString())
                                +
                                Integer.parseInt(attendance.child("Android").child("count")
                                        .child(student.child("REG_NO").getValue().toString()).child("present").getValue().toString());

                        dwdmt= Integer.parseInt(attendance.child("DWDM").child("count")
                                .child(student.child("REG_NO").getValue().toString()).child("present").getValue().toString())
                                +
                                Integer.parseInt(attendance.child("DWDM").child("count")
                                        .child(student.child("REG_NO").getValue().toString()).child("absent").getValue().toString());
                        dwdm=Integer.parseInt(attendance.child("DWDM").child("count")
                                .child(student.child("REG_NO").getValue().toString()).child("present").getValue().toString())
                                *100/(float)dwdmt;

                        andt= Integer.parseInt(attendance.child("Android").child("count")
                                .child(student.child("REG_NO").getValue().toString()).child("present").getValue().toString())
                                +
                                Integer.parseInt(attendance.child("Android").child("count")
                                        .child(student.child("REG_NO").getValue().toString()).child("absent").getValue().toString());
                        and=Integer.parseInt(attendance.child("Android").child("count")
                                .child(student.child("REG_NO").getValue().toString()).child("present").getValue().toString())
                                *100/(float)andt;

                        if(attendance.child("iot").child("count")
                                .child(student.child("REG_NO").getValue().toString()).exists())
                        {
                            elect= Integer.parseInt(attendance.child("iot").child("count")
                                    .child(student.child("REG_NO").getValue().toString()).child("present").getValue().toString())
                                    +
                                    Integer.parseInt(attendance.child("iot").child("count")
                                            .child(student.child("REG_NO").getValue().toString()).child("absent").getValue().toString());
                            elec=Integer.parseInt(attendance.child("iot").child("count")
                                    .child(student.child("REG_NO").getValue().toString()).child("present").getValue().toString())
                                    *100/(float)elect;
                            total=total+Integer.parseInt(attendance.child("iot").child("count")
                                    .child(student.child("REG_NO").getValue().toString()).child("present").getValue().toString());
                        }
                        else
                        {
                            elect= Integer.parseInt(attendance.child("DS").child("count")
                                    .child(student.child("REG_NO").getValue().toString()).child("present").getValue().toString())
                                    +
                                    Integer.parseInt(attendance.child("DS").child("count")
                                            .child(student.child("REG_NO").getValue().toString()).child("absent").getValue().toString());
                            elec=Integer.parseInt(attendance.child("DS").child("count")
                                    .child(student.child("REG_NO").getValue().toString()).child("present").getValue().toString())
                                    *100/(float)elect;
                            total=total+Integer.parseInt(attendance.child("DS").child("count")
                                    .child(student.child("REG_NO").getValue().toString()).child("present").getValue().toString());
                        }
                        float over;
                        switch(sub)
                        {
                            case "DWDM": over= (float)total*100/(andt+dwdmt+elect);
                                        dialog(sub,over,dwdm);
                                break;
                            case "Android":over= (float)total*100/(andt+dwdmt+elect);
                                                 dialog(sub,over,and);
                                            break;
                            case "ELECTIVE":over= (float)total*100/(andt+dwdmt+elect);
                                                dialog(sub,over,elec);
                                                break;
                            case "free": Toast.makeText(getApplicationContext(),"Free",Toast.LENGTH_SHORT).show();
                            default:
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
    void dialog(String sub,float over,float pres)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(sub + " Present Precentage: " + pres + "%\n");
        buffer.append("Overall Present Precentage: " + over + "%\n");
        AlertDialog.Builder builder = new AlertDialog.Builder(tt.this);
        builder.setCancelable(true);
        builder.setTitle("ATTENDANCE RECORD");
        builder.setMessage(buffer.toString());
        builder.setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }
        )
                .create();
        builder.show();
    }
}