package com.cielo.tidy;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

/**
 * Created by 63289 on 2017/6/21.
 */
public class TidyBrandData {
    public static void main(String[] args) throws Exception{
        File file = new File("target/brand/brand.html");
        if(file.canRead()){
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
            String context="";
            String s;
            while ((s=bufferedReader.readLine())!=null){
                context+=s;
            }
            bufferedReader.close();
            Pattern pattern=Pattern.compile("<br>[\\w+\\s\\t]<br>A Détacher</a>")
        }
    }
}
