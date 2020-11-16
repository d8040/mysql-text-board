package com.sbs.example.mysqlTextBoard.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sbs.example.mysqlTextBoard.dto.Article;
import com.sbs.example.mysqlTextBoard.mysqlutil.MysqlUtil;
import com.sbs.example.mysqlTextBoard.mysqlutil.SecSql;

public class ArticleDao {
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	private Article article;

	public Connection connect() {
		String dbmsJdbcUrl = "jdbc:mysql://127.0.0.1:3306/textBoard?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull&connectTimeout=60000&socketTimeout=60000";
		String dbmsLoginId = "sbsst";
		String dbmsLoginPw = "sbs123414";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(dbmsJdbcUrl, dbmsLoginId, dbmsLoginPw);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}

	public int add(int memberId, String title, String body) {
		int id = 0;
		con = connect();
		String sql = "insert into article set regDate = now(), updateDate = NOW(), title = ?, body = ?, memberId = ?, boardId = 1 ";

		try {
			pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			pstmt.setString(1, title);
			pstmt.setString(2, body);
			pstmt.setInt(3, memberId);

			pstmt.executeUpdate();

			rs = pstmt.getGeneratedKeys();
			rs.next();
			id = rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
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

	public List<Article> getArticles() {
		List<Article> articles = new ArrayList<>();
		String sql = "select * from article order by id desc";
		List<Map<String, Object>> articleMapList = MysqlUtil.selectRows(new SecSql().append(sql));
		
		for (Map<String, Object> articleMap : articleMapList) {
			Article article = new Article();
			article.id = (int)articleMap.get("id");
			article.title = (String)articleMap.get("title");
			article.body = (String)articleMap.get("body");
			article.memberId = (int)articleMap.get("memberId");
			article.boardId = (int)articleMap.get("boardId");
			article.regDate = (String)articleMap.get("regDate");
			article.updateDate = (String)articleMap.get("updateDate");
			
			articles.add(article);		
		}

//		try {
//			pstmt = con.prepareStatement(sql);
//			rs = pstmt.executeQuery();
//
//			while (rs.next()) {
//				int id = rs.getInt("id");
//				String title = rs.getString("title");
//				String body = rs.getString("body");
//				int memberId = rs.getInt("memberId");
//				int boardId = rs.getInt("boardId");
//				String regDate = rs.getString("regDate");
//				String updateDate = rs.getString("updateDate");
//
//				article = new Article(id, title, body, memberId, boardId, regDate, updateDate);
//
//				articles.add(article);
//			}
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//
//			try {
//				if (con != null) {
//					con.close();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}

		return articles;
	}

	public Article getArticles(int inputId) {
		List<Article> articles = new ArrayList<>();
		String sql = "select * from article where id = ?";
		Map<String, Object> articleMapList = MysqlUtil.selectRow(new SecSql().append(sql,inputId));
		
		Article article = new Article();
		
		article.id = (int)articleMapList.get("id");
		article.title = (String)articleMapList.get("title");
		article.body = (String)articleMapList.get("body");
		article.memberId = (int)articleMapList.get("memberId");
		article.boardId = (int)articleMapList.get("boardId");
		article.regDate = (String)articleMapList.get("regDate");
		article.updateDate = (String)articleMapList.get("updateDate");
		
		articles.add(article);
//		Article article = null;
//
//		con = connect();
//		String sql = "select * from article where id = ?";
//
//		try {
//			pstmt = con.prepareStatement(sql);
//			pstmt.setInt(1, inputId);
//			rs = pstmt.executeQuery();

//			if (rs.next()) {
//				int id = rs.getInt("id");
//				String title = rs.getString("title");
//				String body = rs.getString("body");
//				int memberId = rs.getInt("memberId");
//				int boardId = rs.getInt("boardId");
//				String regDate = rs.getString("regDate");
//				String updateDate = rs.getString("updateDate");
//
//				article = new Article(id, title, body, memberId, boardId, regDate, updateDate);
//
//			}
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//
//			try {
//				if (con != null) {
//					con.close();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}

		return article;
	}

	public void modify(int inputid, int memberId, String title, String body) {

		con = connect();
		String sql = "update article set updateDate = NOW(), title = ?, body = ?, memberId = ? where id = ? ";

		try {
			pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			pstmt.setString(1, title);
			pstmt.setString(2, body);
			pstmt.setInt(3, memberId);
			pstmt.setInt(4, inputid);

			pstmt.executeUpdate();

			rs = pstmt.getGeneratedKeys();
			rs.next();

		} catch (SQLException e) {
			e.printStackTrace();
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

	public void delete(int inputId) {

		con = connect();
		String sql = "delete from article where id = ? ";

		try {
			pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			pstmt.setInt(1, inputId);

			pstmt.executeUpdate();

			rs = pstmt.getGeneratedKeys();
			rs.next();

		} catch (SQLException e) {
			e.printStackTrace();
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
}
