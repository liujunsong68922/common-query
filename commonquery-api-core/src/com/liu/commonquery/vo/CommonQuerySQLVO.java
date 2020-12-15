package com.liu.commonquery.vo;

public class CommonQuerySQLVO {
	private Long id;
	private String queryname;
	private String querysql;
	private String queryargs;
	private String memo;
	//数据权限的标志位 1-需要数据权限控制 0-不需要数据权限控制
	private String dataprivilegeflag;
	//记录数据查询的类型，不同的querytype下一步会采用不同的查询方式来获取数据结果
	private String querytype;
	//mongodb专用，记录连接的collection名称
	private String mongocollection;
	
	
	public String getMongocollection() {
		return mongocollection;
	}

	public void setMongocollection(String mongocollection) {
		this.mongocollection = mongocollection;
	}

	public String getDataprivilegeflag() {
		return dataprivilegeflag;
	}

	public void setDataprivilegeflag(String dataprivilegeflag) {
		this.dataprivilegeflag = dataprivilegeflag;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQueryname() {
		return queryname;
	}

	public void setQueryname(String queryname) {
		this.queryname = queryname;
	}

	public String getQuerysql() {
		return querysql;
	}

	public void setQuerysql(String querysql) {
		this.querysql = querysql;
	}

	public String getQueryargs() {
		return queryargs;
	}

	public void setQueryargs(String queryargs) {
		this.queryargs = queryargs;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getQuerytype() {
		return querytype;
	}

	public void setQuerytype(String querytype) {
		this.querytype = querytype;
	}
	
	
}
