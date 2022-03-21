package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button Studentbtn;
    private Button Teacherbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Studentbtn = (Button) findViewById(R.id.student);
        Teacherbtn = (Button) findViewById(R.id.teacher);

        Studentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStudentLogin();

            }
        });
        Teacherbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTeacherLogin();

            }
        });

    }

    public void openStudentLogin() {
        Intent intent = new Intent(this, StudentLogin.class);
        startActivity(intent);
    }

    public void openTeacherLogin() {
        Intent intent = new Intent(this, TeacherLogin.class);
        startActivity(intent);
    }
}