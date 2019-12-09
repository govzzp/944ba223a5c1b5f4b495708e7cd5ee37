package pers.me.monday.utils;

import java.util.HashMap;

public class HMUtil {
    public static boolean NotNull(HashMap<String,String> hashMap,String ...keys){
        boolean flag = true;
        for(var i:keys){
            if(hashMap.get(i)==null){
                flag = false;
                break;
            }
        }
        return flag;
    }
}
