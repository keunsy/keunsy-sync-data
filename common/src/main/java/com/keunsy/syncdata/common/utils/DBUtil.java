package com.keunsy.syncdata.common.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
  public DBUtil() {
  }

  public static void freeConnection(Connection conn) {
    if(conn != null) {
      try {
        conn.close();
      } catch (SQLException var2) {
        System.out.println(var2.getLocalizedMessage());
      }
    }

  }

  public static void freeConnection(Connection conn, Statement st) {
    close(st);
    freeConnection(conn);
  }

  public static void freeConnection(Connection conn, Statement st, ResultSet rs) {
    close(rs);
    close(st);
    freeConnection(conn);
  }

  public static void close(ResultSet rs) {
    try {
      if(rs != null) {
        rs.close();
      }
    } catch (Exception var2) {
      System.out.println(var2.getLocalizedMessage());
    }

  }

  public static void close(Statement st) {
    try {
      if(st != null) {
        st.close();
      }
    } catch (Exception var2) {
      System.out.println(var2.getLocalizedMessage());
    }

  }
}
