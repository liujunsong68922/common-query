package com.liu.commonquery.impl;

import javax.sql.DataSource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liu.commonquery.IDataQuery;
import com.liu.commonquery.dao.QuerySQLDAO;
import com.liu.commonquery.exception.CommonQueryException;
import com.liu.commonquery.service.DBSelectOper;
import com.liu.commonquery.service.MongoSelectOper;
import com.liu.commonquery.service.QuerySQLService;
import com.liu.commonquery.service.SolrSelectOper;
import com.liu.commonquery.vo.CommonQuerySQLVO;
import com.liu.commonqueryprivilege.IUserDataPrivilege;
import com.liu.commonqueryprivilege.UserDataPrivilegeFactory;
import com.liu.commonqueryprivilege.UserDataPrivilegeResultVO;
import com.mongodb.client.MongoDatabase;

public class MySQLDataQueryImpl implements IDataQuery {
	private DataSource datasource;
	
	private String solrhome;
	
	private MongoDatabase mongoDatabase;
	//private String mongocollection; 
	/**
	 * set DataSource
	 */
	@Override
	public void setDataSource(DataSource ds) {
		this.datasource = ds;

	}

	private String sqlTablename = "";

	@Override
	public JSONArray getData(long qid, JSONObject qargs) throws Exception {
		QuerySQLService querysqlService = new QuerySQLService();
		querysqlService.setDs(datasource);

		// 判断是否需要设置数据表名称
		if (this.sqlTablename != null && this.sqlTablename.length() > 0) {
			querysqlService.setSqlTablename(this.sqlTablename);
		}

		CommonQuerySQLVO sqlvo = querysqlService.getQuerySQLById(qid);
		if (sqlvo == null) {
			System.out.println("ERROR,sqlvo is null.Qid is not exist.qid:" + qid);
			return null;
		}

		// 获取要执行的SQL语句
		String strsql = querysqlService.getSQLString(qid, qargs);
		System.out.println("strsql:" + strsql);
		// 执行SQL语句，目前仅支持查询语句
		JSONArray ret;

		// 此处需要根据sql查询里面配置的sql类型来走不同的分支进行处理
		// 未来可以考虑修改成一个工厂属性。
		if (sqlvo.getQuerytype() == null || sqlvo.getQuerytype().equalsIgnoreCase("sql")) {
			//封装对于sql语句的查询支持工作
			DBSelectOper dbSelectOper = new DBSelectOper();
			dbSelectOper.setDatasource(datasource);

			ret = dbSelectOper.executeSelect(strsql);
			return ret;
		}else if(sqlvo.getQuerytype().equalsIgnoreCase("solr")) {
			//封装对于solr查询的支持工作
			SolrSelectOper solrSelectOper = new SolrSelectOper();
			solrSelectOper.setSolrhome(this.solrhome);
			
			ret = solrSelectOper.executeSelect(strsql);
			return ret;			
		}else if(sqlvo.getQuerytype().equalsIgnoreCase("mongo")) {
			if(sqlvo.getMongocollection()==null) {
				System.out.println("mongocollection is null");
				return null;
			}
			//封装对于mongodb查询的支持工作
			MongoSelectOper mongoSelectOper = new MongoSelectOper();
			//mongoSelectOper.setMongocollection(this.mongocollection);
			mongoSelectOper.setMongoDatabase(this.mongoDatabase);
			mongoSelectOper.setMongocollection(sqlvo.getMongocollection());
			
			ret = mongoSelectOper.executeSelect(strsql);
			return ret;
		}
		
		System.out.println("querytype is not supported. querytype:"+sqlvo.getQuerytype());
		return null;
	}

	@Override
	public void setSqltablename(String sqltablename) {
		// TODO Auto-generated method stub
		this.sqlTablename = sqltablename;
	}

	@Override
	public JSONArray getDataByUserid(String userid, long qid, JSONObject qargs) throws Exception {
		if (userid == null) {
			System.out.println("userid is null.");
			throw new CommonQueryException("userid is null.");
		}

		// step1:检查用户的访问权限
		// 检查用户的访问权限需要先根据qid来检索对应的funcid

		UserDataPrivilegeFactory factory = new UserDataPrivilegeFactory();
		IUserDataPrivilege udp = factory.getUserDataPrivilege("mysql");
		udp.setDataSource(this.datasource);

		String qfuncid = udp.getqueryfuncid("" + qid);
		// cannot find qid in functable
		if (qfuncid == null) {
			System.out.println("Cannot find qid in functable.qid:" + qid);
			throw new CommonQueryException("Cannot find qid in functable:");
		}

		// 检查当前的
		QuerySQLDAO querysqldao = new QuerySQLDAO();
		querysqldao.setDatasource(this.datasource);

		CommonQuerySQLVO sqlvo = querysqldao.getQuerySQLById(qid);
		if (sqlvo == null) {
			System.out.println("Canot find qid in querytable.qid:" + qid);
			throw new CommonQueryException("Canot find qid in querytable.qid:" + qid);
		} else {
			System.out.println("dataprivilegeflag:" + sqlvo.getDataprivilegeflag());
		}

		// 根据querysql的定义来判断是否需要进行数据权限的检查。
		if (sqlvo.getDataprivilegeflag() != null && sqlvo.getDataprivilegeflag().equals("1")) {
			// 需要数据权限的控制，调用接口来获取数据权限结果
			UserDataPrivilegeResultVO vo = udp.getDataPrivilegeResultVO(userid, qfuncid);
			if (vo.getIret() == -1) {
				// 数据权限的计算出现错误
				System.out.println("errmsg:" + vo.getErrMsg());
				throw new CommonQueryException(vo.getErrMsg());
			}
			// 数据权限计算完成，将计算结果加入到qargs
			System.out.println("DataPrivilege compute end.qwhere:" + vo.getStrCondition());
			System.out.println("Add qwhere to sql syntax:" + vo.getStrCondition());
			qargs.put("qwhere", vo.getStrCondition());
		}

		// 调用另一个方法来计算sql语句执行结果
		return this.getData(qid, qargs);
	}

	@Override
	public void setSolrhome(String solrhome) {
		this.solrhome = solrhome;
		
	}

//	@Override
//	public void setMongocollection(String mongocollection1) {
//		this.mongocollection = mongocollection1;
//		
//	}

	@Override
	public void setMongoDatabase(MongoDatabase mongoDatabase1) {
		this.mongoDatabase = mongoDatabase1;
		
	}

}
