//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.keunsy.syncdata.common.utils;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ResultUtil {
  public ResultUtil() {
  }

  public static <T> List<T> assemble(ResultSet rs, Class<T> bean) {
    ArrayList result = new ArrayList();

    try {
      int r = rs.getMetaData().getColumnCount();

      while(rs.next()) {
        T obj = bean.newInstance();

        for(int i = 1; i <= r; ++i) {
          switch(rs.getMetaData().getColumnType(i)) {
          case -6:
          case 4:
            treatAsInt(rs, obj, i);
            break;
          case -5:
            treatAsLong(rs, obj, i);
            break;
          case 12:
          case 91:
          case 92:
          case 93:
          default:
            treatAsString(rs, obj, i);
          }
        }

        result.add(obj);
      }
    } catch (Exception var6) {
      var6.printStackTrace();
    }

    return result;
  }

  public static <T> T assembleOneBean(ResultSet rs, Class<T> bean) {
    T result = null;

    try {
      int r = rs.getMetaData().getColumnCount();
      T obj = bean.newInstance();

      for(int i = 1; i <= r; ++i) {
        switch(rs.getMetaData().getColumnType(i)) {
        case -6:
        case 4:
          treatAsInt(rs, obj, i);
          break;
        case -5:
          treatAsLong(rs, obj, i);
          break;
        case 12:
        case 91:
        case 92:
        case 93:
        default:
          treatAsString(rs, obj, i);
        }
      }

      result = obj;
    } catch (Exception var6) {
      var6.printStackTrace();
    }

    return result;
  }

  private static void treatAsLong(ResultSet rs, Object bean, int i) throws Exception {
    Method method = bean.getClass().getMethod("set" + FirstUpperCase(rs.getMetaData().getColumnLabel(i)), new Class[]{Long.TYPE});
    method.invoke(bean, new Object[]{Long.valueOf(rs.getLong(i))});
  }

  private static void treatAsString(ResultSet rs, Object bean, int i) throws Exception {
    Method method = bean.getClass().getMethod("set" + FirstUpperCase(rs.getMetaData().getColumnLabel(i)), new Class[]{String.class});
    method.invoke(bean, new Object[]{rs.getString(i)});
  }

  private static void treatAsInt(ResultSet rs, Object bean, int i) throws Exception {
    Method method = bean.getClass().getMethod("set" + FirstUpperCase(rs.getMetaData().getColumnLabel(i)), new Class[]{Integer.TYPE});
    method.invoke(bean, new Object[]{Integer.valueOf(rs.getInt(i))});
  }

  public static String FirstUpperCase(String columnName) {
    String result = columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
    return result;
  }
}
