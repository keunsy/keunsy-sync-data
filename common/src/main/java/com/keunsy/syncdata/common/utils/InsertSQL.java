//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.keunsy.syncdata.common.utils;

import java.sql.SQLException;

public class InsertSQL {
  private String sql;
  private StringBuilder sb;
  private String[] values;
  private int[] tmpValue;
  private boolean hasInsertData = false;
  private String noDataSql = "select 1";

  public InsertSQL(String sql) {
    this.sql = sql;
    this.sb = new StringBuilder(sql.subSequence(0, sql.indexOf("values") + 6));
    String tmpStr = new String(sql);
    int i = 0;
    tmpStr = tmpStr.substring(tmpStr.indexOf("values") + 6);
    tmpStr = tmpStr.substring(tmpStr.indexOf("(") + 1, tmpStr.lastIndexOf(")"));
    this.values = tmpStr.split(",");
    String[] var7 = this.values;
    int var6 = this.values.length;

    int j;
    for(j = 0; j < var6; ++j) {
      String each = var7[j];
      if(each.trim().equals("?")) {
        ++i;
      }
    }

    this.tmpValue = new int[i];
    int k = 0;

    for(j = 0; j < this.values.length; ++j) {
      if(this.values[j].trim().equals("?")) {
        this.tmpValue[k] = j;
        ++k;
      }
    }

  }

  public void setString(int index, String param) {
    if(param == null) {
      this.setNull(index);
    } else {
      param = this.replaceSpecialChar(param);
      this.values[this.tmpValue[index - 1]] = "'" + param + "'";
    }

  }

  private String replaceSpecialChar(String param) {
    String result = null;
    if(param != null) {
      result = param.replace("'", "''");
      result = result.replace("\\", "\\\\");
    }

    return result;
  }

  public void setInt(int index, int param) {
    this.values[this.tmpValue[index - 1]] = String.valueOf(param);
  }

  public void setLong(int index, long param) {
    this.values[this.tmpValue[index - 1]] = String.valueOf(param);
  }

  public void setDouble(int index, double param) {
    this.values[this.tmpValue[index - 1]] = String.valueOf(param);
  }

  public void setObject(int index, Object param) {
    if(param != null) {
      if(param.getClass().equals(Long.class)) {
        this.setLong(index, ((Long)param).longValue());
      } else if(param.getClass().equals(Integer.class)) {
        this.setInt(index, ((Integer)param).intValue());
      } else if(param.getClass().equals(String.class)) {
        this.setString(index, (String)param);
      } else if(param.getClass().equals(Double.class)) {
        this.setDouble(index, ((Double)param).doubleValue());
      } else {
        this.setString(index, param.toString());
      }
    } else {
      this.setNull(index);
    }

  }

  public void setNull(int index) {
    this.values[this.tmpValue[index - 1]] = "null";
  }

  public void addBatch() throws SQLException {
    this.validate();
    this.hasInsertData = true;
    this.sb.append("(");
    String[] var4 = this.values;
    int var3 = this.values.length;

    for(int var2 = 0; var2 < var3; ++var2) {
      String each = var4[var2];
      this.sb.append(each).append(",");
    }

    this.sb.deleteCharAt(this.sb.length() - 1);
    this.sb.append("),");
  }

  private void validate() throws SQLException {
    for(int i = 0; i < this.tmpValue.length; ++i) {
      if(this.values[i] == null) {
        throw new SQLException("insert SQL has unknow param for index : " + (i + 1));
      }
    }

  }

  public void setSql(String sql) {
    this.sql = sql;
  }

  public String getSql() {
    return this.sql;
  }

  public String getFinalSql() {
    String result = this.noDataSql;
    if(this.hasInsertData) {
      result = this.sb.deleteCharAt(this.sb.length() - 1).toString();
    }

    return result;
  }

  public static void main(String[] a) throws Exception {
    String sql = "insert into customer_sms_info (sn,customer_sn,customer_id,td_code,dest_terminal_id,msg_id,msg_content,insert_time,update_time,send_status,response_status,fail_describe,plate_msg_id,move_flag,code,sub_sn,cell_code,charge_count,priority,price)values(?,475,'hongshu2','HFJ1CBY03SHFB',?,'msg_id'  ,?,now(),now(),1,0,'fail_describe','plate_msg_id',0,  ' code',0,'cell_code',1,0,1)";
    InsertSQL insql = new InsertSQL(sql);
    insql.setObject(1, new Exception());
    insql.setString(2, "dongchen_msg_content");
    System.out.println(insql.getFinalSql());
  }
}
