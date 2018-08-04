package com.example.sanje.i_weather;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener{
    //Database object declaration
    DatabaseHelper helper=new DatabaseHelper(this);
    CallbackManager callbackManager;
    GoogleApiClient googleApiClient;
    private static final int REQ_CODE=9001;//api provided by facebook
    //Login Top Views
    LinearLayout linearLayoutLogIn;
    EditText etLoginuserName,etLoginPassword;
    Button btnLoginSubmit;

    //signUp Views
    LinearLayout linearLayoutSignUp;
    EditText etSignUpFullname,etSignUpEmailid,etSignUpPassword,etSignUpCheckPassword;
    CheckBox checkBoxAgreeTermsAndCondition;
    Button btnSignUpSubmit;

    //Login Below Views
    LinearLayout linearLayoutLoginPageSignUp;
    SignInButton btnSignInGoogle;
    LoginButton btnloginFacebook;
    Button btnSignupInLoginPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.actvity_login_main);
        GoogleSignInOptions signInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

        initViews();
        //setting callback manager of facebook on click

        callbackManager=CallbackManager.Factory.create();
        btnloginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                 Intent intent=new Intent(LogInActivity.this,MainActivity.class);
                 startActivity(intent);
            }

            @Override
            public void onCancel() {
                Toast.makeText(LogInActivity.this,"Hello LogIn Cancelled",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LogInActivity.this,"Error Occured",Toast.LENGTH_LONG).show();
            }
        });

    }
    public void initViews(){
        //Login Top Views
        linearLayoutLogIn=(LinearLayout)findViewById(R.id.linearlayoutLogin);
        etLoginuserName=(EditText)findViewById(R.id.editTextLoginUsername);
        etLoginPassword=(EditText)findViewById(R.id.editTextLoginPassword);
        btnLoginSubmit=(Button)findViewById(R.id.buttonLogin);

        //signUp Views
        linearLayoutSignUp=(LinearLayout)findViewById(R.id.linearlayoutSignup);
        etSignUpFullname=(EditText)findViewById(R.id.editTextSignupFullName);
        etSignUpEmailid=(EditText)findViewById(R.id.editTextSignupEmail);
        etSignUpPassword=(EditText)findViewById(R.id.editTextSignupPassword);
        etSignUpCheckPassword=(EditText)findViewById(R.id.editTextSignupPasswordReenter);
        checkBoxAgreeTermsAndCondition=(CheckBox) findViewById(R.id.checkboxSignup);
        btnSignUpSubmit=(Button)findViewById(R.id.buttonSignup);

        //Login Below Views
        linearLayoutLoginPageSignUp=(LinearLayout)findViewById(R.id.linearlayoutLoginBelow);
        btnSignInGoogle=(SignInButton)findViewById(R.id.buttonLoginGoogle);
        btnloginFacebook=(LoginButton)findViewById(R.id.buttonLoginFacebook);
        btnSignupInLoginPage=(Button)findViewById(R.id.buttonSignupInLoginPage);

        //setting the button click listener
        btnSignupInLoginPage.setOnClickListener(LogInActivity.this);
        btnLoginSubmit.setOnClickListener(LogInActivity.this);
        btnSignUpSubmit.setOnClickListener(LogInActivity.this);
        btnSignInGoogle.setOnClickListener(LogInActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ_CODE){
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Intent intent=new Intent(LogInActivity.this,MainActivity.class);
            startActivity(intent);

        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonSignupInLoginPage:
                linearLayoutLogIn.setVisibility(View.GONE);
                linearLayoutSignUp.setVisibility(View.VISIBLE);
                linearLayoutLoginPageSignUp.setVisibility(View.GONE);
                break;
            case R.id.buttonLogin:
                loginDatabase();
                break;
            case R.id.buttonSignup:
                signUpDatabase();
                break;
            case R.id.buttonLoginGoogle:
                signIn();
                break;
        }
    }
    private void signIn(){
        Intent intent= Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);//override method onActivityResult()
    }
    private void signUpDatabase(){
        String name=etSignUpFullname.getText().toString();
        String email=etSignUpEmailid.getText().toString();
        String password1=etSignUpPassword.getText().toString();
        String password2=etSignUpCheckPassword.getText().toString();
        boolean checkBox=checkBoxAgreeTermsAndCondition.isChecked();
        if(!name.equals("") && !email.equals("") && !password1.equals("") && !password2.equals("") && password1.equals(password2) && checkBox){
            Toast.makeText(LogInActivity.this,"Signin in Successfull",Toast.LENGTH_LONG).show();

            //Here we will insert the details in the dataBase
            Contacts contacts=new Contacts();
            contacts.setName(name);
            contacts.setEmail(email);
            contacts.setPassword(password1);
            helper.insertContact(contacts);
        }else{
            if(name.equals(""))
                     etSignUpFullname.setError("Field is Empty");
            if(email.equals(""))
                etSignUpEmailid.setError("Field is Empty");
            if(password1.equals(""))
                etSignUpCheckPassword.setError("Field is Empty");
            if(password2.equals(""))
                etSignUpPassword.setError("Field is Empty");
            if(!checkBox)
                checkBoxAgreeTermsAndCondition.setError("Field should be checked");
            if(!password1.equals(password2))
                Toast.makeText(LogInActivity.this,"Password didn't match",Toast.LENGTH_LONG).show();
        }
    }
    void loginDatabase(){
        String name=etLoginuserName.getText().toString();
        String password=etLoginPassword.getText().toString();
        if(!name.equals("") && !password.equals("")){
            Toast.makeText(LogInActivity.this,"Login Successfull",Toast.LENGTH_LONG).show();

            //TODO:Here we will insert the details in the dataBase
              String passwordResponse=helper.searchPassword(name);
              if(password.equals(passwordResponse)){
                  //TODO:go to main page of the database
                  Intent intent=new Intent(LogInActivity.this,MainActivity.class);
                  startActivity(intent);
              }else {
                  Toast.makeText(LogInActivity.this,"Username and password dont match",Toast.LENGTH_LONG).show();
              }



        }else{
            if(name.equals(""))
                etSignUpFullname.setError("Field is Empty");
            if(password.equals(""))
                etSignUpEmailid.setError("Field is Empty");

        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
