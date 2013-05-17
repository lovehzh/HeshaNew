package com.hesha.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtils {
	public static boolean isNameAdressFormat(String email){
        boolean isExist = false;
     
        //  ^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$
        Pattern p = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");//  \\w+@(\\w+.)+[a-z]{2,3}
        Matcher m = p.matcher(email);
        boolean b = m.matches();
        if(b) {
            System.out.println("有效邮件地址");
            isExist=true;
        } else {
            System.out.println("无效邮件地址");
        }
        return isExist;
    }
}
