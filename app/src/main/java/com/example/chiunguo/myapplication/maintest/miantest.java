package com.example.chiunguo.myapplication.maintest;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.example.chiunguo.myapplication.ServiceApi.DemoService;
import com.example.chiunguo.myapplication.model.Bean;
import com.example.chiunguo.myapplication.util.FastJsonUtil;
import com.example.chiunguo.myapplication.util.HttpUtil;
import com.example.chiunguo.myapplication.util.StringUtils;

import org.junit.Assert;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;

public class miantest {
static Bean bean;
    public static void main(String []args){

        HttpUtil httpUtil=new HttpUtil();
        String a="http://news-at.zhihu.com/api/4/themes";
       String b="http://news-at.zhihu.com/api/4/news/before/20131119";
       String c="http://47.94.242.124:8080/libraryone/loginforperson";
        httpUtil.get(b);

//        Retrofit retrofit=new Retrofit.Builder()
//                .baseUrl("http://news-at.zhihu.com/api/4/news/before/")
//                .build();
//        DemoService demoService=retrofit.create(DemoService.class);
//      //  Bean bean=new Bean();
//        Call<ResponseBody> call = demoService.getDemo();
//        // 用法和OkHttp的call如出一辙,
//        // 不同的是如果是Android系统回调方法执行在主线程
//        call.enqueue(new Callback<ResponseBody>()
//        {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
//            {
//                try {
//                    System.out.println(response.body().string());
//                  //  String a= response.body().toString();
//                   // System.out.println(a);
//                     bean= JSON.parseObject(response.body().string(),Bean.class);
//                    //System.out.println(bean.getDate().toString());
//                  //  System.out.println(bean.getDate().toString());
//                //    Assert.assertNotNull(bean.getDate());
//                   // Log.i("d",bean.getDate().toString());
//                }
//                catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                t.printStackTrace(); } });
//

    }


}
