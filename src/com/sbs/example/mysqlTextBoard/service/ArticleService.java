package com.sbs.example.mysqlTextBoard.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sbs.example.mysqlTextBoard.dao.ArticleDao;
import com.sbs.example.mysqlTextBoard.dto.Article;
import com.sbs.example.mysqlTextBoard.dto.Board;
import com.sbs.example.mysqlTextBoard.dto.Reply;

public class ArticleService {
	private ArticleDao articleDao;

	public ArticleService() {
		articleDao = new ArticleDao();
	}

	public List<Article> getArticles(int boardId) {
		return articleDao.getArticles(boardId);
	}

	public Article getArticle(int id) {
		return articleDao.getArticle(id);
	}

	public void delete(int id) {
		articleDao.delete(id);
	}

	public int modify(int id, String title, String body) {
		Map<String, Object> modifyArgs = new HashMap<>();
		modifyArgs.put("id", id);
		modifyArgs.put("title", title);
		modifyArgs.put("body", body);

		return modify(modifyArgs);

	}

	public int add(int boardId, int memberId, String title, String body) {
		return articleDao.add(boardId, memberId, title, body);
	}

	public int makeBoard( String name, String code) {
		return articleDao.makeBoard(name, code);
	}

	public Board getBoardByCode(String inputCode) {
		return articleDao.getBoardByCode(inputCode);
	}

	public int reply(int memberId, int inputedId, String body) {
		return articleDao.reply(memberId, inputedId, body);

	}

	public List<Reply> getRepliesByArticleId(int articleId) {
		return articleDao.getRepliesByArticleId(articleId);
	}

	public Reply getReplyByArticleId(int inputedId) {
		return articleDao.getReplyByArticleId(inputedId);
	}

	public void replyModify(int memberId, int inputedId, String body) {
		articleDao.replyModify(memberId, inputedId, body);

	}

	public void deleteReply(int inputedId) {
		articleDao.deleteReply(inputedId);

	}

	public List<Article> getForPrintArticles(int boardId) {
		return articleDao.getForPrintArticles(boardId);
	}

	public int recommand(int memberId, int articleId) {
		return articleDao.recommand(memberId, articleId);
	}

	public boolean isMakeBoardAvailableName(String name) {
		Board board = articleDao.getBoardByName(name);

		return board == null;
	}

	public boolean isMakeBoardAvailableCode(String code) {
		Board board = articleDao.getBoardByCode(code);

		return board == null;
	}

	public List<Board> getForPrintBoards() {
		return articleDao.getForPrintBoards();
	}

	public int getArticlesCount(int BoardId) {
		return articleDao.getArticlesCount(BoardId);
	}

	public int cancleRcmd(int memberId, int articleId) {
		return articleDao.cancleRcmd(memberId, articleId);
	}

	public void addHitCount(int inputedId) {
		articleDao.addHitCount(inputedId);
	}

	public List<Article> getForPrintArticles() {
		return articleDao.getArticles();
	}

	public Article getForPrintArticle(int articleId, int boardId) {
		return articleDao.getForPrintArticle(articleId, boardId);
	}

	public List<Article> getArticlesByPaging(int boardId, int start, int paging) {
	return articleDao.getArticlesByPaging(boardId, start, paging);
	}

	public List<Article> getArticlesByPagingAll(int start, int page) {
		return articleDao.getArticlesByPagingAll(start, page);
	}

	public int getHitByAllArticles() {
		return articleDao.getHitByAllArticles();
	}

	public int getHitByBoardArticles(int boardId) {
		return articleDao.getHitByBoardArticles(boardId);
	}

	public Board getBoardByid(int boardId) {
		return articleDao.getBoardByid(boardId);
	}

	public List<Article> getForMainPageArticles() {
		return articleDao.getForMainPageArticles();
	}

	public int modify(Map<String, Object> args) {
		return articleDao.modify(args);
	}

	public void updatePageHits() {
		articleDao.updatePageHits();
	}

}
