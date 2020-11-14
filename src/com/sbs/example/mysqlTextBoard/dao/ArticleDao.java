package com.sbs.example.mysqlTextBoard.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sbs.example.mysqlTextBoard.dto.Article;

public class ArticleDao {
	Connection con = null;

	public Connection connect() {

		// MySQL 드라이버 등록
		try {
			String dbmsJdbcUrl = "jdbc:mysql://127.0.0.1:3306/textBoard?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull&connectTimeout=60000&socketTimeout=60000";
			String dbmsLoginId = "sbsst";
			String dbmsLoginPw = "sbs123414";

			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(dbmsJdbcUrl, dbmsLoginId, dbmsLoginPw);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;

	}

	public List<Article> getArticles() {
		List<Article> articles = new ArrayList<>();

		try {
			con = connect();
			String sql = "SELECT * FROM article ORDER BY id DESC";

			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
					int id = rs.getInt("id");
					String regDate = rs.getString("regDate");
					String updateDate = rs.getString("updateDate");
					String title = rs.getString("title");
					String body = rs.getString("body");
					int memberId = rs.getInt("memberId");
					int boardId = rs.getInt("boardId");

					Article article = new Article(id, regDate, updateDate, title, body, memberId, boardId);

					articles.add(article);
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

		return articles;
	}

	public Article getArticle(int id) {
		Article article = null;

		try {
			con = connect();
			String sql = "SELECT * FROM article WHERE id = ?";

			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, id);
				ResultSet rs = pstmt.executeQuery();

				if (rs.next()) {
					int articleId = rs.getInt("id");
					String regDate = rs.getString("regDate");
					String updateDate = rs.getString("updateDate");
					String title = rs.getString("title");
					String body = rs.getString("body");
					int memberId = rs.getInt("memberId");
					int boardId = rs.getInt("boardId");

					article = new Article(articleId, regDate, updateDate, title, body, memberId, boardId);
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

		return article;
	}

	public int delete(int id) {
		int affectedRows = 0;

		try {
			con = connect();

			String sql = "DELETE FROM article WHERE id = ?";

			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, id);
				affectedRows = pstmt.executeUpdate();
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

		return affectedRows;
	}

	public void modify(int inputedId, String title, String body) {
		try {
			con = connect();

			String sql = "UPDATE article set title = ?, body = ?, updateDate = NOW() where id = ?";

			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setInt(3, inputedId);
				pstmt.setString(1, title);
				pstmt.setString(2, body);

				pstmt.executeUpdate();

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

	public int write(int memberId, String title, String body) {
		int id = 0;
		try {
			con = connect();

			String sql = "INSERT INTO article SET regDate = NOW(), updateDate = NOW(), title = ?, body = ?, memberId = ?, boardId = 1";

			try {
				PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

				pstmt.setString(1, title);
				pstmt.setString(2, body);
				pstmt.setInt(3, memberId);

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
