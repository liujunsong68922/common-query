package com.liu.commonquery.dao;


import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.liu.commonquery.vo.CommonQuerySQLVO;
//import com.liu.commonquery.vo.QuerySQLVO;

public class QuerySQLDAO {
	private DataSource datasource;

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}
	/**
	 * default sql table name
	 * 默认的存储sql查询的数据表的名称
	 */
	private String sqltablename ="common_query_sql";
	
	//get鏂规硶
	public String getSqltablename() {
		return sqltablename;
	}

	//set鏂规硶
	public void setSqltablename(String sqltablename) {
		this.sqltablename = sqltablename;
	}


	public CommonQuerySQLVO getQuerySQLById(long id) {
		String strsql="select * from "+this.sqltablename;
		strsql += " Where id="+id;
		
		JdbcTemplate jt = new JdbcTemplate(this.datasource);
		
		List<Map<String,Object>> list1 = jt.queryForList(strsql);
		if(list1.size()==0) {
			return null;
		}else {
			Map map1 = list1.get(0);
			CommonQuerySQLVO querysql = new CommonQuerySQLVO();
			querysql.setId(id);
			querysql.setQueryname((String)map1.get("queryname"));
			querysql.setQuerysql((String)map1.get("querysql"));
			querysql.setQueryargs((String)map1.get("queryargs"));
			querysql.setMemo((String)map1.get("memo"));
			//增加一个字段，获取数据权限的标志符号
			querysql.setDataprivilegeflag((String)map1.get("dataprivilegeflag"));
			//增加一个字段，获取数据查询的查询类型
			//不同的查询类型下一步会使用不同的处理方式进行处理
			querysql.setQuerytype((String)map1.get("querytype"));
			//增加一个字段，记录mongocollection
			querysql.setMongocollection((String)map1.get("mongocollection"));
			return querysql;
		}

	}
}
