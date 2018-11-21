
package com.example.usuario.pdmclientgroup4;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

import exceptions.EmailNotUniqueException;
import exceptions.LoginExistingException;
import exceptions.LoginNotExistingException;
import exceptions.WrongPasswordException;
import logic.Logic;
import message.Privilege;
import message.Status;
import message.User;


/**
 * Clase que define los manejadores de eventos de la interfaz
 * @author Gorka
 */
public class SignupActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private Button btnSignUp;
    private Button btnCancel;
    private EditText txtFName;
    private EditText txtFEmail;
    private EditText txtFUser;
    private EditText txtFPassword;
    private EditText txtFRpPassword;
    private boolean error=false;
    private String errorContext="";

    private Logic logic;

    private static final Logger logger = Logger.getLogger("SignUpController");
    /**
     * Inilization of the window
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_signup);

        btnSignUp = findViewById(R.id.buttonSignUp);
        btnSignUp.setOnClickListener(this);

        btnCancel = findViewById(R.id.buttonCancel);
        btnCancel.setOnClickListener(this);

        txtFName = findViewById(R.id.editTextFullName);
        txtFEmail = findViewById(R.id.editTextEmail);
        txtFUser = findViewById(R.id.editTextUser);
        txtFPassword = findViewById(R.id.editTextPassword);
        txtFRpPassword = findViewById(R.id.editTextRpPassword);

        txtFName.addTextChangedListener(this);
        txtFEmail.addTextChangedListener(this);
        txtFUser.addTextChangedListener(this);
        txtFPassword.addTextChangedListener(this);
        txtFRpPassword.addTextChangedListener(this);
    }

    /**
     * Method on click sign up or cancel button
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(btnSignUp.isPressed()){
            pushSignUp();
        }
        if(btnCancel.isPressed()){
            finish();
        }
    }

    /**
     * Text changed of the fields Full name, Email, Username, Password y Repeat Password
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(!txtFName.getText().toString().isEmpty() && !txtFEmail.getText().toString().isEmpty()
                && !txtFUser.getText().toString().isEmpty() && !txtFPassword.getText().toString().isEmpty()
                && !txtFRpPassword.getText().toString().isEmpty()){
            btnSignUp.setEnabled(true);
        }else {
            btnSignUp.setEnabled(false);
        }

        if(txtFUser.length() == 17){
            txtFUser.setText(txtFUser.getText().toString().substring(0,16));
            txtFUser.clearFocus();
            ocultarTeclado(txtFUser);
            Toast.makeText(getApplicationContext(),"Write user with maximum of 16 characters",Toast.LENGTH_LONG).show();
        }

        if(txtFPassword.length() == 17){
            txtFPassword.setText(txtFPassword.getText().toString().substring(0,16));
            txtFPassword.clearFocus();
            ocultarTeclado(txtFPassword);
            Toast.makeText(getApplicationContext(),"Write password with maximum of 16 characters",Toast.LENGTH_LONG).show();
        }

        if(txtFRpPassword.length() == 17){
            txtFRpPassword.setText(txtFRpPassword.getText().toString().substring(0,16));
            txtFRpPassword.clearFocus();
            ocultarTeclado(txtFRpPassword);
            Toast.makeText(getApplicationContext(),"Write password with maximum of 16 characters",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void afterTextChanged(Editable s) {
    }

    private void pushSignUp(){
        if(verifyEmail() && verifyUser() && verifyPassword()){
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            User user = new User(Status.enabled, Privilege.user);
            user.setFullName(txtFName.getText().toString());
            user.setLogin(txtFUser.getText().toString());
            user.setEmail(txtFEmail.getText().toString());
            user.setPassword(txtFPassword.getText().toString());
            user.setLastAcces(timestamp);
            user.setLastPasswordChange(timestamp);
            try {
                ThreadClient tc = new ThreadClient(user, 2, logic);
                tc.setUncaughtExceptionHandler(this::uncaughtException);
                tc.start();
                tc.join();
                if(!error){
                    finish();
                }else{
                    Toast toast = Toast.makeText(this, errorContext, Toast.LENGTH_SHORT);
                    toast.show();
                }
                error=false;
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "ErrorPushSignUp", ex);
            }
        }
    }

    /**
     *
     * @param thread
     * @param t
     */
    public void uncaughtException(Thread thread, Throwable t) {
        error=true;
        if(t.getCause() instanceof LoginExistingException){
            txtFUser.setTextColor(Color.parseColor("#ff0000"));
            txtFPassword.setTextColor(Color.parseColor("#237bf7"));
            txtFEmail.setTextColor(Color.parseColor("#237bf7"));
            txtFName.setTextColor(Color.parseColor("#237bf7"));
            txtFPassword.setTextColor(Color.parseColor("#237bf7"));
            txtFRpPassword.setTextColor(Color.parseColor("#237bf7"));
            errorContext="Login doesn't exist";
        }else if(t.getCause() instanceof EmailNotUniqueException){
            txtFUser.setTextColor(Color.parseColor("#237bf7"));
            txtFPassword.setTextColor(Color.parseColor("#237bf7"));
            txtFEmail.setTextColor(Color.parseColor("#ff0000"));
            txtFName.setTextColor(Color.parseColor("#237bf7"));
            txtFPassword.setTextColor(Color.parseColor("#237bf7"));
            txtFRpPassword.setTextColor(Color.parseColor("#237bf7"));
            errorContext="This email is in use.";
        } else if (t.getCause() instanceof Exception) {
            txtFUser.setTextColor(Color.parseColor("#237bf7"));
            txtFPassword.setTextColor(Color.parseColor("#237bf7"));
            txtFEmail.setTextColor(Color.parseColor("#237bf7"));
            txtFName.setTextColor(Color.parseColor("#237bf7"));
            txtFPassword.setTextColor(Color.parseColor("#237bf7"));
            txtFRpPassword.setTextColor(Color.parseColor("#237bf7"));
            errorContext="An error have occurred.";
        }
    }

    /**
 * Method to validate email
 * @return boolean
 */
