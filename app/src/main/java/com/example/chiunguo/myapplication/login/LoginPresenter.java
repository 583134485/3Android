package com.example.chiunguo.myapplication.login;

import android.support.design.widget.TextInputLayout;

import com.example.chiunguo.myapplication.util.StringUtils;

public class LoginPresenter implements LoginContract.Presenter {


    public  final LoginContract.View loginview;

    private TextInputLayout username;

    private  TextInputLayout password;

    //get loginview implemnet to presenter
    public LoginPresenter(LoginContract.View loginview){
        //creat contact to view
        this.loginview=loginview;
        //set Presenter for view
        this.loginview.setPresenter(this);
        //bind view like textinputlayout to this.presenter
        if(loginview!=null){
            username=this.loginview.getUsernameTextInput();
            password=this.loginview.getPasswordTextInput();
        }
    }

    @Override
    public void login(String usernmae, String password) {
        //验证用户名和密码
        if(validateAccount(usernmae)&&validatePassword(password)){
            loginview.showDialog("login success");
        }
        else{
            loginview.showDialog("login failed");
        }
    }

    @Override
    public void start() {

    }

    /**
     * 验证用户名
     *
     * @param account
     * @return
     */
    private boolean validateAccount(String account) {
        if (null==account||account.isEmpty()) {
            loginview.showError(username, "用户名不能为空");
            return false;
        }
        if(StringUtils.validate(account)==false){
            loginview.showError(username,"用户格式不符");
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
            loginview.showError(password,"密码不能为空");
            return false;
        }

        if (inputpassword.length() < 6 || inputpassword.length() > 18) {
           loginview.showError(password, "密码长度为6-18位");
            return false;
        }

        if(inputpassword.equals("6666666")!=true){
            // Log.i(TAG,"input:"+inputpassword+"判断"+(inputpassword.equals("6666666"))+"类型");

            loginview.showError(password,"密码不正确");
            return false;
        }
        return true;
    }


}
