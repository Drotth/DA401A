package com.drotth.grumpychat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view){
        EditText emailInsert = (EditText) findViewById(R.id.emailField);
        String email = emailInsert.getText().toString();

        EditText passInsert = (EditText) findViewById(R.id.passwordField);
        String password = passInsert.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Enter both email and password", Toast.LENGTH_SHORT).show();
        }
        else{
            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.putExtra("User", email);
            mainIntent.putExtra("Password", password);
            this.startActivity(mainIntent);
            this.finish();
        }
    }
}
