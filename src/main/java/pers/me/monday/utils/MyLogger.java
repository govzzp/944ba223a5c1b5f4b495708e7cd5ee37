package pers.me.monday.utils;

public class MyLogger{
    public static void log(String path,String ...param){
        if (param.length%2!=0){
            System.out.println("[Logger Error:odd param");
            return;
        }
        StringBuilder logInfo = new StringBuilder();
        int count = 0;
        for (var i:param){
            if(count%2==0){
                logInfo.append(i).append(":");
            }else{
                logInfo.append(i).append(" ");
            }
            count++;
        }
        System.out.println("[REQ:  "+path+"  "+logInfo+"]");
    }
}
