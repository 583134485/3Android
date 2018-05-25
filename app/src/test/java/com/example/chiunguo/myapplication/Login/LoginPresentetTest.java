package com.example.chiunguo.myapplication.Login;

import android.util.Log;
import android.widget.Toast;

import com.example.chiunguo.myapplication.BuildConfig;
import com.example.chiunguo.myapplication.R;
import com.example.chiunguo.myapplication.login.LoginActivity;
import com.example.chiunguo.myapplication.login.LoginContract;
import com.example.chiunguo.myapplication.login.LoginPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Any;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class LoginPresentetTest {
    private LoginActivity view;

    @Mock
    private LoginPresenter loginPresenter;

  private  LoginContract.View mView;

  private  LoginActivity loginView;

  private LoginPresenter mPresenter;

    @Before
    public void setup(){
       MockitoAnnotations.initMocks(this);
      // view = spy(Robolectric.buildActivity(LoginActivity.class).visible().get());
       //view=Robolectric.setupActivity(LoginActivity.class);
        //loginPresenter=new LoginPresenter(view);
      mView = mock(LoginContract.View.class);
      mPresenter = new LoginPresenter(mView);
    }

    @Test
    public void testWhenLoginSuccess(){
    when(mView.getPassword()).thenReturn("6666666");
    when(mView.getUsername()).thenReturn("jacj.httt");
    mPresenter.login();
    verify(mView).showDialog("login success");
    }


    //these test  are all
  @Test
    public void testWhenTheUserNameIsEmpty(){
      mPresenter = new LoginPresenter(mView);
        when(mView.getUsername()).thenReturn("");
        mPresenter.login();
        verify(mView).showDialog("login failed");
  }

  @Test
    public void testWhenPasswordIsError(){
      mView = mock(LoginContract.View.class);
      mPresenter = new LoginPresenter(mView);
      when(mView.getUsername()).thenReturn("dddddddddd");
      when(mView.getPassword()).thenReturn("");
      mPresenter.login();
      verify(mView).showDialog("login failed");
  }






}
