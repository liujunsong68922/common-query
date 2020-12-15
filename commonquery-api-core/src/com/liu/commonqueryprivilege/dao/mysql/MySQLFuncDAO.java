package com.liu.commonqueryprivilege.dao.mysql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.liu.commonqueryprivilege.vo.FuncVO;
import com.liu.commonqueryprivilege.vo.RoleFuncVO;

//import com.liu.userdataprivilege.core.vo.FuncVO;
//import com.liu.userdataprivilege.core.vo.RoleFuncVO;

public class MySQLFuncDAO {
	private DataSource datasource;

	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}
	
	private String FUNCTION_TABLENAME="t_function";

	/**
	 * ����rolefunclist����ȡFuncList
	 * @param rolefunclist
	 * @return
	 */
	public List<FuncVO> getFuncList(List<RoleFuncVO> rolefunclist){
		List<FuncVO> funcvolist = new ArrayList<FuncVO>();
		
		String funcs ="";
		for(RoleFuncVO rolefuncvo:rolefunclist) {
			if(funcs.length()==0) {
				funcs += "'" + rolefuncvo.getFuncid() +"'";
			}else {
				funcs += ",'" + rolefuncvo.getFuncid() +"'";
			}
		}
		
		JdbcTemplate jdbctemplate = new JdbcTemplate(this.datasource);
		String strsql = "Select funcid,funcconditions from " + this.FUNCTION_TABLENAME;
		strsql += " Where funcid in ("+funcs+")";
		List<FuncVO> retlist = new ArrayList<FuncVO>();			
		List<Map<String, Object>> list1 = jdbctemplate.queryForList(strsql);
		
		for(Map<String, Object> map1:list1) {
			FuncVO funcvo = new FuncVO();
			funcvo.setFuncid((String)map1.get("funcid"));
			funcvo.setFuncConditions((String)map1.get("funcconditions"));
			retlist.add(funcvo);
		}
		return retlist;
	}
	
	public FuncVO getFuncVOByQueryid(String qid) {
		JdbcTemplate jdbctemplate = new JdbcTemplate(this.datasource);
		String strsql = "Select funcid,funcconditions from " + this.FUNCTION_TABLENAME;		
		strsql += " Where funcqid='"+qid+"'";
		strsql += " And functype='2'";

		List<Map<String, Object>> list1 = jdbctemplate.queryForList(strsql);
		if(list1.size()==0) {
			//�Ҳ���
			return null;
		}
		Map<String,Object> map1 = list1.get(0);
		FuncVO funcvo = new FuncVO();
		funcvo.setFuncid((String)map1.get("funcid"));
		funcvo.setFuncConditions((String)map1.get("funcconditions"));
		
		return funcvo;
	}
}
