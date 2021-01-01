package com.sbs.example.mysqlTextBoard.dao;

import com.sbs.example.mysqlTextBoard.mysqlutil.MysqlUtil;
import com.sbs.example.mysqlTextBoard.mysqlutil.SecSql;

public class Ga4DataDao {

	public int deletePagePath(String pagePath) {
		SecSql sql = new SecSql();
		sql.append("DELETE");
		sql.append("FROM ga4DataPagePath");
		sql.append("WHERE pagePath = ?", pagePath);
		
		return MysqlUtil.delete(sql);
	}

	public int savePagePath(String pagePath, int hit) {
		SecSql sql = new SecSql();
		sql.append("INSERT INTO ga4DataPagePath");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", pagePath = ?", pagePath);
		sql.append(", hit = ?", hit);
		
		return MysqlUtil.insert(sql);
	}

}
