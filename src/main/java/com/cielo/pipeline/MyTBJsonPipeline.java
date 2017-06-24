package com.cielo.pipeline;

import com.alibaba.fastjson.JSON;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by 63289 on 2017/6/23.
 */
public class MyTBJsonPipeline extends FilePersistentBase implements Pipeline {
    public MyTBJsonPipeline(String path) {
        this.setPath(path);
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        try {
            Iterator iterator = resultItems.getAll().values().iterator();
            while (iterator.hasNext()) {
                Map map = (Map) iterator.next();
//                System.out.println(map);
                String name = map.get("itemId").toString();
                if (map.get("raw_title") == null) {
                    if (map.get("rateList")!=null)
                        name += "_tmall_comment";
                    else name += "_taobao_comment";
                }
                PrintWriter printWriter = new PrintWriter(new FileWriter(this.getFile(path + name + ".json")));
                printWriter.write(JSON.toJSONString(map));
                printWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
