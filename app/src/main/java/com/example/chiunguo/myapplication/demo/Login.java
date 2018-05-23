package com.example.chiunguo.myapplication.demo;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.chiunguo.myapplication.R;
import com.example.chiunguo.myapplication.util.StringUtils;


public class Login extends AppCompatActivity {

    private final  String TAG="Login";
    private TextInputLayout username;
    private TextInputLayout password;
    private Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        login_button=findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputusername = username.getEditText().getText().toString();
                String inputpassword = password.getEditText().getText().toString();

                username.setErrorEnabled(false);
                password.setErrorEnabled(false);

                //验证用户名和密码
                if(validateAccount(inputusername)&&validatePassword(inputpassword)&&sendLogin(inputusername,inputpassword)){
                   toast("登录成功");
                }
                else{
                    toast("登录失败");
                }
            }
        });


    }


    /**
     * 显示错误提示，并获取焦点
     *
     * @param textInputLayout
     * @param error
     */
    private void showError(TextInputLayout textInputLayout, String error) {
        textInputLayout.setError(error);
        textInputLayout.getEditText().setFocusable(true);
        textInputLayout.getEditText().setFocusableInTouchMode(true);
        textInputLayout.getEditText().requestFocus();
    }

    /**
     * 验证用户名
     *
     * @param account
     * @return
     */
    private boolean validateAccount(String account) {
        if (null==account||account.isEmpty()) {
            showError(username, "用户名不能为空");
            return false;
        }
        if(StringUtils.validate(account)==false){
            showError(username,"用户格式不符");
            return  false;
        }
        return true;
    }

    /**
     * 验证密码
     *
     * @param inputpassword
     * @return
     */
    private boolean validatePassword(String inputpassword) {
        if (null==inputpassword||inputpassword.isEmpty()) {
            showError(password,"密码不能为空");
            return false;
        }

        if (inputpassword.length() < 6 || inputpassword.length() > 18) {
            showError(password, "密码长度为6-18位");
            return false;
        }

        if(inputpassword.equals("6666666")!=true){
           // Log.i(TAG,"input:"+inputpassword+"判断"+(inputpassword.equals("6666666"))+"类型");

            showError(password,"密码不正确");
            return false;
        }
        return true;
    }
    public void toast(String m){
        Context context = getApplicationContext();
        CharSequence text = m;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public boolean sendLogin(String inputusername,String inputpassword){
//        if(inputpassword!="666666"){
//            return false;
//        }
        return true;

    }


}


