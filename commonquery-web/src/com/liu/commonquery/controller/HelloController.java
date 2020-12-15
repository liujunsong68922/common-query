package com.liu.commonquery.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liu.commonquery.DataQueryFactory;
import com.liu.commonquery.IDataQuery;
import com.liu.commonquery.tokenuser.IToken2User;
import com.liu.commonquery.tokenuser.Token2UserFactory;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;


@Controller
@RequestMapping("/hello")
public class HelloController {
	@Resource(name = "dataSource")
	private DataSource dataSource;

	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate() {
		// this.jdbcTemplate = jdbcTemplate;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@RequestMapping(value = "/list")
	@ResponseBody
	public String list(HttpServletRequest request) {
		System.out.println("HelloController.list:");
		return "OK";
	}

	@RequestMapping(value = "/test")
	@ResponseBody
	public JSONArray test(HttpServletRequest request) throws Exception {
		System.out.println("HelloController.testset:");
		
		DataQueryFactory factory = new DataQueryFactory();
		IDataQuery service = factory.getIDataQuery("mysql");
		
		service.setDataSource(dataSource);
		long qid=2;
		
		JSONObject qargs = new JSONObject();
		//qargs.put("tes", "");
		JSONArray ret = service.getData(qid, qargs);
		return ret;
	}

	@RequestMapping(value = "/testsolr")
	@ResponseBody
	public JSONArray testsolr(HttpServletRequest request) throws Exception {
		System.out.println("HelloController.testsolrset:");
		
		DataQueryFactory factory = new DataQueryFactory();
		IDataQuery service = factory.getIDataQuery("mysql");
		
		service.setDataSource(dataSource);
		service.setSolrhome("http://localhost/solr/user");
		long qid=3;
		
		JSONObject qargs = new JSONObject();
		//qargs.put("tes", "");
		JSONArray ret = service.getData(qid, qargs);
		return ret;
	}
	
	@RequestMapping(value = "/testmongo")
	@ResponseBody
	public JSONArray testmongo(HttpServletRequest request) throws Exception {
		System.out.println("HelloController.testmongo:");
		
		DataQueryFactory factory = new DataQueryFactory();
		IDataQuery service = factory.getIDataQuery("mysql");
		
		service.setDataSource(dataSource);
		//连接到mongodb
        //连接到 mongodb 服务
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        //连接到数据库
        MongoDatabase mongoDatabase = mongoClient.getDatabase("test");		
        service.setMongoDatabase(mongoDatabase);
        //service.setMongocollection("user0");
        
		long qid=4;
		
		JSONObject qargs = new JSONObject();
		//qargs.put("tes", "");
		JSONArray ret = service.getData(qid, qargs);
		return ret;
	}	

	@RequestMapping(value = "/test2/{tokenid}")
	@ResponseBody
	public JSONArray test2(HttpServletRequest request,@PathVariable String tokenid) throws Exception {
		System.out.println("HelloController.testset2,tokenid:"+tokenid);
		
		//把tokenid转换成userid;
		String userid;
		Token2UserFactory fact = new Token2UserFactory();
		IToken2User toke2user = fact.getIToken2User("mysql");
		toke2user.setDataSource(this.dataSource);
		
		userid = toke2user.getUserIdByTokenId(tokenid);
		System.out.println("userid:"+(userid==null?"null":userid));
		
		DataQueryFactory factory = new DataQueryFactory();
		IDataQuery service = factory.getIDataQuery("mysql");
		
		service.setDataSource(dataSource);
		long qid=1;
		
		JSONObject qargs = new JSONObject();
		//qargs.put("tes", "");
		JSONArray ret =service.getDataByUserid(userid, qid, qargs);
		return ret;
	}

}

