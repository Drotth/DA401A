package com.drotth.grumpychat;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class StartActivity extends Activity {

    private Firebase firebase;
    private FragmentManager fragmentManager;
    private LoginFragment loginPage;
    private RegisterFragment registerPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Firebase.setAndroidContext(this);
        firebase = new Firebase((String)getResources().getText(R.string.firebase_url));
        fragmentManager = getFragmentManager();

        loginPage = new LoginFragment();
        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out,
                        android.R.animator.fade_in, android.R.animator.fade_out)
                .add(R.id.fragmentViewStart, loginPage)
                .commit();
    }

    public void onClick(View view){
        EditText emailInsert, passInsert;
        String email, password;

        switch (view.getId()){
            case R.id.loginBtn:
                emailInsert = (EditText) findViewById(R.id.emailFieldLogin);
                email = emailInsert.getText().toString();

                passInsert = (EditText) findViewById(R.id.passwordFieldLogin);
                password = passInsert.getText().toString();

                login(email, password);
                break;

            case R.id.regBtn1:
                if (registerPage == null || !registerPage.isAdded()){
                    registerPage = new RegisterFragment();
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out,
                                    android.R.animator.fade_in, android.R.animator.fade_out)
                            .replace(R.id.fragmentViewStart, registerPage)
                            .addToBackStack(null)
                            // TODO: for VG, is it .setTransition or .setCustomAnimations?
                            //.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit();
                }
                break;

            case R.id.regBtn2:
                emailInsert = (EditText) findViewById(R.id.emailFieldReg);
                email = emailInsert.getText().toString();

                passInsert = (EditText) findViewById(R.id.passwordFieldReg);
                password = passInsert.getText().toString();

                register(email, password);
                break;
        }
    }

    private void login(String email, String password){
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.enter_both, Toast.LENGTH_SHORT).show();
        }

        else{
            findViewById(R.id.loginBtn).setVisibility(View.GONE);
            findViewById(R.id.regBtn1).setVisibility(View.GONE);
            findViewById(R.id.loadingPanelLogin).setVisibility(View.VISIBLE);

            firebase.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    Intent mainIntent = new Intent(StartActivity.this, MainActivity.class);
                    StartActivity.this.startActivity(mainIntent);
                    StartActivity.this.finish();
                }
                @Override
                public void onAuthenticationError(FirebaseError error) {
                    findViewById(R.id.loadingPanelLogin).setVisibility(View.GONE);
                    findViewById(R.id.loginBtn).setVisibility(View.VISIBLE);
                    findViewById(R.id.regBtn1).setVisibility(View.VISIBLE);

                    switch (error.getCode()) {
                        case FirebaseError.INVALID_PASSWORD:
                            Toast.makeText(getApplicationContext(), R.string.invalid_pass, Toast.LENGTH_SHORT).show();
                            break;

                        default:
                            Toast.makeText(getApplicationContext(), R.string.login_unsuccessful, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        }
    }

    public void register(final String email, final String password){
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.enter_both, Toast.LENGTH_SHORT).show();
        }

        else {
            findViewById(R.id.regBtn2).setVisibility(View.GONE);
            findViewById(R.id.loadingPanelReg).setVisibility(View.VISIBLE);

            firebase.createUser(email, password, new Firebase.ResultHandler() {
                @Override
                public void onSuccess() {
                    StartActivity.this.login(email, password);
                }

                @Override
                public void onError(FirebaseError error) {
                    findViewById(R.id.loadingPanelReg).setVisibility(View.GONE);
                    findViewById(R.id.regBtn2).setVisibility(View.VISIBLE);

                    switch (error.getCode()) {
                        case FirebaseError.EMAIL_TAKEN:
                            Toast.makeText(getApplicationContext(), R.string.email_taken, Toast.LENGTH_SHORT).show();
                            break;

                        default:
                            Toast.makeText(getApplicationContext(), R.string.reg_unsuccessful, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        }
    }
}
