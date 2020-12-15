package com.liu.commonqueryprivilege.dao.mysql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.liu.commonqueryprivilege.vo.FuncConditionVO;

//import com.liu.userdataprivilege.core.vo.FuncConditionVO;

public class MySQLFuncConditionDAO {
	private DataSource datasource;

	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}
	private String FUNCCONDITION_TABLENAME = "T_FUNCCONDITION";
	public Map<String,String> getAllConditionMap(){
		JdbcTemplate jdbctemplate = new JdbcTemplate(this.datasource);
		String strsql = "Select conditionid,conditionexpression from " + this.FUNCCONDITION_TABLENAME;

		Map<String,String> retMap = new HashMap<String,String>();
		List<Map<String, Object>> list1 = jdbctemplate.queryForList(strsql);
		for(Map<String, Object> map1:list1) {
			FuncConditionVO vo = new FuncConditionVO();
			vo.setConditionId((String)map1.get("conditionid"));
			vo.setConditionexpression((String)map1.get("conditionexpression"));
			retMap.put(vo.getConditionId(),vo.getConditionexpression());
		}
		return retMap;
	}
}