private boolean verifyEmail() {
    boolean a = false;
    boolean b = false;
    for(int i = 0; i < txtFEmail.length(); i++){
        if(!a && txtFEmail.getText().toString().substring(i, i+1).equalsIgnoreCase("@")){
            a = true;
        }
        if(a && txtFEmail.getText().toString().substring(i, i+1).equalsIgnoreCase(".")){
            b = true;
        }
    }
    if(b){
        //lblEmail.setTextColor(Color.parseColor("#237bf7"));
        return true;
    }else {
        txtFEmail.getText().clear();
        //lblEmail.setTextColor(Color.parseColor("#FF0000"));
        Toast.makeText(getApplicationContext(),"Email invalid",Toast.LENGTH_LONG).show();
        return false;
    }
}

    /**
     * Method to validate user
     * @return boolean
     */
    private boolean verifyUser() {
        if(txtFUser.length() >= 4){
            //lblUser.setTextColor(Color.parseColor("#237bf7"));
            return true;
        }else {
            txtFUser.getText().clear();
            //lblUser.setTextColor(Color.parseColor("#FF0000"));
            Toast.makeText(getApplicationContext(),"Username invalid",Toast.LENGTH_LONG).show();
            return false;
        }
    }

    /**
     * Method to validate password
     * @return boolean
     */
    private boolean verifyPassword() {
        if(txtFPassword.getText().toString().equalsIgnoreCase(txtFRpPassword.getText().toString())
                && txtFPassword.length() >=4){
            //lblPassword.setTextColor(Color.parseColor("#237bf7"));
            //lblRpPassword.setTextColor(Color.parseColor("#237bf7"));
            return true;
        }else {
            txtFPassword.getText().clear();
            txtFRpPassword.getText().clear();
            //lblPassword.setTextColor(Color.parseColor("#FF0000"));
            //lblRpPassword.setTextColor(Color.parseColor("#FF0000"));
            Toast.makeText(getApplicationContext(),"Password invalid",Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void ocultarTeclado(EditText editText){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }
}
