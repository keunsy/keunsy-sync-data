package com.keunsy.syncdata.dao;

import com.keunsy.syncdata.common.utils.CommonLogFactory;
import com.keunsy.syncdata.common.utils.DBUtil;
import com.keunsy.syncdata.common.utils.ResultUtil;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * dao基类
 * 
 */
@Repository
public class BasicDAO {

    protected Log log = CommonLogFactory.getLog("jdbcLog");

    /*
     * 数据源注入
     */
    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 基本的sql执行方法，没有返回值，方法负责将输入sql传如数据库执行
     * 
     * @param sql
     */
    public void execute(String sql) {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = dataSource.getConnection();
            stmt = conn.createStatement();

            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.freeConnection(conn, stmt);
            wirteLog(sql);
        }
    }

    /**
     * 执行sql语句（带参数）
     * 
     * @param sql
     * @param params
     */
    public void execute(String sql, Object[] params) {

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; params != null && i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.freeConnection(conn, ps);
            wirteLog(sql);
        }
    }

    /**
     * 执行更新(带参数)
     * 
     * @param sql
     * @param params
     */
    public int executeUpdate(String sql, Object[] params) {

        int result = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; params != null && i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.freeConnection(conn, ps);
            wirteLog(sql);
        }

        return result;
    }

    /**
     * 判断数据要插入的表是否存在
     * 
     * @param tableName
     * @return
     */
    public boolean hasTable(String tableName) {
        Connection conn = null;
        PreparedStatement prep = null;
        ResultSet rs = null;
        String sql = "show tables like '" + tableName + "'";
        try {
            conn = dataSource.getConnection();
            prep = conn.prepareStatement(sql);
            rs = prep.executeQuery(sql);
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.freeConnection(conn, prep, rs);
            wirteLog(sql);
        }
    }

    /**
     * 获取数据
     * @method queryData         
     * @return List<T>
     */
    public <T> List<T> queryData(String sql, Class<T> clz) {

        List<T> resultList = new ArrayList<T>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            resultList = ResultUtil.assemble(rs, clz);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.freeConnection(conn, ps, rs);
            wirteLog(sql);
        }
        return resultList;
    }

    /**
     * 获取数量
     * @method queryCount         
     * @return int
     */
    public int queryCount(String sql, Object[] params) {

        int result = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; params != null && i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            rs = ps.executeQuery();
            //获取数量
            while (rs.next()) {
                result++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.freeConnection(conn, ps);
            wirteLog(sql);
        }

        return result;
    }

    /**
     * 获取数据 sn
     * @method queryData         
     * @return List<Integer>
     */
    public List<Integer> querySnList(String sql) {

        List<Integer> resultList = new ArrayList<Integer>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultList.add(rs.getInt(1));
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        } finally {
            DBUtil.freeConnection(conn, ps, rs);
            wirteLog(sql);
        }
        return resultList;
    }

    /**
     * 获取数据
     * @method queryData         
     * @return List<T>
     */
    public List<Map<String, Object>> queryMapData(String sql) {

        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

        Connection conn = null;
        try {
            conn = dataSource.getConnection();

            QueryRunner queryRunner = new QueryRunner();

            resultList = queryRunner.query(conn, sql, new MapListHandler());

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.freeConnection(conn, null, null);
            wirteLog(sql);
        }
        return resultList;
    }

    /** 
     * 日志记录
     * @method wirteLog         
     * @return void 
     */
    private void wirteLog(String sql) {
        log.info("Excuse Sql:" + sql);
    }
}
