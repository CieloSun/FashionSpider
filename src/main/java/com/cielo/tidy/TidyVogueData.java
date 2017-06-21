package com.cielo.tidy;

import com.cielo.utils.JSONUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by 63289 on 2017/6/19.
 */
public class TidyVogueData {
    private static Map tempDB = new HashMap();
    private final static String fromPath = "target/in";
    private final static String toPath="target/out";

    public static void storeToDB() {
        File directory = new File(fromPath);
        if (directory.isDirectory()) {
            String[] fileList = directory.list();
            for (String fileName : fileList) {
                File readFile = new File(fromPath + "/" + fileName);
                if (!readFile.isDirectory()) {
                    try {
                        try {
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(readFile), "utf-8"));
                            String fileString = "";
                            String tempString;
                            try {
                                while ((tempString = bufferedReader.readLine()) != null) {
                                    fileString += tempString;
                                }
                                bufferedReader.close();
                                try {
                                    Map map = JSONUtils.parseMap(fileString);
                                    String url = (String) map.get("url");
                                    if (url == null) continue;
                                    String title = (String) map.get("title");
                                    if (title == null) {
                                        String share = (String) map.get("share");
                                        if (share == null) continue;
                                    } else {
                                        String date = ((String) map.get("date")).replaceAll("[年月]", "-")
                                                .replaceAll("日", "");
                                        if (date == null) continue;
                                        map.put("date", date);
                                        String article = (String) map.get("article");
                                        if (article == null) continue;
                                        map.put("article", article);
                                    }
                                    Map exist = (Map) tempDB.get(map.get("url"));
                                    if (exist != null) {
                                        map.putAll(exist);
                                    }
                                    tempDB.put(map.get("url"), map);
                                } catch (IOException e) {
                                    System.out.println("cannot trans json to map.");
                                    e.printStackTrace();
                                }
                            } catch (IOException e) {
                                System.out.println("cannot readFile.");
                                e.printStackTrace();
                            }
                        } catch (FileNotFoundException e) {
                            System.out.println("File not found.");
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        System.out.println("not utf-8 file");
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.out.println("Is file.");
        }
    }

    public static void changeToJSON() {
        Iterator<Map> iterator = tempDB.values().iterator();
        while (iterator.hasNext()) {
            try {
                Map map = iterator.next();
                String jsonString = JSONUtils.toJSON(map);
                File file = new File(toPath + "/" + DigestUtils.md5((String) map.get("url")) + ".json");
                try {
                    try {
                        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
                        printWriter.print(jsonString);
                        printWriter.close();
                    } catch (FileNotFoundException e) {
                        System.out.println("File not found");
                        e.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    System.out.println("Unsupported charset utf-8");
                    e.printStackTrace();
                }
            } catch (JsonProcessingException e) {
                System.out.println("cannot change to JSON");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        storeToDB();
        changeToJSON();
    }
}
