package com.drotth.grumpychat;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class StartActivity extends Activity {

    //remember to change this to da401a before demo
    private static final String FIREBASE_URL = "https://testda401a.firebaseio.com";
    private Firebase firebase;
    private FragmentManager fragmentManager;
    private LoginFragment loginPage;
    private RegisterFragment registerPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Firebase.setAndroidContext(this);
        firebase = new Firebase(FIREBASE_URL);

        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        loginPage = new LoginFragment();
        fragmentTransaction.add(R.id.fragmentViewStart, loginPage);
        fragmentTransaction.commit();
    }

    public void loginClick(View view){
        EditText emailInsert = (EditText) findViewById(R.id.emailFieldLogin);
        String email = emailInsert.getText().toString();

        EditText passInsert = (EditText) findViewById(R.id.passwordFieldLogin);
        String password = passInsert.getText().toString();

        login(email, password);
    }

    public void registerClick(View view){
        int id = view.getId();

        if(id == R.id.regBtn1){
            if (registerPage == null || !registerPage.isAdded()){
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                registerPage = new RegisterFragment();
                fragmentTransaction.replace(R.id.fragmentViewStart, registerPage);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();
            }
        }
        else if (id == R.id.regBtn2){
            EditText emailInsert = (EditText) findViewById(R.id.emailFieldReg);
            String email = emailInsert.getText().toString();

            EditText passInsert = (EditText) findViewById(R.id.passwordFieldReg);
            String password = passInsert.getText().toString();

            register(email, password);
        }
    }

    private void login(String email, String password){
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter both email and password", Toast.LENGTH_SHORT).show();
        }

        else{
            firebase.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    Intent mainIntent = new Intent(StartActivity.this, MainActivity.class);
                    StartActivity.this.startActivity(mainIntent);
                    StartActivity.this.finish();
                }
                @Override
                public void onAuthenticationError(FirebaseError error) {
                    Toast.makeText(getApplicationContext(), "Login unsuccessful", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void register(final String email, final String password){
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter both email and password", Toast.LENGTH_SHORT).show();
        }

        else {
            firebase.createUser(email, password, new Firebase.ResultHandler() {
                @Override
                public void onSuccess() {
                    StartActivity.this.login(email, password);
                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    Toast.makeText(getApplicationContext(),"Registration unsuccessful", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
