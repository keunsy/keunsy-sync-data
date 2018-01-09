//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.keunsy.syncdata.common.utils;

import org.apache.commons.logging.Log;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Repository
public class SimpleDAO {
  protected Log log = CommonLogFactory.getLog(this.getClass());
  @Resource(
    name = "dataSource"
  )
  protected DataSource dataSource;

  public SimpleDAO() {
  }

  public DataSource getDataSource() {
    return this.dataSource;
  }

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void execute(String sql) {
    Connection conn = null;
    Statement stmt = null;

    try {
      conn = this.dataSource.getConnection();
      stmt = conn.createStatement();
      stmt.execute(sql);
    } catch (SQLException var8) {
      var8.printStackTrace();
      this.log.error("Base DAO execute Exception!", var8);
    } finally {
      DBUtil.freeConnection(conn, stmt);
    }

  }

  public void execute(String sql, Object[] params) {
    Connection conn = null;
    PreparedStatement ps = null;

    try {
      conn = this.dataSource.getConnection();
      ps = conn.prepareStatement(sql);

      for(int i = 0; params != null && i < params.length; ++i) {
        ps.setObject(i + 1, params[i]);
      }

      ps.execute();
    } catch (SQLException var9) {
      var9.printStackTrace();
      this.log.error("Base DAO execute Exception!", var9);
    } finally {
      DBUtil.freeConnection(conn, ps);
    }

  }

  public int executeUpdate(String sql, Object[] params) {
    Connection conn = null;
    PreparedStatement ps = null;

    int result;
    try {
      conn = this.dataSource.getConnection();
      ps = conn.prepareStatement(sql);

      for(int i = 0; params != null && i < params.length; ++i) {
        ps.setObject(i + 1, params[i]);
      }

      result = ps.executeUpdate();
    } catch (SQLException var10) {
      var10.printStackTrace();
      result = -1;
      this.log.error("Base DAO executeUpdate Exception!", var10);
    } finally {
      DBUtil.freeConnection(conn, ps);
    }

    return result;
  }

  public int executeUpdate(String sql) {
    return this.executeUpdate(sql, (Object[])null);
  }

  public boolean hasTable(String tableName) {
    boolean result = false;
    Connection conn = null;
    PreparedStatement prep = null;
    ResultSet rs = null;
    String sql = "show tables like '" + tableName + "'";

    try {
      conn = this.dataSource.getConnection();
      prep = conn.prepareStatement(sql);
      rs = prep.executeQuery(sql);
      if(rs.next()) {
        result = true;
      }
    } catch (SQLException var11) {
      var11.printStackTrace();
      this.log.error("Base DAO hasTable Exception!", var11);
    } finally {
      DBUtil.freeConnection(conn, prep, rs);
    }

    return result;
  }
}
