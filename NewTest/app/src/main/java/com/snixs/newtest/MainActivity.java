package com.snixs.newtest;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText mobile;
    Button button;
    String no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mobile = findViewById(R.id.mobile);

        button = findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                no = mobile.getText().toString();
                validNo(no);
                Intent intent = new Intent(MainActivity.this,VerifyMobile.class);
                intent.putExtra("mobile",no);
                startActivity(intent);
                Toast.makeText(MainActivity.this,no,Toast.LENGTH_LONG).show();
            }
        });


    }

    private void validNo(String no){
        if(no.isEmpty() || no.length() < 10){
            mobile.setError("Enter a valid mobile");
            mobile.requestFocus();
            return;
        }
    }
}