package com.example.chiunguo.myapplication.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
     private  static  Pattern pattern;
     private  static Matcher matcher;

     private static final String USERNAME_PATTERN = "^[a-zA-Z0-9]([._](?![._])|[a-zA-Z0-9]){6,18}[a-zA-Z0-9]$";

     public StringUtils(){

     }

     /**
      * Validate username with regular expression
      * @param username username for validation
      * @return true valid username, false invalid username
      */
     public static boolean validate(final String username){
         pattern = Pattern.compile(USERNAME_PATTERN);
         matcher = pattern.matcher(username);
         return matcher.matches();

     }
}
