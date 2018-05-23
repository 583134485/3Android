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
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class LoginPresentetTest {



    private LoginActivity view;

    @Mock
    private LoginPresenter loginPresenter;


    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
      // view = spy(Robolectric.buildActivity(LoginActivity.class).visible().get());
       view=Robolectric.setupActivity(LoginActivity.class);
        //loginPresenter=new LoginPresenter(view);
    }
    @Test
    public void createPresenter_setsThePresenterToView() {
        // Get a reference to the class under test
      //  loginPresenter = new LoginPresenter(view);

        // Then the presenter is set to the view
        //verify(view).setPresenter(loginPresenter);
    }

    @Test
    public void testLogin(){
        String username="hhhhhhhhh";
        String password="6666666";

       // view = spy(Robolectric.buildActivity(LoginActivity.class).visible().get());
        view=Robolectric.setupActivity(LoginActivity.class);

        view.findViewById(R.id.login_button).performClick();
//        toast=ShadowToast.getLatestToast();
//        assertNotNull(toast);
       // loginPresenter = new LoginPresenter(view);
//        loginPresenter.login(username,password);
//       verify(view).showDialog("login success");

//         username="hhhhhhhhh";
//         password="66666777";
//        loginPresenter = new LoginPresenter(view);
//        loginPresenter.login(username,password);
//        verify(view).showDialog("login failed");
//
//        username="";
//        password="33";
//        loginPresenter = new LoginPresenter(view);
//        loginPresenter.login(username,password);
//        verify(view,times(2)).showDialog("login failed");

    }






}
