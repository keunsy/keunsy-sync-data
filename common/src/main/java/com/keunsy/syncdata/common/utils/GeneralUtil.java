/**    
 * 文件名：CommonUtil.java    
 *    
 * 版本信息：    
 * 日期：2015-9-22    
 * Copyright 足下 Corporation 2015     
 * 版权所有    
 *    
 */
package com.keunsy.syncdata.common.utils;


import org.apache.commons.lang3.StringUtils;

/**
 *     
 * 项目名称：syncPlateDataTool    
 * 类名称：CommonUtil    
 * 类描述：    
 * 创建人：chenrong1    
 * 创建时间：2015-9-22 上午10:27:41    
 * 修改人：chenrong1    
 * 修改时间：2015-9-22 上午10:27:41    
 * 修改备注：    
 * @version     
 *     
 */
public class GeneralUtil {

    /**
     * 首字母大写
     * @method FirstUpperCase         
     * @return String
     */
    public static String FirstUpperCase(String str) {

        if (StringUtils.isNotBlank(str)) {
            str = str.trim();
            str = str.substring(0, 1).toUpperCase() + str.substring(1);
        }
        return str;
    }

    /**
     * 简单比较是否非未来时间
     * @method isNotFutureTime         
     * @return boolean
     */
    public static boolean isNotFutureTime(String str) {

        boolean flag = true;
        if (StringUtils.isBlank(str)) {
            return false;
        }
        int result = str.compareTo(DateUtil.getDateStr24());
        if (result > 0) {
            flag = false;
        }
        return flag;
    }

    /** 
     * 获取参数
     * @method createColumnArray         
     * @return String[] 
     */
    public static String[] createArray(String str, String separator) {

        String[] array = null;

        if (StringUtils.isNotBlank(str)) {
            str = rmInvalidSeparator(str.trim(), separator);
            array = str.split(separator);
        }

        return array;

    }

    /** 
     * 移除开头无用的分隔符
     * @method rmInvalidSeparator         
     * @return void 
     */
    public static String rmInvalidSeparator(String str, String separator) {

        if (StringUtils.isBlank(str)) {
            return "";
        }
        int index = str.indexOf(separator);
        if (index == 0) {
            str = str.substring(separator.length());
            str = rmInvalidSeparator(str, separator);
        }
        return str;
    }

    /** 
     * @method main         
     * @return void 
    */
    public static void main(String[] args) {

        System.out.println(createArray(",abc", ",").length);

        System.out.println(rmInvalidSeparator(",,,,abc", ",,,"));

    }
}
