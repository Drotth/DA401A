package com.drotth.grumpychat;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class StartActivity extends Activity {

    FragmentManager fragmentManager;
    LoginFragment loginPage;
    RegisterFragment registerPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        loginPage = new LoginFragment();
        fragmentTransaction.add(R.id.fragmentViewStart, loginPage);
        fragmentTransaction.commit();
    }

    public void login(View view){
        EditText emailInsert = (EditText) findViewById(R.id.emailFieldLogin);
        String email = emailInsert.getText().toString();

        EditText passInsert = (EditText) findViewById(R.id.passwordFieldLogin);
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

    public void register(View view){
        int id = view.getId();

        if (id == R.id.regBtn1) {
            if (registerPage == null || !registerPage.isAdded()){
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                registerPage = new RegisterFragment();
                fragmentTransaction.replace(R.id.fragmentViewStart, registerPage);
                //TODO: This stack state is kept when entering the "if-else" below. Fix!
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();
            }
        }
        else if (id == R.id.regBtn2){
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentViewStart, loginPage);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();
        }
    }
}
