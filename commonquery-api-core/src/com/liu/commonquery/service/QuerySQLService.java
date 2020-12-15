package com.liu.commonquery.service;

import java.util.Optional;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.liu.commonquery.dao.QuerySQLDAO;
import com.liu.commonquery.vo.CommonQuerySQLVO;
//import com.liu.commonquery.vo.QuerySQLVO;

public class QuerySQLService {
	private DataSource ds;
	
	public void setDs(DataSource ds) {
		this.ds = ds;
	}

	private String sqlTablename="";
	
	public void setSqlTablename(String sqlTablename) {
		this.sqlTablename = sqlTablename;
	}

	private static final Logger logger = LoggerFactory.getLogger(QuerySQLService.class);

	/**
	 * retrieve by queryid ,and replace all argument by args object
	 * 
	 * @param queryid
	 * @param args
	 * @return
	 * @throws QueryException
	 */
	public String getSQLString(long qid, JSONObject qargs) throws Exception {
		System.out.println("qid=" + qid);
		Long id = (long) qid;

		// 根据id来检索querysqlobj;
		CommonQuerySQLVO querysqlobj = null;

		System.out.println("id=" + id);
		QuerySQLDAO querysqldao = new QuerySQLDAO();
		querysqldao.setDatasource(ds);
		
		//set sqltablename
		//so that sqltablename can be changed by outside.
		if(this.sqlTablename!=null && this.sqlTablename.length()>0) {
			querysqldao.setSqltablename(this.sqlTablename);
		}
		
		CommonQuerySQLVO sql = querysqldao.getQuerySQLById(id);
		
		if (sql != null) {
			querysqlobj = sql;
		}

		if (querysqlobj == null) {
			throw new Exception("Cannot find qid:" + qid);
		}

		String strsql = querysqlobj.getQuerysql();
		if (strsql == null || strsql.length() == 0) {
			throw new Exception("Invalid SQL. queryid:" + qid);
		}

		String qargnames[] = new String[0];
		if (querysqlobj.getQueryargs() == null) {
			// do nothing
		} else {
			qargnames = querysqlobj.getQueryargs().split(","); // 鐢ㄩ�楀彿鍒嗛殧
		}

		// 杩涜鍙傛暟鏇挎崲锛屾湭杈撳叆鍙傛暟锛屾浛鎹负""
		for (int i = 0; i < qargnames.length; i++) {
			String argname = qargnames[i].trim();
			if (argname != null && argname.length() > 0) {
				String argvalue = qargs.getString(qargnames[i]);
				if (argvalue == null) {
					argvalue = "";
				}
				// replace an argname to argvalue
				// support three type of location define
				// 1.support for @,such as @id
				strsql = strsql.replaceAll("@" + argname, argvalue);

				// 2.support for $,such as $id
				strsql = strsql.replaceAll("\\$" + argname, argvalue);

				// 3.support for {},suan as {id}
				strsql = strsql.replaceAll("\\{" + argname + "}", argvalue);
				logger.info("strsql:" + strsql);
			}
		}
		return strsql;
	}
	
	public CommonQuerySQLVO getQuerySQLById(long id) {
		QuerySQLDAO querysqldao = new QuerySQLDAO();
		querysqldao.setDatasource(ds);
		
		//set sqltablename
		//so that sqltablename can be changed by outside.
		if(this.sqlTablename!=null && this.sqlTablename.length()>0) {
			querysqldao.setSqltablename(this.sqlTablename);
		}
		
		CommonQuerySQLVO sqlvo = querysqldao.getQuerySQLById(id);
		return sqlvo;
	}
}
