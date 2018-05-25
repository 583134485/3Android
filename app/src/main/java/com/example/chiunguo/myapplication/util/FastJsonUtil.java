package com.example.chiunguo.myapplication.util;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.example.chiunguo.myapplication.model.Bean;

import org.json.JSONObject;

public class FastJsonUtil<T> {

     public  T ChangeStringToObject(String json ,Bean t){
         T jsonobject;
         try{
            // jsonobject= JSON.parseObject(json,t.getClass());
             //return jsonobject;
         }
         catch (Exception e){
             e.printStackTrace();
             Log.i("转化出错","转化出错");
         }

        return  null;
    }
}
