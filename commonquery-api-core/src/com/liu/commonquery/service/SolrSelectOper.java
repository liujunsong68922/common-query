package com.liu.commonquery.service;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
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
 * 利用一个字符串来进行solr查询，将结果封装成一个jsonarray返回
 * 
 * @author Administrator
 *
 */
@Service
public class SolrSelectOper {

	private String solrhome;
	
	public String getSolrhome() {
		return solrhome;
	}

	public void setSolrhome(String solrhome) {
		this.solrhome = solrhome;
	}
	
	private static final Logger logger = LoggerFactory.getLogger(SolrSelectOper.class);

	private JSONArray getJsonArrayReturnValue(String sql) throws Exception {

	    // 第一步：创建一个SolrServer对象
	    SolrServer solrServer = new CommonsHttpSolrServer(this.solrhome);
	    // 第二步：创建一个SolrQuery对象。
	    SolrQuery query = new SolrQuery();
	    // 第三步：向SolrQuery中添加查询条件、过滤条件。。。
	    // 此处将定义的sql查询条件直接输入进去
	    query.setQuery(sql);
	    // 第四步：执行查询。得到一个Response对象。
	    QueryResponse response = solrServer.query(query);
	    // 第五步：取查询结果。
	    SolrDocumentList solrDocumentList = response.getResults();
	    System.out.println("查询结果的总记录数：" + solrDocumentList.getNumFound());
	    // 第六步：遍历结果并打印。
		JSONArray retArray = new JSONArray();
	    for (SolrDocument solrDocument : solrDocumentList) {
//	        System.out.println(solrDocument.get("id"));
//	        System.out.println(solrDocument.get("item_title"));
//	        System.out.println(solrDocument.get("item_price"));
	        Collection<String> fields = solrDocument.getFieldNames();
	        JSONObject obj = new JSONObject();
	        retArray.add(obj);
	        for(String field:fields) {
	        	obj.put(field, solrDocument.get(field).toString());
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
