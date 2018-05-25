package com.example.chiunguo.myapplication.login;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.chiunguo.myapplication.R;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private final  String TAG="Login";
    private TextInputLayout username;
    private TextInputLayout password;
    private Button login_button;

   // private Toolbar toolbar;

    LoginContract.Presenter presenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
      //  toolbar=findViewById(R.id.logintoolbar);

        login_button=findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setErrorEnabled(false);
                password.setErrorEnabled(false);
                presenter.login();
            }
        });


        //new LoginPresenter for  it contructor and setPresenter for view also
         this.presenter=new LoginPresenter(this);


    }

    @Override
    public void showTitle(String title) {
    //toolbar.setTitle("Login");


    }

    //toast
    @Override
    public void showDialog(String message) {
        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    //textinputlayout show error
    @Override
    public void showError(TextInputLayout textInputLayout, String error) {
        textInputLayout.setError(error);
        textInputLayout.getEditText().setFocusable(true);
        textInputLayout.getEditText().setFocusableInTouchMode(true);
        textInputLayout.getEditText().requestFocus();
    }

    @Override
    public String getUsername() {
        return  username.getEditText().getText().toString();
    }

    @Override
    public String getPassword() {
        return password.getEditText().getText().toString();
    }

    @Override
    public TextInputLayout getUsernameTextInput() {
        return username;
    }

    @Override
    public TextInputLayout getPasswordTextInput() {
        return password;
    }


    //set presenter to this  class
    //and use  setPresneter in  this constructor
    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
      this.presenter=presenter;
    }
}
