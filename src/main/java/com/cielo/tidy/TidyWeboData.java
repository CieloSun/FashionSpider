package com.cielo.tidy;

import com.cielo.utils.JSONUtils;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by 63289 on 2017/6/22.
 */
public class TidyWeboData {
    private static String path = "target/weibo";
    private static int cnt = 0;

    public static void dictoryMethod(File file) {
        if (file.isDirectory()) {
            String[] fileList = file.list();
            for (String subFile : fileList) {
                dictoryMethod(new File(file.getPath() + "/" + subFile));
            }
        } else {
            fileMethod(file);
        }
    }

    public static void fileMethod(File file) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String jsonString = "";
            String tempString;
            while ((tempString = bufferedReader.readLine()) != null) {
                jsonString += tempString;
            }
            bufferedReader.close();
            jsonString = StringEscapeUtils.unescapeJava(jsonString).replaceAll("<.*?>", "");
            Map map = JSONUtils.parseMap(jsonString);
            if (map.get("created_at") == null) {
                if (map.get("date") == null)
                    file.delete();
                return;
            }
            String created_at = (String) map.get("created_at");
            String date;
            Pattern patternA = Pattern.compile("\\d+-\\d+-\\d+");
            Pattern patternB = Pattern.compile("\\d+-\\d+");
            Pattern patternC = Pattern.compile("\\d{1,2}:\\d{1,2}");
            if (patternA.matcher(created_at).find()) {
                date = created_at.substring(0, 10);
            } else if (patternB.matcher(created_at).find()) {
                date = "2017-" + created_at.substring(0, 5);
            } else if (patternC.matcher(created_at).find()) {
                LocalDate localDate = LocalDate.now();
                date = localDate.getYear() + "-" + localDate.getMonthValue() + "-" + localDate.getDayOfMonth();
            } else {
                file.delete();
                return;
            }
            map.put("date", date);
            map.remove("created_at");
            map.remove("thumbnail_pic");
            map.remove("bmiddle_pic");
            map.remove("original_pic");
            map.remove("itemid");
            map.remove("status");
            map.remove("rid");
            map.remove("cardid");
            map.remove("visible");
            if (map.get("pics") != null) {
                List list = (List) map.get("pics");
                map.put("pics", list.size());
            } else {
                map.put("pics", 0);
            }
            if (map.get("user") != null) {
                Map userMap = (Map) map.get("user");
                userMap.remove("profile_image_url");
                userMap.remove("cover_image_phone");
                userMap.remove("profile_url");
                userMap.remove("description");
                userMap.remove("follow_me");
                userMap.remove("following");
            }
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)));
            printWriter.print(JSONUtils.toJSON(map));
            printWriter.close();
        } catch (Exception e) {
            file.delete();
            System.out.println(cnt++ + " error files.");
//            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        dictoryMethod(new File(path));
    }
}
