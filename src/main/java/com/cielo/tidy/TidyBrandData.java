package com.cielo.tidy;
import com.cielo.utils.JSONUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 63289 on 2017/6/21.
 */
public class TidyBrandData {
    public static void main(String[] args) throws Exception {
        List<String[]> list = new ArrayList();
        File file = new File("target/brand/brand.html");
        if (file.canRead()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            String context = "";
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                context += s;
            }
            bufferedReader.close();
            context = context.replaceAll("<li><a href=.*? target=.*?><img src=.*? alt=.*?><br><br>", "");
            String[] contexts = context.replaceAll("<img src=.*?>", "")
                    .replaceAll("<a href=.*?>", "").replaceAll("</a></li>", "")
                    .replaceAll("<li .*?>", "").replaceAll("</li>", "")
                    .replaceAll("<ul .*?>", "").replaceAll("</ul>", "")
                    .replaceAll("<h2 .*?>", "").replaceAll("</h2>", "")
                    .replaceAll("<br><br>", "").split("  ");
            for (String string : contexts) {
                if (string.length() > 1) {
                    String[] toPair = string.split("<br>");
                    list.add(toPair);
                    File outPut=new File("target/brand/brand.json");
                    FileWriter fileWriter=new FileWriter(outPut);
                    fileWriter.write(JSONUtils.toJSON(list));
                }
            }
        }
    }
}
