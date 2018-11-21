package com.example.usuario.pdmclientgroup4;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.logging.Logger;
import logic.Logic;
import logic.LogicFactory;
import exceptions.LoginNotExistingException;
import exceptions.WrongPasswordException;
import message.User;

/**
 * @version 1.0
 * @author Lander Lluvia
 */

public class LoginActivity extends AppCompatActivity implements Button.OnClickListener, Thread.UncaughtExceptionHandler {

    private TextView txtUser;
    private TextView txtPassword;
    private EditText editTxtUser;
    private EditText editTxtPassword;
    private Button btnLogIn;
    private TextView txtRegister;
    private ProgressBar progressBar;
    private Logic logic;
    private boolean error=false;
    private String errorContext="";

    private static final Logger logger = Logger.getLogger("LogInController");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_login);

        txtUser = findViewById(R.id.txtUser);
        txtPassword = findViewById(R.id.txtPassword);
        editTxtUser = findViewById(R.id.txtUser);
        editTxtPassword = findViewById(R.id.txtPassword);
        btnLogIn = findViewById(R.id.btnLogIn);
        txtRegister = findViewById(R.id.txtRegister);
        progressBar = findViewById(R.id.progressBar);

        btnLogIn.setOnClickListener(this);
        txtRegister.setOnClickListener(this);

        editTxtUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)  {
                handleTextChanged();
            }
        });

            editTxtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                handleTextChanged();
            }
        });
    }

    /**
     * Method that calls the method logIn when the btnLogIn is clicked
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(btnLogIn.isPressed()){
            progressBar.setVisibility(View.VISIBLE);
            logIn();
        }else {
            signUp();
        }

    }

    public void handleTextChanged() {
        if(editTxtUser.getText().toString().trim().isEmpty() ||
                editTxtPassword.getText().toString().trim().isEmpty()){
            btnLogIn.setEnabled(false);
        }else {
            btnLogIn.setEnabled(true);
        }
    }


    /**
     * Method that creates a new User with the data in the User and Password fields and send it.
     * Catch wrong login/password, shows an alert and changes the color of the wrong field.
     */
    private void logIn() {
        User us = new User(editTxtUser.getText().toString(), editTxtPassword.getText().toString());
        logic = LogicFactory.getLogic();
        try {
            ThreadClient tc = new ThreadClient(us, 1, logic);
            tc.setUncaughtExceptionHandler(this::uncaughtException);
            tc.start();
            tc.join();
            if(!error){
                us = tc.getUser();
                logOut(us);
            }else{
                Toast toast = Toast.makeText(this, errorContext, Toast.LENGTH_SHORT);
                toast.show();
            }
            error=false;
        }catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    /**
     *
     * @param thread
     * @param t
     */
    @Override
    public void uncaughtException(Thread thread, Throwable t) {
        error=true;
        if(t.getCause() instanceof LoginNotExistingException){
            txtUser.setTextColor(Color.parseColor("#ff0000"));
            txtPassword.setTextColor(Color.parseColor("#237bf7"));
            errorContext="Login doesn't exist";
        }else if(t.getCause() instanceof WrongPasswordException){
            txtUser.setTextColor(Color.parseColor("#237bf7"));
            txtPassword.setTextColor(Color.parseColor("#ff0000"));
            errorContext="Wrong password";
        } else if (t.getCause() instanceof Exception) {
            errorContext="An error have occurred.";
        }
    }


    /**
     * Method that shows the logOut window
     * @param user
     */
    private void logOut(User user){
        Intent intent = new Intent(this, LogOut.class);
        intent.putExtra("username", user.getFullName());
        //setResult(RESULT_OK, intent);
        startActivity(intent);
        finish();
    }

    private void signUp() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}
