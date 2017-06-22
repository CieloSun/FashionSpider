package com.cielo.tidy;

import com.cielo.utils.JSONUtils;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.*;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by 63289 on 2017/6/22.
 */
public class TidyWeboData {
    private static String path ="target/weibo";
    public static void dictoryMethod(File file){
        if(file.isDirectory()){
            String[] fileList=file.list();
            for (String subFile:fileList){
                dictoryMethod(new File(file.getPath()+"/"+subFile));
            }
        }else {
//            System.out.println(file.getParent()+"\\"+file.getName());
            fileMethod(file);
        }
    }
    public static void fileMethod(File file){
        try {
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String jsonString="";
            String tempString;
            while ((tempString=bufferedReader.readLine())!=null){
                jsonString+=tempString;
            }
            bufferedReader.close();
            jsonString=StringEscapeUtils.unescapeJava(jsonString).replaceAll("<.*?>","");
            Map map= JSONUtils.parseMap(jsonString);
            String created_at=(String) map.get("created_at");
            String date;
            Pattern patternA=Pattern.compile("\\d+-\\d+-\\d+");
            Pattern patternB=Pattern.compile("\\d+-\\d+");
            if(patternA.matcher(created_at).find()){
                date=created_at.substring(0,10);
            }else if (patternB.matcher(created_at).find()){
                date="2017-"+created_at.substring(0,5);
            }else {
                date=null;
            }
            map.put("date",date);
            PrintWriter printWriter=new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)));
            printWriter.print(JSONUtils.toJSON(map));
            printWriter.close();
        }catch (Exception e){
            file.delete();
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        dictoryMethod(new File(path));
    }
}
