/**    
 * 文件名：SqlUtil.java    
 *    
 * 版本信息：    
 * 日期：2015-9-30    
 * Copyright 足下 Corporation 2015     
 * 版权所有    
 *    
 */
package com.keunsy.syncdata.common.utils;

import java.util.ArrayList;
import java.util.List;

/**    
 *     
 * 项目名称：syncDataSubscriber    
 * 类名称：SqlUtil    
 * 类描述：    
 * 创建人：chenrong1    
 * 创建时间：2015-9-30 下午3:04:59    
 * 修改人：chenrong1    
 * 修改时间：2015-9-30 下午3:04:59    
 * 修改备注：    
 * @version     
 *     
 */
public class SqlUtil {

    /** 
     * 生成insert语句  形如：insert into user_info(user_id) values();
     * @method createInsertSql         
     * @return String 
     */
    public static String createInsertSql(String table_name, String column) {

        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("insert into ").append(table_name).append(SeparatorUtil.SPACE).append(SeparatorUtil.OPEN_PAREN)
                .append(column.trim()).append(SeparatorUtil.CLOSE_PAREN).append(" values");

        String[] column_array = GeneralUtil.createArray(column, SeparatorUtil.COMMA);
        if (column_array != null && column_array.length > 0) {
            sBuffer.append(SeparatorUtil.OPEN_PAREN);
            for (int i = 0, len = column_array.length; i < len; i++) {
                sBuffer.append(SeparatorUtil.QUESTION_MARK);
                if (i != len - 1) {
                    sBuffer.append(SeparatorUtil.COMMA);
                }
            }
            sBuffer.append(SeparatorUtil.CLOSE_PAREN);
        }
        return sBuffer.toString();
    }

    /** 
     * 生成查询sql语句 形如： select user_id from user_info where user_id=?
     * @method createSelectSql         
     * @return String 
    */
    public static String createSelectSql(String table_name, String column, String where_column) {

        String sql = "select " + column + " from " + table_name;

        //拼接where 语句
        String sql_where = createWhereSql(where_column);

        return sql + sql_where;
    }

    /** 
     * 生成更新sql 语句
     * @param where_column 
     * @method createUpdateSql         
     * @return String 
     */
    public static String createUpdateSql(String table_name, String column, String where_column) {

        String update_sql = "update " + table_name + " set ";
        //拼接set值语句
        String sql_set = createSetSql(column);
        //拼接where 语句
        String sql_where = createWhereSql(where_column);

        return update_sql + sql_set + sql_where;
    }

    /** 
     * 生成set中间语句 如： set user_id=?,server_id=?
     * @method createSetSql         
     * @return String 
     */
    public static String createSetSql(String column) {

        StringBuffer sBuffer = new StringBuffer();
        String[] column_array = GeneralUtil.createArray(column, SeparatorUtil.COMMA);
        if (column_array != null && column_array.length > 0) {
            sBuffer.append(SeparatorUtil.SPACE);
            for (int i = 0, len = column_array.length; i < len; i++) {
                sBuffer.append(column_array[i]).append(SeparatorUtil.EQUAL_MARK).append(SeparatorUtil.QUESTION_MARK);
                if (i != len - 1) {
                    sBuffer.append(SeparatorUtil.COMMA);
                }
            }
            sBuffer.append(SeparatorUtil.SPACE);
        }
        return sBuffer.toString();
    }

    /** 
     * 生成where 条件 如  where user_id=? and server_id=?
     * @method createWhereSql         
     * @return String 
     */
    public static String createWhereSql(String where_column) {

        StringBuffer sBuffer = new StringBuffer();
        String[] where_array = GeneralUtil.createArray(where_column, SeparatorUtil.COMMA);
        if (where_array != null && where_array.length > 0) {
            sBuffer.append(" where ");
            for (int i = 0, len = where_array.length; i < len; i++) {
                sBuffer.append(where_array[i]).append(SeparatorUtil.EQUAL_MARK).append(SeparatorUtil.QUESTION_MARK);
                if (i != len - 1) {
                    sBuffer.append(" and ");
                }
            }
        }
        return sBuffer.toString();
    }

    /** 
     * 反射获取字段值数组
     * @method getParamObject         
     * @return Object[] 
     */
    public static Object[] getParamObject(String column, Object obj) {

        Object[] objects = null;
        String[] column_array = GeneralUtil.createArray(column, SeparatorUtil.COMMA);
        if (column_array != null && column_array.length > 0) {
            int len = column_array.length;
            objects = new Object[len];
            for (int i = 0; i < len; i++) {
                //反射取值
                objects[i] = ReflectionUtils.getPropertyValueByField(obj, column_array[i]);
            }
        }
        return objects;
    }

    /** 
     * @method main         
     * @return void 
    */
    public static void main(String[] args) {
        StringBuffer sbBuffer = new StringBuffer();
        String aString = "1111";
        for (int i = 0; i < 100; i++) {
            sbBuffer.append("1235858699");
            aString += "11114545";
        }
        System.out.println(aString);
        System.out.println(aString.length());
        List<Integer> list = new ArrayList<Integer>();
        list.add(null);
        for (Integer integer : list) {
            System.out.println(integer);
        }

        System.out
                .println("1235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699123585869912358586991235858699"
                        .length());
        System.out.println(sbBuffer.toString());
        System.out.println(sbBuffer.toString().length());
    }
}
