package com.example.chiunguo.myapplication.login;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.Layout;

import com.example.chiunguo.myapplication.core.BasePresenter;
import com.example.chiunguo.myapplication.core.BaseView;

public interface LoginContract {

    interface View extends BaseView<Presenter> {

        void showTitle( String title);

        void showDialog(String message);

        void showError( TextInputLayout textInputLayout, String error);

        String getUsername();

        String getPassword();

        TextInputLayout getUsernameTextInput();

        TextInputLayout getPasswordTextInput();

    }

    interface Presenter extends BasePresenter {

        void login(String usernmae,String password);

    }
}
