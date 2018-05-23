package com.example.chiunguo.myapplication;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.widget.Toast;

import com.example.chiunguo.myapplication.demo.Login;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowToast;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

//   @Test
//    public void login(){
//        MainActivity mainActivity= Robolectric.setupActivity(MainActivity.class);
//        mainActivity.findViewById(R.id.gotologin).performClick();
//        //assertEquals(1,1);
//       // 判断启动的activity是否是登录页面
//      ShadowActivity shadowActivity= Shadows.shadowOf(mainActivity);
//
//       Intent actualIntent = shadowActivity.getNextStartedActivity();
//       assertEquals(actualIntent.getComponent().getClassName(),Login.class.getName());
//   }

   @Test
    public  void loginToast(){
        Login login=Robolectric.setupActivity(Login.class);
      Toast toast= ShadowToast.getLatestToast();
       //开始尚未弹出toast
       assertNull(toast);

       login.findViewById(R.id.login_button).performClick();
       toast=ShadowToast.getLatestToast();
       assertNotNull(toast);

       TextInputLayout usernmame=login.findViewById(R.id.username);
       TextInputLayout password=login.findViewById(R.id.password);
       usernmame.getEditText().setText("jaac.aaaa");
       password.getEditText().setText("6666666");
       login.findViewById(R.id.login_button).performClick();
      // toast=ShadowToast.getLatestToast();
       assertNotNull(toast);
       assertEquals("登录成功",ShadowToast.getTextOfLatestToast());
       System.out.println("toast"+ShadowToast.getTextOfLatestToast());
   }
   @Test
    public  void loginTest(){

   }

   @Test
    public void testMock(){
        List mockedList=mock(List.class);

       //using mock object
       mockedList.add("one");
       mockedList.clear();

       //verification
       verify(mockedList).add("one");
       verify(mockedList).clear();


       //stubbing
       when(mockedList.get(0)).thenReturn("first");
       when(mockedList.get(1)).thenThrow(new RuntimeException());

//following prints "first"
       System.out.println(mockedList.get(0));

       //following prints "null" because get(999) was not stubbed
       System.out.println(mockedList.get(999));

//following throws runtime exception
     //  System.out.println(mockedList.get(1));

       LinkedList mockedLinckedList = mock(LinkedList.class);
//stubbing using built-in anyInt() argument matcher
       when(mockedLinckedList.get(anyInt())).thenReturn("element");

//following prints "element"
       System.out.println(mockedLinckedList.get(999));

//you can also verify using an argument matcher
       verify(mockedLinckedList).get(anyInt());







   }

}