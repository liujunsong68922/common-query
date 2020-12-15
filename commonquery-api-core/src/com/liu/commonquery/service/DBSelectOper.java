package com.liu.commonquery.service;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;



/**
 * 鎵ц鏁版嵁搴撲笂鐨凷ELECT鍛戒护锛屾寜鐓ф寚瀹氭牸寮忚繑鍥炲搴旀暟鎹� 
 * 
 * @author Administrator
 *
 */
@Service
public class DBSelectOper {
	private DataSource datasource;
	
	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}

	private JdbcTemplate jdbcTemplate;
	
	private static final Logger logger = LoggerFactory.getLogger(DBSelectOper.class);

	private JSONArray getJsonArrayReturnValue(String sql) throws Exception {
		// 鎵撳紑鏁版嵁搴撹繛锟�?
		//Connection conn = this.getConnection();
		//Statement stat = conn.createStatement();
		jdbcTemplate = new JdbcTemplate(this.datasource);
		
		SqlRowSet rs =  jdbcTemplate.queryForRowSet(sql);
		String sret = "";

		// 鑾峰彇杩斿洖缁撴灉
		SqlRowSetMetaData meta = rs.getMetaData();
		String colname[] = new String[meta.getColumnCount()+1];
		
		String sline = "";
		int col = 0;
		for (col = 1; col <= meta.getColumnCount(); col++) {
			colname[col] = meta.getColumnLabel(col);
		
			if (col < meta.getColumnCount()) {
				sline = sline + meta.getColumnLabel(col) + "\t";
			} else {
				sline = sline + meta.getColumnLabel(col);
			}
		}
		logger.info("sline = " + sline);
		JSONArray retArray = new JSONArray();
		// 鑾峰彇琛ㄥご
		sret = sret + sline + "\r\n";
		for (; rs.next(); sret = sret + sline + "\r\n") {
			JSONObject obj = new JSONObject();
			retArray.add(obj);
			sline = "";
			for (col = 1; col <= meta.getColumnCount(); col++) {
				obj.put(colname[col], rs.getString(col));
				
				if (col < meta.getColumnCount()) {
					sline = sline + rs.getString(col) + "\t";

				} else {
					sline = sline + rs.getString(col);
				}
			}
		}

		return retArray;

	}

	public JSONArray executeSelect(String strsql) throws Exception {
		logger.info(strsql);
		try {
			return this.getJsonArrayReturnValue(strsql);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception:{}:{}, Error:{}", e.getClass().getName(),  e);
			throw new Exception("SQL Error:" + strsql);
		}
	}

}
