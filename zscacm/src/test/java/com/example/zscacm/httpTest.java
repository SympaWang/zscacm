package com.example.zscacm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = ZscacmApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class httpTest {

    @Test
    public void test() {
        String url = "https://codeforces.com/api/contest.list";
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).get().build();
        try (Response response = client.newCall(request).execute()) {  
            String str = response.body().string();
            String arr = JSON.parseObject(str).get("result").toString();
            List<HashMap> list = JSON.parseArray(arr,HashMap.class);
            System.out.println(list.get(0));
        } catch (IOException e) {
            throw new RuntimeException(e); 
        }
    }
}
