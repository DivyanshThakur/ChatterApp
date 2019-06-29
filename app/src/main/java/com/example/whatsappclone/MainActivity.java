package com.example.whatsappclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail, edtUsername, edtPassword;
    private Button btnSignUp, btnLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Save the current Installation to Back4App
        ParseInstallation.getCurrentInstallation().saveInBackground();

        //checking if there is any logged in user
        if (ParseUser.getCurrentUser() != null) {
            whatsappCloneActivity();
        }

        // Setting app title
        setTitle("WhatsApp Clone");

        // Initializing the variables
        edtEmail = findViewById(R.id.edtEmailSA);
        edtUsername = findViewById(R.id.edtUsernameSA);
        edtPassword = findViewById(R.id.edtPasswordSA);
        btnSignUp = findViewById(R.id.btnSignUpSA);
        btnLogIn = findViewById(R.id.btnLogInSA);

        // setting the onKeyListener
        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    onClick(btnSignUp);
                }
                return false;
            }
        });

        // setting the onClickListener
        btnSignUp.setOnClickListener(this);
        btnLogIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignUpSA :

                // Adding user to parse server
                final ParseUser appUser = new ParseUser();
                appUser.setEmail(edtEmail.getText().toString());
                appUser.setUsername(edtUsername.getText().toString());
                appUser.setPassword(edtPassword.getText().toString());

                // checking for null input
                if (edtEmail.getText().toString().equals("") || edtUsername.getText().toString().equals("") || edtPassword.getText().toString().equals("")) {
                    Toasty.error(MainActivity.this, "Please fill complete details",Toasty.LENGTH_LONG,true).show();
                } else {
                    // adding progress dialog
                    final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                    dialog.setMessage("Signing up " + edtUsername.getText().toString());
                    dialog.show();

                    // saving user to parse in background
                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toasty.success(MainActivity.this,appUser.getUsername() + " is signed up!",Toasty.LENGTH_SHORT,true).show();
                                whatsappCloneActivity();
                            } else {
                                Toasty.error(MainActivity.this,e.getMessage(),Toasty.LENGTH_LONG,true).show();
                            }
                            dialog.dismiss();
                        }
                    });
                }
                break;
            case R.id.btnLogInSA :
                Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void rootLayoutTapped(View view) {
        try {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void whatsappCloneActivity() {
        Intent intent = new Intent(MainActivity.this,WhatsAppClone.class);
        startActivity(intent);
        finish();
    }
}
