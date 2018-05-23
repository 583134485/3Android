package com.example.chiunguo.myapplication.util;

import com.alibaba.fastjson.JSON;
import com.example.chiunguo.myapplication.model.Bean;
import com.example.chiunguo.myapplication.model.Bean.StoriesBean;




import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {
   public  final  String loginurl="https://www.baidu.com";
    public  String url;
    public String posturl="http://news-at.zhihu.com/api/4/news/before/20131119";
  //  Gson gson=new Gson();
   public void get(String url){
       this.url=url;
       if(url==null||this.url.isEmpty()){
          this.url=loginurl;
       }
        OkHttpClient client=new OkHttpClient();

        Request request=new Request.Builder().get().url(this.url).build();

        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
              //  ToastUtil.showToast(GetActivity.this, "Get 失败");
                System.out.println("faild");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String responseStr = response.body().string();
                System.out.println("resporse"+responseStr);
                Bean bean= JSON.parseObject(responseStr,Bean.class);
                //  Bean bean=new Gson().fromJson(responseStr,Bean.class);
//                 Bean bean = gson.fromJson(response.body().charStream(), Bean.class);
                System.out.println("bean"+bean.getDate());
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                       // tv_result.setText(responseStr);
//                        System.out.println("respnse"+responseStr);
//                    }
//                });
            }
        });
    }

    public void postParameter(String posturl) {
        OkHttpClient client = new OkHttpClient();
 this.posturl=posturl;
        //构建FormBody，传入要提交的参数
        FormBody formBody = new FormBody
                .Builder()
                .add("username", "admin1")
                .add("password", "admin1")
                .build();
        final Request request = new Request.Builder()
                .url(this.posturl)
                .post(formBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //ToastUtil.showToast(PostParameterActivity.this, "Post Parameter 失败");
               System.out.println("failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
               final String responseStr = response.body().string();
                System.out.println("resporse"+responseStr);
              //  Bean bean= JSON.parseObject(responseStr,Bean.class);
              //  Bean bean=new Gson().fromJson(responseStr,Bean.class);
//                 Bean bean = gson.fromJson(response.body().charStream(), Bean.class);
               // System.out.println("bean"+bean);
                 //                List<StoriesBean> stories = bean.getStories();
//                final StringBuilder stringBuilder = new StringBuilder();
//                for (Bean.StoriesBean storiesBean : stories) {
//                   stringBuilder.append(storiesBean);
//                    stringBuilder.append("\n\n\n");
//                }
//                System.out.println(stringBuilder.toString());


                //  System.out.println("response"+bean.toString());
                //  System.out.println("response"+responseStr);
                //ToastUtil.showToast(PostParameterActivity.this, "Code：" + String.valueOf(response.code()));
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        tv_result.setText(responseStr);
//                    }
//                });
            }
        });
    }




}
