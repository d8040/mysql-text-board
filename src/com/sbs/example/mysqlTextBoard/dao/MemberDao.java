package com.sbs.example.mysqlTextBoard.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MemberDao {
	Connection con = null;

	public int join(String loginId, String loginPw, String name) {
		int id = 0;
		try {
			String dbmsJdbcUrl = "jdbc:mysql://127.0.0.1:3306/textBoard?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull&connectTimeout=60000&socketTimeout=60000";
			String dbmsLoginId = "sbsst";
			String dbmsLoginPw = "sbs123414";

			// MySQL 드라이버 등록
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			// 연결 생성
			try {
				con = DriverManager.getConnection(dbmsJdbcUrl, dbmsLoginId, dbmsLoginPw);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			String sql = "INSERT INTO member SET regDate = NOW(), updateDate = NOW(), loginId = ?, loginPw = ?, name = ?";

			try {
				PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

				pstmt.setString(1, loginId);
				pstmt.setString(2, loginPw);
				pstmt.setString(3, name);

				pstmt.executeUpdate();

				ResultSet rs = pstmt.getGeneratedKeys();
				rs.next();
				id = rs.getInt(1);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return id;
	}
}
