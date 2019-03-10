package com.example.cssm;


import com.example.cssm.domain.User;
import com.example.cssm.util.JedisAdapter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CssmApplication.class)
public class JedisTests {


    @Autowired
    JedisAdapter jedisAdapter;


    @Test
    public  void testObject(){
       // JedisAdapter jedisAdapter = new JedisAdapter();
     //   JedisShardInfo shardInfo = new JedisShardInfo("redis://localhost:6379/");//这里是连接的本地地址和端口
      //  shardInfo.setPassword("123456");//这里是密码
       // Jedis jedis = new Jedis(shardInfo);
        //jedis.connect();


        User user = new User();
        user.setHeadUrl("http://image.nowcodeseer");
        user.setName("user");
        user.setPassword("123");
        user.setSalt("salt");

        jedisAdapter.setObject("user",user);
        System.out.println(ToStringBuilder.reflectionToString(user));
    }
}
