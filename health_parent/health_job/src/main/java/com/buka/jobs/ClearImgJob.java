package com.buka.jobs;

import com.buka.constant.RedisConstant;
import com.buka.utils.FTPUtils;
import org.apache.commons.net.ftp.FTP;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImgJob {
    @Autowired
    private  JedisPool jedisPool;
    public void clearImg(){
        FTPUtils ftpUtils = new FTPUtils();
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES,RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        System.out.println("job");
        for (String s : set) {
            ftpUtils.deleteFile("/home/www/pages",s);
            System.out.println("deleting.."+s);
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,s);
        }
    }

//    public static void main(String[] args) {
//        FTPUtils ftpUtils = new FTPUtils();
//        ftpUtils.deleteFile("/home/www/pages","facf4c6c-fca1-4bcd-b267-607014a9f61c2.jpg");
//    }
}
