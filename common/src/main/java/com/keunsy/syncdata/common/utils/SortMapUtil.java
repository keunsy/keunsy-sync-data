/**    
 * 文件名：SortMapUtil.java    
 *    
 * 版本信息：    
 * 日期：2015-9-28    
 * Copyright 足下 Corporation 2015     
 * 版权所有    
 *    
 */
package com.keunsy.syncdata.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**    
 *     
 * 项目名称：syncDataPublisher    
 * 类名称：SortMapUtil    
 * 类描述：    
 * 创建人：chenrong1    
 * 创建时间：2015-9-28 下午5:57:27    
 * 修改人：chenrong1    
 * 修改时间：2015-9-28 下午5:57:27    
 * 修改备注：    
 * @version     
 *     
 */
public class SortMapUtil {

    /** 
     * @method main         
     * @return void 
    */
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("a", "1");
        map.put("b", "2");
        map.put("c", "3");
        map.put("d", "4");
        map.put("e", "6");
        map.put("f", "5");

        for (String string : map.keySet()) {
            System.out.println(string + "==" + map.get(string));
        }

        System.out.println("=========================");
        Map<String, String> sortedMap = sortMapByValue(map);
        for (String string : sortedMap.keySet()) {
            System.out.println(string + "==" + map.get(string));
        }

        System.out.println("=========================");

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("1", "fsefe");
        map2.put("8", "sfef");
        map2.put("7", "fse");
        map2.put("4", "sfe");
        map2.put("5", "sfe");
        map2.put("6", "sfe");
        Map<String, String> sortedMap2 = sortMapByIntKey(map2);
        for (String string : sortedMap2.keySet()) {
            System.out.println(string + "==" + map2.get(string));
        }

    }

    /**
     * 根据map的value 值 对map 进行排序
     * @method sortMapByValue         
     * @return Map<String,String>
     */
    public static Map<String, String> sortMapByValue(Map<String, String> oriMap) {

        Map<String, String> sortedMap = new LinkedHashMap<String, String>();
        if (oriMap != null && !oriMap.isEmpty()) {
            List<Entry<String, String>> entryList = new ArrayList<Entry<String, String>>(oriMap.entrySet());
            //排序list
            Collections.sort(entryList, new Comparator<Entry<String, String>>() {
                @Override
                public int compare(Entry<String, String> entry1, Entry<String, String> entry2) {

                    String value1 = entry1.getValue();
                    String value2 = entry2.getValue();
                    //null 默认等同于空 
                    return (value1 == null ? "" : value1).compareTo((value2 == null ? "" : value2));
                }
            });
            Iterator<Entry<String, String>> iter = entryList.iterator();
            Entry<String, String> tmpEntry = null;
            while (iter.hasNext()) {
                tmpEntry = iter.next();
                sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
            }
        }
        return sortedMap;
    }

    /**
     * 根据key排序
     * @method sortMapByIntKey         
     * @return Map<String,String>
     */
    public static Map<String, String> sortMapByIntKey(Map<String, String> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<String, String> sortedMap = new TreeMap<String, String>(new Comparator<String>() {
            @Override
            public int compare(String key1, String key2) {
                int value1 = 0;
                int value2 = 0;
                try {
                    value1 = Integer.parseInt(key1);
                    value2 = Integer.parseInt(key2);
                } catch (Exception e) {
                    value1 = 0;
                    value2 = 0;
                }
                return value1 - value2;
            }
        });
        sortedMap.putAll(oriMap);
        return sortedMap;
    }
}
