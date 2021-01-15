package com.sbs.example.mysqlTextBoard.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sbs.example.mysqlTextBoard.dto.Tag;
import com.sbs.example.mysqlTextBoard.mysqlutil.MysqlUtil;
import com.sbs.example.mysqlTextBoard.mysqlutil.SecSql;

public class TagDao {

	public List<Tag> getDedupTagsByRelTypeCode(String relTypeCode) {
		List<Tag> tags = new ArrayList<>();

		SecSql sql = new SecSql();
		sql.append("SELECT T.*");
		sql.append("FROM tag AS T");
		sql.append("WHERE 1");

		if (relTypeCode != null && relTypeCode.length() > 0) {
			sql.append("AND T.relTypeCode = ?", relTypeCode);
		}
		sql.append("GROUP BY T.body");

		sql.append("ORDER BY T.body ASC");

		List<Map<String, Object>> list = MysqlUtil.selectRows(sql);

		for (Map<String, Object> map : list) {
			tags.add(new Tag(map));
		}

		return tags;
	}

	public List<String> getDeudpTagBodiesByRelTypecode(String relTypeCode) {
		List<String> tagBodies = new ArrayList<>();

		SecSql sql = new SecSql();
		sql.append("SELECT T.body");
		sql.append("FROM tag AS T");
		sql.append("WHERE 1");

		if (relTypeCode != null && relTypeCode.length() > 0) {
			sql.append("AND T.relTypeCode = ?", relTypeCode);
		}
		sql.append("GROUP BY T.body");

		sql.append("ORDER BY T.body ASC");

		List<Map<String, Object>> list = MysqlUtil.selectRows(sql);

		for (Map<String, Object> map : list) {
			tagBodies.add((String)map.get("body"));
		}

		return tagBodies;
	}

}
