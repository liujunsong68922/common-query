package com.liu.commonquery;

import javax.sql.DataSource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.MongoDatabase;

public interface IDataQuery {
	/**
	 * set Datasource
	 * @param ds
	 */
	public void setDataSource(DataSource ds);
	
	/**
	 * 设置solrhome变量，为通过字符串来进行solr查询准备条件
	 * @param solrhome
	 */
	public void setSolrhome(String solrhome);
	
	
	//public void setMongocollection(String mongocollection);
	
	public void setMongoDatabase(MongoDatabase mongoDatabase);
	
	/**
	 * set sql table name
	 * @param sqltablename
	 */
	public void setSqltablename(String sqltablename) ;
	
	/**
	 * 获取数据的方法，这个方法不和用户信息关联，因此不做用户权限认证和数据行权限限制
	 * get data
	 * @param qid
	 * @param qargs
	 * @return
	 * @throws Exception 
	 */
	public JSONArray getData(long qid,JSONObject qargs) throws Exception;
	
	/**
	 * 获取数据的方法，这个方法需要传入用户ID，来做基于用户信息的权限认证和数据权限控制
	 * get Data,Using userid ,qid , qargs as three argument
	 * thus we can do some user auth based on userid.
	 * @param userid,the userid,convert by tokenid in web controller layer
	 * @param qid, long datatype qid,based on table
	 * @param qargs, jsonobject, wrap all argument from request.
	 * @return
	 * @throws Exception
	 */
	public JSONArray getDataByUserid(String userid,long qid,JSONObject qargs) throws Exception;
}
