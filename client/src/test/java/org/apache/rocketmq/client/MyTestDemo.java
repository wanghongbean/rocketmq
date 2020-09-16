package org.apache.rocketmq.client;

import org.apache.rocketmq.common.MixAll;
import org.apache.rocketmq.common.UtilAll;
import org.apache.rocketmq.common.utils.HttpTinyClient;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * 我的测试演示
 *
 * @author labu
 * @date 2020/09/16
 */
public class MyTestDemo {

    @Test
    public void test_message_compress() throws IOException {
        byte[] body = "This is a very huge message!".getBytes();
        int zipCompressLevel = Integer.parseInt(System.getProperty(MixAll.MESSAGE_COMPRESS_LEVEL, "5"));
        //rocketMq 默认消息body超过4k就压缩body 可以通过 producer.setCompressMsgBodyOverHowmuch(16); 指定值
        int compressMsgBodyOverHowmuch = 1024 * 4;
        System.out.println("compress before body length :"+body.length);
        byte[] data = UtilAll.compress(body, zipCompressLevel);
        System.out.println("compress after body length :"+data.length);
        Assert.assertEquals(true,body.length>compressMsgBodyOverHowmuch);
    }

    /**
     * 项目中有用到大量数据传输的问题可以考虑使用数据压缩，提升传输效率
     * 经纬度热力图 半个月数据1818k 压缩后 365k
     *
     */
    @Test
    public void test_mydata_compress() throws IOException {
        String url ="http://img.ucdn.static.helijia.com/city/aggregate/6m/110100/6/heatmapData.js";
        HttpTinyClient.HttpResult httpResult = HttpTinyClient.httpGet(url, null, null,"UTF-8",5000);
        byte[] bytes = httpResult.content.getBytes();
        System.out.println("compress before body length :"+bytes.length/1024);//1818k
        byte[] data = UtilAll.compress(bytes, 5);
        System.out.println("compress after body length :"+data.length/1024);//365k
    }
}
