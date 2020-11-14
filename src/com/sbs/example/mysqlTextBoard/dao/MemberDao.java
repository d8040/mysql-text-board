package com.sbs.example.mysqlTextBoard.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.sbs.example.mysqlTextBoard.dto.Member;

public class MemberDao {
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;


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
				pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

				pstmt.setString(1, loginId);
				pstmt.setString(2, loginPw);
				pstmt.setString(3, name);

				pstmt.executeUpdate();

				rs = pstmt.getGeneratedKeys();
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

	public Member getMemberByLoginId(String MemberId) {
		Member member = null;

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

			String sql = "SELECT * FROM member WHERE loginid = ?";

			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setString(1, MemberId);
				ResultSet rs = pstmt.executeQuery();

				if (rs.next()) {
					int id = rs.getInt("id");
					String loginId = rs.getString("loginId");
					String loginPw = rs.getString("loginPw");
					String name = rs.getString("name");

					member = new Member(loginId, loginPw, name, id);
				}
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

		return member;
	}

	public void login(String loginId, String loginPw) {
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

			try {
				String SQL = "SELECT loginPw FROM `member` WHERE loginId = ?";

				pstmt = con.prepareStatement(SQL);
				pstmt.setString(1, loginId);
				rs = pstmt.executeQuery();

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

	}

	public Member getMemberByMemberId(int memberId) {
		Member member = null;

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
			String sql = "SELECT * FROM member WHERE id = ?";

			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, memberId);
				ResultSet rs = pstmt.executeQuery();

				if (rs.next()) {
					int id = rs.getInt("id");
					String loginId = rs.getString("loginId");
					String loginPw = rs.getString("loginPw");
					String name = rs.getString("name");

					member = new Member(loginId, loginPw, name, id);
				}
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

		return member;
	}
}
