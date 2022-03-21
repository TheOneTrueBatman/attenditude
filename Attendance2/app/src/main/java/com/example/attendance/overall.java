package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class overall extends AppCompatActivity {

    TextView Dwdmt;
    TextView Dwdm;
    TextView Dwdmp;

    TextView Andt;
    TextView And;
    TextView Andp;

    TextView Elect;
    TextView Elec;
    TextView Elecp;
    TextView elective;

    TextView tott;
    TextView tot;
    TextView totp;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overall);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        Dwdmt = findViewById(R.id.tot1);
        Dwdm = findViewById(R.id.attend1);
        Dwdmp = findViewById(R.id.percent1);

        Andt = findViewById(R.id.tot2);
        And = findViewById(R.id.attend2);
        Andp = findViewById(R.id.percent2);

        Elect = findViewById(R.id.tot3);
        Elec = findViewById(R.id.attend3);
        Elecp = findViewById(R.id.percent3);
        elective= findViewById(R.id.elective);

        tott = findViewById(R.id.tot4);
        tot = findViewById(R.id.attend4);
        totp = findViewById(R.id.percent4);
        mDatabase.child("Student").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot student) {
                mDatabase.child("Attendance").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot attendance) {

                        int elect, dwdmt, andt;
                        float elec, dwdm, and;

                        int total = Integer.parseInt(attendance.child("DWDM").child("count")
                                .child(student.child("REG_NO").getValue().toString()).child("present").getValue().toString())
                                +
                                Integer.parseInt(attendance.child("Android").child("count")
                                        .child(student.child("REG_NO").getValue().toString()).child("present").getValue().toString());
                        Dwdm.setText(attendance.child("DWDM").child("count")
                                .child(student.child("REG_NO").getValue().toString()).child("present").getValue().toString());
                        And.setText(attendance.child("Android").child("count")
                                .child(student.child("REG_NO").getValue().toString()).child("present").getValue().toString());

                        dwdmt = Integer.parseInt(attendance.child("DWDM").child("count")
                                .child(student.child("REG_NO").getValue().toString()).child("present").getValue().toString())
                                +
                                Integer.parseInt(attendance.child("DWDM").child("count")
                                        .child(student.child("REG_NO").getValue().toString()).child("absent").getValue().toString());
                        Dwdmt.setText(Integer.toString(dwdmt));

                        dwdm = Integer.parseInt(attendance.child("DWDM").child("count")
                                .child(student.child("REG_NO").getValue().toString()).child("present").getValue().toString())
                                * 100 / (float) dwdmt;
                        Dwdmp.setText(Float.toString(dwdm)+"%");

                        andt = Integer.parseInt(attendance.child("Android").child("count")
                                .child(student.child("REG_NO").getValue().toString()).child("present").getValue().toString())
                                +
                                Integer.parseInt(attendance.child("Android").child("count")
                                        .child(student.child("REG_NO").getValue().toString()).child("absent").getValue().toString());
                        Andt.setText(Integer.toString(andt));
                        and = Integer.parseInt(attendance.child("Android").child("count")
                                .child(student.child("REG_NO").getValue().toString()).child("present").getValue().toString())
                                * 100 / (float) andt;
                        Andp.setText(Float.toString(and)+"%");

                        if (attendance.child("iot").child("count")
                                .child(student.child("REG_NO").getValue().toString()).exists()) {
                            elective.setText("IOT");
                            Elec.setText(attendance.child("iot").child("count")
                                    .child(student.child("REG_NO").getValue().toString()).child("present").getValue().toString());

                            elect = Integer.parseInt(attendance.child("iot").child("count")
                                    .child(student.child("REG_NO").getValue().toString()).child("present").getValue().toString())
                                    +
                                    Integer.parseInt(attendance.child("iot").child("count")
                                            .child(student.child("REG_NO").getValue().toString()).child("absent").getValue().toString());
                            Elect.setText(Integer.toString(elect));
                            elec = Integer.parseInt(attendance.child("iot").child("count")
                                    .child(student.child("REG_NO").getValue().toString()).child("present").getValue().toString())
                                    * 100 / (float) elect;
                            Elecp.setText(Float.toString(elec)+"%");

                            total = total + Integer.parseInt(attendance.child("iot").child("count")
                                    .child(student.child("REG_NO").getValue().toString()).child("present").getValue().toString());

                        } else {
                            elective.setText("DS");
                            Elec.setText(attendance.child("DS").child("count")
                                    .child(student.child("REG_NO").getValue().toString()).child("present").getValue().toString());
                            elect = Integer.parseInt(attendance.child("DS").child("count")
                                    .child(student.child("REG_NO").getValue().toString()).child("present").getValue().toString())
                                    +
                                    Integer.parseInt(attendance.child("DS").child("count")
                                            .child(student.child("REG_NO").getValue().toString()).child("absent").getValue().toString());
                            Elect.setText(Integer.toString(elect));

                            elec = Integer.parseInt(attendance.child("DS").child("count")
                                    .child(student.child("REG_NO").getValue().toString()).child("present").getValue().toString())
                                    * 100 / (float) elect;
                            Elecp.setText(Float.toString(elec)+"%");

                            total = total + Integer.parseInt(attendance.child("DS").child("count")
                                    .child(student.child("REG_NO").getValue().toString()).child("present").getValue().toString());
                        }
                        tot.setText(Integer.toString(total));
                        totp.setText(Float.toString((float)total*100/(andt+dwdmt+elect))+"%");
                        tott.setText(Integer.toString(andt+dwdmt+elect));
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

    }}