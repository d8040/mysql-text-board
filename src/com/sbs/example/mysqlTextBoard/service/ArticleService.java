package com.sbs.example.mysqlTextBoard.service;

import java.util.List;

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

	public void modify(int memberId, int inputedId, String title, String body) {
		articleDao.modify(memberId, inputedId, title, body);

	}

	public int add(int boardId, int memberId, String title, String body) {
		return articleDao.add(boardId, memberId, title, body);
	}

	public int makeBoard(int loginedId, String name) {
		return articleDao.makeBoard(loginedId, name);
	}

	public Board getBoardByName(String inputName) {
		return articleDao.getBoardByName(inputName);
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

}
