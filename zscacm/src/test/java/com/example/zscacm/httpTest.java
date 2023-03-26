package com.example.zscacm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.zscacm.entity.CfUser;
import com.example.zscacm.utils.CfApiUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.zscacm.utils.JwtUtil.parseJWT;

@SpringBootTest(classes = ZscacmApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class httpTest {

    @Resource
    private CfApiUtil cfApiUtil;

    @Test
    public void UserTest() {
        CfUser user = cfApiUtil.getUserDetail("Sympa");
        System.out.println(user.toString());
    }


    @Test
    public void testPassword() throws Exception {
        System.out.println(parseJWT("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0YmNiZDQ2MmE2ZDY0ODBhODZiNGYzMjE2ZDIyMTBkZSIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTY3NzEyOTIyNSwiZXhwIjoxNjc3MTMyODI1fQ.hT0-Yf7q37f2j51GaW5ObMVdW4z-KEDCNSDiwWdQMjo"));
    }
}
