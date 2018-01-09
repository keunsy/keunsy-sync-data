package com.keunsy.syncdata.test;

import com.keunsy.syncdata.redis.RedisDcAdapter;
import com.keunsy.syncdata.redis.RedisSentinelAdapter;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 项目名称：dcAdapterProject2(以下测试数据环境为：window项目+linux redis；正式环境理论上更快)
 * 类名称：JedisterTester
 * 类描述：
 * 创建人：chenrong1
 * 创建时间：2015-7-27 下午5:06:14
 * 修改人：chenrong1
 * 修改时间：2015-7-27 下午5:06:14
 * 修改备注：
 */
public class JedisterTester {

  RedisDcAdapter adapter = new RedisSentinelAdapter();

  @Test
  public void addQueueElement() {

    try {
      List<UserInfo> list = new ArrayList<UserInfo>();
      for (int i = 0; i < 10000; i++) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(i);
        userInfo.setName("name" + i);
        list.add(userInfo);
      }
      long time1 = System.currentTimeMillis();
      adapter.addQueueElements("myqueue", list);
      System.out.println("time:" + (System.currentTimeMillis() - time1));
    } catch (Exception e) {
    }
  }

  /*
   * 10000 733 ; 1000 156
   */
  @Test
  public void getQueueElements() {

    System.out.println("start");
    try {
      long time1 = System.currentTimeMillis();
      List<Object> list = adapter.getQueueElements("myqueue", 10000);
      System.out.println("time:" + (System.currentTimeMillis() - time1));
      System.out.println(list.size());
    } catch (Exception e) {

    }
  }

  /*
   * 100000数据 13744ms  10000数据 687ms
   */
  @Test
  public void addHashQueueElements() {

    System.out.println("start");
    Map<String, String> map = new HashMap<String, String>();
    for (int i = 190; i < 200; i++) {
      UserInfo userInfo = new UserInfo();
      userInfo.setId(i);
      userInfo.setName("name" + i);
      map.put("hashmap" + i, "fsefefe" + i);
    }
    try {
      long time1 = System.currentTimeMillis();
      boolean result = adapter.addHashQueueElements("myhashqueue2", map);
      System.out.println("time:" + (System.currentTimeMillis() - time1));
      System.out.println(result);
    } catch (Exception e) {

    }
  }

  /*
   * 10 0-15;100 15-16;1000 62-140;10000 312-343
   */
  @Test
  public void getHashQueueValues() {

    try {
      Set<String> fields = new HashSet<String>();
      for (int i = 0; i < 10000; i++) {
        fields.add("hashmap" + i);
      }

      long time1 = System.currentTimeMillis();
      List<UserInfo> result = adapter.getHashQueueValues("myhashqueue2", fields);
      System.out.println("time:" + (System.currentTimeMillis() - time1));
      System.out.println(result.size());
    } catch (Exception e) {

    }
  }

  @Test
  public void delHashQueueElements() {

    try {
      Set<String> fields = new HashSet<String>();
      for (int i = 0; i < 100; i++) {
        fields.add("hashmap" + i);
      }

      long time1 = System.currentTimeMillis();
      boolean result = adapter.delHashQueueElements("myhashqueue", fields);
      System.out.println("time:" + (System.currentTimeMillis() - time1));
      System.out.println(result);
    } catch (Exception e) {

    }
  }

  @Test
  public void getQueueSize() {

    try {
      long time1 = System.currentTimeMillis();
      long result = adapter.getQueueSize("myqueue");
      System.out.println("time:" + (System.currentTimeMillis() - time1));
      System.out.println(result);
    } catch (Exception e) {

    }
  }

  /*
   * 10000 359-733;1000 63-125;100 0-31
   */
  @Test
  public void putCacheData() {

    Map<String, UserInfo> cacheMap = new HashMap<String, UserInfo>();
    for (int i = 0; i < 10000; i++) {
      UserInfo userInfo = new UserInfo();
      userInfo.setUser_id(i + "");
      userInfo.setCharge_sum(i);
      cacheMap.put("userinfo" + i, userInfo);
    }
    try {
      long time1 = System.currentTimeMillis();
      boolean result = adapter.putCacheData(cacheMap);
      System.out.println("time:" + (System.currentTimeMillis() - time1));
      System.out.println(result);

    } catch (Exception e) {

    }
  }

  @Test
  public void getCacheData() {

    try {
      long time1 = System.currentTimeMillis();
      UserInfo result = (UserInfo) adapter.getCacheData("userinfo0");
      System.out.println("time:" + (System.currentTimeMillis() - time1));
      System.out.println(result.getUser_id() + result.getCharge_sum());

    } catch (Exception e) {

    }
  }

  /*
   * 10 6;100 15-78;1000 78-124;10000 359-551
   */
  @Test
  public void getCacheDataByList() {

    List<String> keys = new ArrayList<String>();
    for (int i = 0; i < 10000; i++) {
      keys.add("userinfo" + i);
    }
    try {
      long time1 = System.currentTimeMillis();
      List<Object> result = adapter.getCacheData(keys);
      System.out.println("time:" + (System.currentTimeMillis() - time1));
      System.out.println(result.size());

    } catch (Exception e) {

    }
  }

  @Test
  public void getRWCacheInStringMode() {

    try {
      long time1 = System.currentTimeMillis();
      List<UserInfo> result = adapter.getRWCacheInStringMode("stringModel1", UserInfo.class);
      System.out.println("time:" + (System.currentTimeMillis() - time1));
      System.out.println(result.size());
    } catch (Exception e) {

    }
  }

  @Test
  public void getRWCache() {

    try {
      long time1 = System.currentTimeMillis();
      List<UserInfo> result = adapter.getRWCache("stringModel[0-9][0-9]", UserInfo.class);
      System.out.println("time:" + (System.currentTimeMillis() - time1));
      System.out.println(result.size());

    } catch (Exception e) {

    }
  }

  /*
   * 10 0;100 17-33;1000 63-99 ;10000 421-733
   */
  @Test
  public void addStringValue() {

    Map<String, UserInfo> cacheMap = new HashMap<String, UserInfo>();
    for (int i = 0; i < 10000; i++) {
      UserInfo userInfo = new UserInfo();
      userInfo.setUser_id(i + "");
      userInfo.setName("name" + i);
      cacheMap.put("key" + i, userInfo);
    }
    try {
      long time1 = System.currentTimeMillis();
      boolean result = adapter.addStringValue("prefix", cacheMap);
      System.out.println("time:" + (System.currentTimeMillis() - time1));
      System.out.println(result);
    } catch (Exception e) {

    }
  }

  /*
   * 10 0-16;100 15-30;1000 62-172;10000 390-437
   */
  @Test
  public void getStringTypeValues() {

    List<String> keys = new ArrayList<String>();
    for (int i = 0; i < 10000; i++) {
      keys.add("prefixkey" + i);
    }
    try {
      long time1 = System.currentTimeMillis();
      List<Object> result = adapter.getStringTypeValues(keys);
      System.out.println("time:" + (System.currentTimeMillis() - time1));
      System.out.println(result.size());
    } catch (Exception e) {

    }
  }

  @Test
  public void publishUpdate() {

    String channels = "channels_list";

    List<UserInfo> list = new ArrayList<UserInfo>();
    for (int i = 0; i < 10; i++) {
      UserInfo userInfo = new UserInfo();
      userInfo.setUser_id(i + "");
      userInfo.setName("nameList" + i);
      list.add(userInfo);
    }
    try {
      adapter.publishUpdate(channels, list);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void subscribeUpdate() {

    String channels = "channels_list";
    adapter.subscribeUpdate(channels, null);
  }

  @Test
  public void getALLHashElements() {

    Map<String, String> map = adapter.getALLHashElements("myhashqueue2");
    for (String keyString : map.keySet()) {
      System.out.println(keyString);
      System.out.println(map.get(keyString));

    }
  }
}
