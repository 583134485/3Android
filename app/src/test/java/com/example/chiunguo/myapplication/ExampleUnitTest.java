package com.example.chiunguo.myapplication;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.chiunguo.myapplication.ServiceApi.DemoService;
import com.example.chiunguo.myapplication.demo.Login;
import com.example.chiunguo.myapplication.login.LoginActivity;
import com.example.chiunguo.myapplication.login.LoginContract;
import com.example.chiunguo.myapplication.login.LoginPresenter;
import com.example.chiunguo.myapplication.model.Bean;
import com.example.chiunguo.myapplication.util.FastJsonUtil;

import org.bouncycastle.math.ec.ScaleYPointMap;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowToast;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
   //为什么 放在 这个test 就会有覆盖率  放在 自定义路径的 test 0% coverage
    @Test
    public void testWhenPasswordIsError(){
        // LoginContract.View mView = mock(LoginContract.View.class);
        LoginActivity mView=mock(LoginActivity.class);
       LoginPresenter mPresenter = new LoginPresenter(mView);
        when(mView.getUsername()).thenReturn("dddddddddd");
        when(mView.getPassword()).thenReturn("");

        mPresenter.login();

        verify(mView).showDialog("login failed");
    }
    @Test
    public  void testRetrofitDemo() throws IOException {
        System.out.println("ssss");
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://news-at.zhihu.com/api/4/news/before/")
                .build();
        DemoService demoService=retrofit.create(DemoService.class);

        Call<ResponseBody> call = demoService.getDemo();
        String a=call.execute().body().string();
        System.out.println(a.toString());
        Bean b=JSON.parseObject(a,Bean.class);
        System.out.println(b.getDate().toString());

        // 用法和OkHttp的call如出一辙,
        // 不同的是如果是Android系统回调方法执行在主线程
//        call.enqueue(new Callback<ResponseBody>()
//        {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
//            {
//                try {
//                    System.out.println(response.body().string());
//                     bean= JSON.parseObject(response.body().toString(), Bean.class);
//                    //System.out.println(bean.getDate().toString());
//                  print(bean.getDate().toString());
//                    Assert.assertNotNull(bean.getDate());
//                    Log.i("d",bean.getDate().toString());
//                }
//                catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                t.printStackTrace(); } });
    }

}