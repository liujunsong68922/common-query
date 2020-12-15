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
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

/**
 * 利用一个字符串来进行solr查询，将结果封装成一个jsonarray返回
 * 
 * @author Administrator
 *
 */
@Service
public class MongoSelectOper {
	private MongoDatabase mongoDatabase;
	private String mongocollection;

	public String getMongocollection() {
		return mongocollection;
	}

	public void setMongocollection(String mongocollection) {
		this.mongocollection = mongocollection;
	}

	public MongoDatabase getMongoDatabase() {
		return mongoDatabase;
	}

	public void setMongoDatabase(MongoDatabase mongoDatabase) {
		this.mongoDatabase = mongoDatabase;
	}

	private static final Logger logger = LoggerFactory.getLogger(MongoSelectOper.class);

	private JSONArray getJsonArrayReturnValue(String sql) throws Exception {
		// 获取集合
		MongoCollection<Document> collection = mongoDatabase.getCollection(this.mongocollection);
		// 查找集合中的所有文档
		// 先定义为无条件的表达式
		FindIterable<Document> findIterable = collection.find();
		if (sql.length() > 0) {
			Bson filter = this.convertBson(sql);
			if (filter != null) {
				findIterable = collection.find(filter);
			}
		}
		MongoCursor cursor = findIterable.iterator();
		JSONArray retArray = new JSONArray();
		int ilen = 0;
		while (cursor.hasNext()) {
			ilen++;
			if (ilen >= 100) {
				// 最多返回100条记录，退出循环
				break;
			}
			Document document = (Document) cursor.next();
			Set<String> keyset = document.keySet();
			JSONObject obj = new JSONObject();
			for (String key : keyset) {
				obj.put(key, document.get(key));
			}
			retArray.add(obj);			
		}

		return retArray;

	}

	public JSONArray executeSelect(String strsql) throws Exception {
		logger.info(strsql);
		try {
			return this.getJsonArrayReturnValue(strsql);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception:{}:{}, Error:{}", e.getClass().getName(), e);
			throw new Exception("SQL Error:" + strsql);
		}
	}

	/**
	 * 将bson标准格式的字符串转换为BSON格式,暂时只支持一个变量 输入格式为 字段名 操作符 操作值 ，假设使用空格来进行分隔
	 * 
	 * @param sql
	 * @return
	 */
	private Bson convertBson(String sql) {
		String s1[] = sql.split(" ");
		if (s1.length < 3) {
			return null;
		}

		// 暂时只处理三个元素，第一个是变量名，第二个是操作符，第三个是值
		Bson bson = null;
		if (s1[1].equalsIgnoreCase("=")) {
			// 判断是否是数字
			if (isNumber(s1[2])) {
				bson = Filters.eq(s1[0], Double.valueOf(s1[2]));
			} else {
				bson = Filters.eq(s1[0], s1[2]);
			}
		} else if (s1[1].equalsIgnoreCase(">")) {
			if (isNumber(s1[2])) {
				bson = Filters.gt(s1[0], Double.valueOf(s1[2]));
			} else {
				bson = Filters.gt(s1[0], s1[2]);
			}
		} else if (s1[1].equalsIgnoreCase(">=")) {
			if (isNumber(s1[2])) {
				bson = Filters.gte(s1[0], Double.valueOf(s1[2]));
			} else {
				bson = Filters.gte(s1[0], s1[2]);
			}
		} else if (s1[1].equalsIgnoreCase("<")) {
			if (isNumber(s1[2])) {
				bson = Filters.lt(s1[0], Double.valueOf(s1[2]));
			} else {
				bson = Filters.lt(s1[0], s1[2]);
			}
		} else if (s1[1].equalsIgnoreCase("<=")) {
			if (isNumber(s1[2])) {
				bson = Filters.lte(s1[0], Double.valueOf(s1[2]));
			} else {
				bson = Filters.lte(s1[0], s1[2]);
			}
		} else {
			System.out.println("Unknown oper:" + s1[1]);
			return null;
		}
		return bson;
	}

	private boolean isNumber(String s1) {
		if (s1.length() == 0) {
			return false;
		}
		if (s1.charAt(0) >= '0' && s1.charAt(0) <= '9') {
			return true;
		}
		if (s1.charAt(0) == '-') {
			return true;
		}
		return false;
	}
}
