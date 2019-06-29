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

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import es.dmoral.toasty.Toasty;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtUsername, edtPassword;
    private Button btnSignUp, btnLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //checking if there is any logged in user
        if (ParseUser.getCurrentUser() != null) {
            whatsappCloneActivity();
        }

        // Setting app title
        setTitle("Log In");

        // Initializing the variables
        edtUsername = findViewById(R.id.edtUsernameLA);
        edtPassword = findViewById(R.id.edtPasswordLA);
        btnSignUp = findViewById(R.id.btnSignUpLA);
        btnLogIn = findViewById(R.id.btnLogInLA);

        // setting the onKeyListener
        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    onClick(btnLogIn);
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
            case R.id.btnSignUpLA:
                finish();
                break;
            case R.id.btnLogInLA:

                if (edtUsername.getText().toString().equals("") || edtPassword.getText().toString().equals("")) {
                    Toasty.error(LogInActivity.this, "Please enter Username/Password", Toasty.LENGTH_LONG, true).show();
                } else {
                    // Progress dialog
                    final ProgressDialog dialog = new ProgressDialog(this);
                    dialog.setMessage("Logging in " + edtUsername.getText().toString());
                    dialog.show();

                    // Logging in the user
                    ParseUser.logInInBackground(edtUsername.getText().toString(), edtPassword.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (user != null && e == null) {
                                Toasty.success(LogInActivity.this, "Welcome " + ParseUser.getCurrentUser().getUsername(), Toasty.LENGTH_SHORT, true).show();
                                whatsappCloneActivity();
                            } else {
                                Toasty.error(LogInActivity.this, e.getMessage(), Toasty.LENGTH_LONG, true).show();
                            }
                            dialog.dismiss();
                        }
                    });
                    break;
                }
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
        Intent intent = new Intent(LogInActivity.this,WhatsAppClone.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
