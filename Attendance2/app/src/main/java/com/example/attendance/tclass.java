package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class tclass extends AppCompatActivity {
    private static final String tag="tclass";
    private  String date;
    private String dow;
    private TextView dispdate;
    private Spinner spinner;
    String hour;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    Button sy ;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tclass);
        dispdate=(TextView)findViewById(R.id.date);
        spinner=(Spinner)findViewById(R.id.spinner);
        sy =(Button)findViewById(R.id.sy);
        sy.setText("Loading...");
        sy.setEnabled(false);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(myToolbar);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        //array adapter for drop down
        ArrayAdapter<String> myAdapt=new ArrayAdapter<String>(tclass.this, R.layout.custom_spinner,getResources().getStringArray(R.array.slots)){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.DKGRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        myAdapt.setDropDownViewResource(R.layout.custom_drop);
        spinner.setAdapter(myAdapt);



        //date module
        dispdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog=new DatePickerDialog(tclass.this,R.style.DialogTheme,mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2196F3")));
                dialog.show();
            }
        });
        mDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar cal=Calendar.getInstance();
                month=month+1;
            Log.d(tag,"onDateSet:dd/mm/yyy:"+day+"/"+month+"/"+year);
                GregorianCalendar dayofweek = new GregorianCalendar(year, month, day-4);
                int dayOfWeek=dayofweek.get(Calendar.DAY_OF_WEEK);
                dow=Integer.toString(dayOfWeek);

                date=day+"-"+month+"-"+year;
            dispdate.setText(date);
            }
        };
        mDatabase.child("Teacher").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sy.setText("SYMCS");
                sy.setEnabled(true);
                sy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (spinner.getSelectedItem().toString())
                        {
                            case "9:00-9:50am":hour="9";
                            break;
                            case "10:00-10:50am":hour="10";
                                break;
                            case "11:00-11:50am":hour="11";
                                break;
                            case "12:00-12:50am":hour="12";
                                break;
                            default:
                        }

                        if(snapshot.child("SYMCS").getValue().toString().equals("iot")) {
                            if (dispdate.getText().toString().equals("")|| spinner.getSelectedItem().toString().equals("Select the hour")) {
                                Toast toast = Toast.makeText(tclass.this,"Empty fields not allowed",Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                            }
                            else {
                                Intent intent = new Intent(tclass.this, iot.class);
                                intent.putExtra("Date", date);
                                intent.putExtra("Hour", hour);
                                intent.putExtra("Day", dow);
                                startActivity(intent);
                            }
                        }
                        else if (snapshot.child("SYMCS").getValue().toString().equals("DS")) {
                            if(dispdate.getText().toString().equals("")|| spinner.getSelectedItem().toString().equals("Select the hour")) {
                                Toast toast = Toast.makeText(tclass.this,"Empty fields not allowed",Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                            }
                            else {Intent intent = new Intent(tclass.this, ds.class);
                                intent.putExtra("Date", date);
                                intent.putExtra("Hour", hour);
                                intent.putExtra("Day", dow);
                                startActivity(intent);
                            }
                        }
                        else {
                            if(dispdate.getText().toString().equals("")|| spinner.getSelectedItem().toString().equals("Select the hour")) {
                                Toast toast = Toast.makeText(tclass.this,"Empty fields not allowed",Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                            }
                            else {
                                //Log.d("tag",snapshot.child("SYMCS").getValue().toString());
                                Intent intent = new Intent(tclass.this, TAttendance.class);
                                intent.putExtra("Date", date);
                                intent.putExtra("Hour", hour);
                                intent.putExtra("Day", dow);
                                startActivity(intent);
                            }
                        }}
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(tclass.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(tclass.this, TeacherLogin.class));
                return true;
            default:
                return true;
        }
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(tclass.this,MainActivity.class));
        finish();
    }
}