package com.cielo.pipeline;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by 63289 on 2017/6/18.
 */
public class MyJSONPipeline extends FilePersistentBase implements Pipeline {
    public MyJSONPipeline(String path){
        this.setPath(path);
    }
    @Override
    public void process(ResultItems resultItems, Task task) {
        if(resultItems.get("url")!=null){
            try {
                PrintWriter printWriter = new PrintWriter(new FileWriter(this.getFile(path + DigestUtils.md5Hex(resultItems.getRequest().getUrl()) + ".json")));
                printWriter.write(JSON.toJSONString(resultItems.getAll()));
                printWriter.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
