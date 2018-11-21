package com.example.usuario.pdmclientgroup4;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LogOut extends AppCompatActivity implements Button.OnClickListener{

    Button button;
    TextView txtV;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_out);

        button=findViewById(R.id.button);
        button.setOnClickListener(this);

        txtV=findViewById(R.id.message);
        txtV.setText("Welcome, " + getIntent().getStringExtra("username"));
    }
    @Override
    public void onClick(View v) {
        finish();
    }
}
