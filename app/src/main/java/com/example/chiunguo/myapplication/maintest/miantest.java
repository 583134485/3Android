package com.example.chiunguo.myapplication.maintest;

import android.util.Log;

import com.example.chiunguo.myapplication.util.HttpUtil;
import com.example.chiunguo.myapplication.util.StringUtils;

public class miantest {
    public static void main(String []args){
//     boolean a;
//     String username="jack.yangg";
//
//     a= StringUtils.validate(username);
//     System.out.println("result=="+a);
      //  Log.i("test",Boolean.toString(a));
        HttpUtil httpUtil=new HttpUtil();
        String a="http://news-at.zhihu.com/api/4/themes";
        String b="http://news-at.zhihu.com/api/4/news/before/20131119";
       String c="http://47.94.242.124:8080/libraryone/loginforperson";
       // httpUtil.get(b);
        httpUtil.postParameter(c);
    }
}
