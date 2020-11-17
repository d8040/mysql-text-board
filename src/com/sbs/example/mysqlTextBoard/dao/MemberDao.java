package com.sbs.example.mysqlTextBoard.dao;

import java.util.Map;

import com.sbs.example.mysqlTextBoard.dto.Member;
import com.sbs.example.mysqlTextBoard.mysqlutil.MysqlUtil;
import com.sbs.example.mysqlTextBoard.mysqlutil.SecSql;

public class MemberDao {


	public int join(String loginId, String loginPw, String name) {
		SecSql sql = new SecSql();

		sql.append("INSERT INTO member");
		sql.append(" SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", loginId = ?", loginId);
		sql.append(", loginPw = ?", loginPw);
		sql.append(", name = ?", name);

		return MysqlUtil.insert(sql);
	}

	public Member getMemberByLoginId(String MemberId) {
		SecSql sql = new SecSql();
		sql.append("SELECT *");
		sql.append("FROM member");
		sql.append("WHERE loginId = ?", MemberId);
		
		Map<String, Object> memberMap = MysqlUtil.selectRow(sql);
		if (memberMap.isEmpty()) {
			return null;
		}
		return new Member(memberMap);
		
	}

	public Member getMemberByMemberId(int memberId) {
		SecSql sql = new SecSql();
		sql.append("SELECT *");
		sql.append("FROM member");
		sql.append("WHERE id = ?", memberId);
		
		Map<String, Object> memberMap = MysqlUtil.selectRow(sql);
		if (memberMap.isEmpty()) {
			return null;
		}
		return new Member(memberMap);
		
	}
}
