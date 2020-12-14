package com.sbs.example.mysqlTextBoard.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sbs.example.mysqlTextBoard.dto.Article;
import com.sbs.example.mysqlTextBoard.dto.Board;
import com.sbs.example.mysqlTextBoard.dto.Reply;
import com.sbs.example.mysqlTextBoard.mysqlutil.MysqlUtil;
import com.sbs.example.mysqlTextBoard.mysqlutil.SecSql;

public class ArticleDao {

	public int add(int boardId, int memberId, String title, String body) {
		SecSql sql = new SecSql();

		sql.append("INSERT INTO article");
		sql.append(" SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", boardId = ?", boardId);
		sql.append(", memberId = ?", memberId);
		sql.append(", title = ?", title);
		sql.append(", body = ?", body);
		sql.append(", rcmCount = ?", 0);
		sql.append(", hit = ?", 0);

		return MysqlUtil.insert(sql);

	}

	public List<Article> getArticles(int boardId) {
		List<Article> articles = new ArrayList<>();

		SecSql sql = new SecSql();
		sql.append("SELECT *");
		sql.append("FROM article");
		sql.append("WHERE boardId = ?", boardId);
		sql.append("ORDER BY id DESC");

		List<Map<String, Object>> articleMapList = MysqlUtil.selectRows(sql);

		for (Map<String, Object> articleMap : articleMapList) {
			articles.add(new Article(articleMap));
		}

		return articles;
	}

	public List<Article> getArticles() {
		List<Article> articles = new ArrayList<>();

		SecSql sql = new SecSql();
		sql.append("SELECT *");
		sql.append("FROM article");
		sql.append("ORDER BY id DESC");

		List<Map<String, Object>> articleMapList = MysqlUtil.selectRows(sql);

		for (Map<String, Object> articleMap : articleMapList) {
			articles.add(new Article(articleMap));
		}

		return articles;
	}

	public Article getArticle(int inputId) {

		SecSql sql = new SecSql();
		sql.append("SELECT *");
		sql.append("FROM article");
		sql.append("WHERE id = ?", inputId);

		Map<String, Object> articleMap = MysqlUtil.selectRow(sql);
		if (articleMap.isEmpty()) {
			return null;
		}
		return new Article(articleMap);
	}

	public void modify(int memberId, int inputId, String title, String body) {

		SecSql sql = new SecSql();
		sql.append("UPDATE article");
		sql.append("SET updateDate = NOW()");
		sql.append(", memberId = ?", memberId);
		sql.append(", title = ?", title);
		sql.append(", body = ?", body);
		sql.append("where id = ?", inputId);

		MysqlUtil.update(sql);
	}

	public void delete(int inputId) {

		SecSql sql = new SecSql();
		sql.append("delete");
		sql.append("FROM article");
		sql.append("WHERE id = ?", inputId);

		MysqlUtil.delete(sql);

	}

	public int makeBoard(String name, String code) {
		SecSql sql = new SecSql();

		sql.append("INSERT INTO board");
		sql.append(" SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", name = ?", name);
		sql.append(", code = ?", code);

		return MysqlUtil.insert(sql);
	}

	public Board getBoardByCode(String inputCode) {

		SecSql sql = new SecSql();
		sql.append("SELECT *");
		sql.append("FROM board");
		sql.append("WHERE `code` = ?", inputCode);

		Map<String, Object> boardMap = MysqlUtil.selectRow(sql);
		if (boardMap.isEmpty()) {
			return null;
		}
		return new Board(boardMap);
	}

	public int reply(int memberId, int inputedId, String body) {
		SecSql sql = new SecSql();
		sql.append("INSERT INTO articleReply");
		sql.append(" SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", memberId = ?", memberId);
		sql.append(", body = ?", body);
		sql.append(", articleId = ?", inputedId);

		return MysqlUtil.insert(sql);
	}

	public List<Reply> getRepliesByArticleId(int articleId) {
		List<Reply> replies = new ArrayList<>();

		SecSql sql = new SecSql();
		sql.append("SELECT *");
		sql.append("FROM articleReply");
		sql.append("WHERE articleId = ?", articleId);
		sql.append("ORDER BY id DESC");

		List<Map<String, Object>> replyMapList = MysqlUtil.selectRows(sql);

		for (Map<String, Object> replyMap : replyMapList) {
			replies.add(new Reply(replyMap));
		}
		return replies;
	}

	public Reply getReplyByArticleId(int inputedId) {
		SecSql sql = new SecSql();
		sql.append("SELECT *");
		sql.append("FROM articleReply");
		sql.append("WHERE id = ?", inputedId);

		Map<String, Object> replyMap = MysqlUtil.selectRow(sql);
		if (replyMap.isEmpty()) {
			return null;
		}
		return new Reply(replyMap);
	}

	public void replyModify(int memberId, int inputedId, String body) {
		SecSql sql = new SecSql();
		sql.append("UPDATE articleReply");
		sql.append("SET updateDate = NOW()");
		sql.append(", memberId = ?", memberId);
		sql.append(", body = ?", body);
		sql.append("where id = ?", inputedId);

		MysqlUtil.update(sql);
	}

	public void deleteReply(int inputedId) {
		SecSql sql = new SecSql();
		sql.append("delete");
		sql.append("FROM articleReply");
		sql.append("WHERE id = ?", inputedId);

		MysqlUtil.delete(sql);
	}

	public List<Article> getForPrintArticles(int boardId) {
		List<Article> articles = new ArrayList<>();

		SecSql sql = new SecSql();
		sql.append("SELECT *");
		sql.append(", M.name AS extra_writer");
		sql.append("FROM article AS A");
		sql.append("INNER JOIN `member` AS M");
		sql.append("ON A.memberId = M.id");
		if (boardId != 0) {
			sql.append("WHERE boardId = ?", boardId);
		}
		sql.append("ORDER BY A.id DESC");

		List<Map<String, Object>> articleMapList = MysqlUtil.selectRows(sql);

		for (Map<String, Object> articleMap : articleMapList) {
			articles.add(new Article(articleMap));
		}

		return articles;
	}

	public int recommand(int memberId, int articleId) {
		SecSql sql = new SecSql();
		sql.append("INSERT INTO recommand");
		sql.append("(updateDate, memberId, articleId)");
		sql.append("SELECT NOW(), ?, ?", memberId, articleId);
		sql.append("FROM DUAL WHERE NOT EXISTS");
		sql.append("(SELECT * FROM recommand WHERE memberId = ? AND articleId = ?)", memberId, articleId);

		MysqlUtil.insert(sql);
		SecSql sql1 = new SecSql();
		sql1.append(
				"UPDATE article SET rcmCount = (SELECT COUNT(articleId) FROM recommand WHERE articleId = ?) WHERE id = ?",
				articleId, articleId);

		return MysqlUtil.update(sql1);

	}

	public Board getBoardByName(String name) {
		SecSql sql = new SecSql();
		sql.append("SELECT *");
		sql.append("FROM board");
		sql.append("WHERE `name` = ?", name);

		Map<String, Object> boardMap = MysqlUtil.selectRow(sql);
		if (boardMap.isEmpty()) {
			return null;
		}
		return new Board(boardMap);
	}

	public List<Board> getForPrintBoards() {
		List<Board> boards = new ArrayList<>();

		SecSql sql = new SecSql();
		sql.append("SELECT *");
		sql.append("FROM board");
		sql.append("ORDER BY id DESC");

		List<Map<String, Object>> boardMapList = MysqlUtil.selectRows(sql);

		for (Map<String, Object> boardMap : boardMapList) {
			boards.add(new Board(boardMap));
		}

		return boards;
	}

	public int getArticlesCount(int boardId) {

		SecSql sql = new SecSql();
		sql.append("SELECT COUNT(*)");
		sql.append("FROM article");
		sql.append("WHERE boardId = ?", boardId);

		return MysqlUtil.selectRowIntValue(sql);
	}

	public int cancleRcmd(int memberId, int articleId) {
		SecSql sql = new SecSql();
		sql.append("DELETE FROM recommand WHERE memberId = ? and articleId = ?", memberId, articleId);

		return MysqlUtil.delete(sql);
	}

	public void addHitCount(int inputedId) {

		SecSql sql = new SecSql();
		sql.append("UPDATE article");
		sql.append("SET hit = hit + 1");
		sql.append("WHERE id = ?", inputedId);

		MysqlUtil.update(sql);
	}

	public Article getForPrintArticle(int articleId, int boardId) {
		
		SecSql sql = new SecSql();
		sql.append("SELECT *");
		sql.append("FROM article");
		sql.append("WHERE boardId = ?", boardId);
		sql.append("AND id = ?", articleId);

		Map<String, Object> articleMap = MysqlUtil.selectRow(sql);
		if (articleMap.isEmpty()) {
			return null;
		}
		return new Article(articleMap);
	}

	public List<Article> getArticlesByPaging(int boardId, int end, int paging) {
		List<Article> articles = new ArrayList<>();

		SecSql sql = new SecSql();
		sql.append("SELECT *");
		sql.append("FROM article");
		if (boardId != 0) {
			sql.append("WHERE boardId = ?", boardId);
		}
		sql.append("ORDER BY id DESC");
		sql.append("LIMIT ?, ?", end, paging);

		List<Map<String, Object>> articleMapList = MysqlUtil.selectRows(sql);

		for (Map<String, Object> articleMap : articleMapList) {
			articles.add(new Article(articleMap));
		}

		return articles;
	}

	public List<Article> getArticlesByPagingAll(int start, int page) {
		List<Article> articles = new ArrayList<>();

		SecSql sql = new SecSql();
		sql.append("SELECT *");
		sql.append("FROM article");
		sql.append("ORDER BY id DESC");
		sql.append("LIMIT ?, ?", start, page);

		List<Map<String, Object>> articleMapList = MysqlUtil.selectRows(sql);

		for (Map<String, Object> articleMap : articleMapList) {
			articles.add(new Article(articleMap));
		}

		return articles;
	}

	public int getHitByAllArticles() {
				
		SecSql sql = new SecSql();
		sql.append("SELECT SUM(hit) FROM article");
				
		return MysqlUtil.selectRowIntValue(sql);
	}

	public int getHitByBoardArticles(int boardId) {
		SecSql sql = new SecSql();
		sql.append("SELECT SUM(hit) FROM article WHERE boardId = ?", boardId);
				
		return MysqlUtil.selectRowIntValue(sql);
	}

	public Board getBoardByid(int boardId) {
		SecSql sql = new SecSql();
		sql.append("SELECT *");
		sql.append("FROM board");
		sql.append("WHERE `id` = ?", boardId);

		Map<String, Object> boardMap = MysqlUtil.selectRow(sql);
		if (boardMap.isEmpty()) {
			return null;
		}
		return new Board(boardMap);
	}
}